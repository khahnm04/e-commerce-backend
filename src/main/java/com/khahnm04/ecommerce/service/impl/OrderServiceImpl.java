package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.common.enums.CouponDiscountType;
import com.khahnm04.ecommerce.common.enums.OrderStatus;
import com.khahnm04.ecommerce.common.enums.PaymentStatus;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.OrderCreationRequest;
import com.khahnm04.ecommerce.dto.request.filter.OrderFilterRequest;
import com.khahnm04.ecommerce.dto.request.OrderItemRequest;
import com.khahnm04.ecommerce.dto.projection.OrderProjection;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.OrderItemResponse;
import com.khahnm04.ecommerce.dto.response.OrderResponse;
import com.khahnm04.ecommerce.entity.*;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.OrderMapper;
import com.khahnm04.ecommerce.repository.*;
import com.khahnm04.ecommerce.service.OrderService;
import com.khahnm04.ecommerce.service.PdfService;
import com.khahnm04.ecommerce.specification.CustomerOrderSpecification;
import com.khahnm04.ecommerce.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final ProductVariantRepository productVariantRepository;
    private final InventoryRepository inventoryRepository;
    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final AuditorAwareConfig auditorAwareConfig;
    private final PdfService pdfService;

    @Override
    @Transactional
    public OrderResponse createOrder(Long customerId, OrderCreationRequest request) {
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        // 1. Process Order Items & Calculate SubTotal
        BigDecimal subTotal = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<Long> variantIdsToClear = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.items()) {
            ProductVariant variant = productVariantRepository.findByIdAndDeletedAtIsNull(itemRequest.productVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

            // Valid Stock
            int updatedRows = inventoryRepository.decreaseQuantity(variant.getId(), itemRequest.quantity().intValue());
            if (updatedRows == 0) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }

            BigDecimal itemTotal = variant.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            subTotal = subTotal.add(itemTotal);

            orderDetails.add(OrderDetail.builder()
                    .productVariant(variant)
                    .unitPrice(variant.getPrice())
                    .quantity(itemRequest.quantity())
                    .build());

            variantIdsToClear.add(variant.getId());
        }

        // 2. Handle Coupon
        BigDecimal discount = BigDecimal.ZERO;
        Coupon coupon = null;
        if (request.couponCode() != null && !request.couponCode().isBlank()) {
            coupon = couponRepository.findByCodeAndDeletedAtIsNull(request.couponCode())
                    .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));

            validateCoupon(coupon, subTotal);
            discount = calculateDiscount(coupon, subTotal);
        }

        BigDecimal total = subTotal.subtract(discount).max(BigDecimal.ZERO);

        // 3. Create Order
        Order order = orderMapper.toOrder(request);
        order.setCode(generateOrderCode());
        order.setCustomer(customer);
        order.setCoupon(coupon != null ? coupon.getCode() : null);
        order.setSubTotal(subTotal);
        order.setDiscount(discount);
        order.setTotal(total);
        order.setStatus(OrderStatus.PENDING);
        order.setExpectedDeliveryDate(LocalDateTime.now().plusDays(3));

        Order savedOrder = orderRepository.save(order);

        // 4. Save Order Details
        for (OrderDetail detail : orderDetails) {
            detail.setOrder(savedOrder);
        }
        orderDetailRepository.saveAll(orderDetails);

        // 5. Save Payment
        Payment payment = Payment.builder()
                .order(savedOrder)
                .paymentMethod(request.paymentMethod())
                .paymentStatus(PaymentStatus.UNPAID)
                .amount(total)
                .build();
        paymentRepository.save(payment);

        // 6. Record Coupon Usage
        if (coupon != null) {
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponRepository.save(coupon);

            CouponUsage usage = CouponUsage.builder()
                    .coupon(coupon)
                    .customer(customer)
                    .order(savedOrder)
                    .discountAmount(discount)
                    .build();
            couponUsageRepository.save(usage);
        }

        // 7. Clear Cart Items
        clearItemsFromCart(customerId, variantIdsToClear);

        return mapToOrderResponse(savedOrder, orderDetails, payment);
    }

    @Override
    public PageResponse<OrderResponse> findAll(OrderFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.page() - 1, filter.size(), OrderSpecification.getSort(filter.sort()));
        Specification<Order> spec = OrderSpecification.buildSpecification(filter);
        return getAllWithFilterAndSearch(spec, pageable);
    }

    @Override
    public PageResponse<OrderResponse> findAllByCustomerId(Long customerId, OrderFilterRequest filter) {
        String sortStr = filter.sort();
        if (sortStr == null || sortStr.equals("createdAt,desc")) {
            sortStr = "createdAt,desc";
        }
        Pageable pageable = PageRequest.of(filter.page() - 1, filter.size(), OrderSpecification.getSort(sortStr));
        Specification<Order> spec = CustomerOrderSpecification.buildSpecification(customerId, filter);
        return getAllWithFilterAndSearch(spec, pageable);
    }

    private PageResponse<OrderResponse> getAllWithFilterAndSearch(Specification<Order> spec, Pageable pageable) {
        Page<OrderProjection> orders = orderRepository
                .findBy(spec, q -> q.as(OrderProjection.class).page(pageable));

        if (orders.isEmpty()) {
            return PageResponse.fromPage(Page.empty(pageable));
        }

        List<Long> orderIds = orders.getContent().stream().map(OrderProjection::getId).toList();
        log.info("Fetching order details for order IDs: {}", orderIds);

        List<Payment> allPayments = paymentRepository.findByOrderIdIn(orderIds);
        List<OrderDetail> allDetails = orderDetailRepository.findByOrderIdIn(orderIds);
        log.info("Found {} order details and {} payments", allDetails.size(), allPayments.size());

        Map<Long, Payment> paymentsMap = allPayments.stream()
                .collect(Collectors.toMap(p -> p.getOrder().getId(), p -> p, (p1, p2) -> p1));

        Map<Long, List<OrderDetail>> detailsMap = allDetails.stream()
                .collect(Collectors.groupingBy(d -> d.getOrder().getId()));

        Page<OrderResponse> responsePage = orders.map(proj -> {
            Payment payment = paymentsMap.get(proj.getId());
            List<OrderDetail> details = detailsMap.getOrDefault(proj.getId(), new ArrayList<>());
            log.debug("Mapping order {}: {} items found", proj.getId(), details.size());

            OrderResponse response = mapProjectionToResponse(proj, payment);

            List<OrderItemResponse> itemResponses = details.stream()
                    .map(orderMapper::toOrderItemResponse)
                    .toList();

            int totalQuantity = details.stream()
                    .mapToInt(detail -> detail.getQuantity() != null ? detail.getQuantity().intValue() : 0)
                    .sum();

            return response.toBuilder()
                    .items(itemResponses)
                    .totalQuantity(totalQuantity)
                    .build();
        });
        return PageResponse.fromPage(responsePage);
    }

    @Override
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return mapToOrderResponse(order);
    }

    @Override
    public OrderResponse getByCustomerIdAndId(Long customerId, Long orderId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return mapToOrderResponse(order);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> orderIds = request.ids();
        String action = request.action().toUpperCase();

        if (orderIds == null || orderIds.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_SELECTION_REQUIRED);
        }

        switch (action) {
            case "PENDING", "CONFIRMED", "SHIPPING", "COMPLETED", "CANCELLED", "RETURNED" -> {
                Set<Order> existingOrders = new HashSet<>(orderRepository.findAllById(orderIds));
                if (orderIds.size() != existingOrders.size()) {
                    throw new AppException(ErrorCode.ORDER_NOT_FOUND);
                }
                OrderStatus nextStatus = OrderStatus.valueOf(action);
                existingOrders.forEach(order -> {
                    validateStatusTransition(order.getStatus(), nextStatus);
                    order.setStatus(nextStatus);
                    if (nextStatus == OrderStatus.COMPLETED) {
                        order.setActualDeliveryDate(LocalDateTime.now());
                    }
                });
                orderRepository.saveAll(existingOrders);
            }
            case "DELETE" -> {
                User currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                orderRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, orderIds);
            }
            case "RESTORE" -> {
                orderRepository.restoreDeletedByIds(orderIds);
            }
            default -> throw new AppException(ErrorCode.ORDER_INVALID_BULK_ACTION);
        }
    }

    @Override
    public byte[] generateOrderInvoice(Long id) {
        OrderResponse orderResponse = getById(id);

        Map<String, Object> data = Map.of(
                "order", orderResponse,
                "printAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")));

        byte[] pdf = pdfService.generatePdfFromTemplate("orders/invoice", data);

        log.info("Invoice generated for order ID: {} by {}", id,
                auditorAwareConfig.getCurrentAuditorReference().getUsername());

        return pdf;
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderDetail> details = orderDetailRepository.findByOrderId(order.getId());
        Payment payment = paymentRepository.findByOrderId(order.getId()).orElse(null);
        return mapToOrderResponse(order, details, payment);
    }

    private OrderResponse mapProjectionToResponse(OrderProjection proj, Payment payment) {
        return OrderResponse.builder()
                .id(proj.getId())
                .code(proj.getCode())
                .subTotal(proj.getSubTotal())
                .discount(proj.getDiscount())
                .total(proj.getTotal())
                .status(proj.getStatus())
                .shippingName(proj.getShippingName())
                .shippingPhone(proj.getShippingPhone())
                .shippingProvince(proj.getShippingProvince())
                .shippingDistrict(proj.getShippingDistrict())
                .shippingWard(proj.getShippingWard())
                .shippingAddressDetail(proj.getShippingAddressDetail())
                .note(proj.getNote())
                .customer(proj.getCustomerFullName())
                .estimatedDeliveryDate(proj.getExpectedDeliveryDate())
                .createdAt(proj.getCreatedAt())
                .paymentMethod(payment != null ? payment.getPaymentMethod() : null)
                .paymentStatus(payment != null ? payment.getPaymentStatus().name() : null)
                .expectedDeliveryDate(proj.getExpectedDeliveryDate())
                .build();
    }

    private OrderResponse mapToOrderResponse(Order order, List<OrderDetail> details, Payment payment) {
        OrderResponse response = orderMapper.toOrderResponse(order)
                .toBuilder()
                .paymentMethod(payment != null ? payment.getPaymentMethod() : null)
                .paymentStatus(payment != null ? payment.getPaymentStatus().name() : null)
                .build();
        if (details != null) {
            List<OrderItemResponse> itemResponses = details.stream()
                    .map(orderMapper::toOrderItemResponse)
                    .toList();
            int totalQuantity = details.stream()
                    .mapToInt(detail -> detail.getQuantity().intValue())
                    .sum();
            response = response.toBuilder()
                    .items(itemResponses)
                    .totalQuantity(totalQuantity)
                    .build();
        }
        return response;
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == next)
            return;

        boolean valid = switch (current) {
            case PENDING -> next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
            case CONFIRMED -> next == OrderStatus.SHIPPING || next == OrderStatus.CANCELLED;
            case SHIPPING -> next == OrderStatus.COMPLETED || next == OrderStatus.RETURNED;
            default -> false;
        };

        if (!valid) {
            throw new AppException(ErrorCode.ORDER_INVALID_STATUS_TRANSITION);
        }
    }

    private void validateCoupon(Coupon coupon, BigDecimal subTotal) {
        if (coupon.getStartDate().isAfter(LocalDateTime.now()) || coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.COUPON_EXPIRED);
        }
        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new AppException(ErrorCode.COUPON_USAGE_LIMIT_REACHED);
        }
        if (coupon.getMinOrderValue() != null && subTotal.compareTo(coupon.getMinOrderValue()) < 0) {
            throw new AppException(ErrorCode.COUPON_MIN_ORDER_VALUE_NOT_MET);
        }
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal subTotal) {
        BigDecimal discount;
        if (coupon.getTypeDiscount() == CouponDiscountType.PERCENTAGE) {
            discount = subTotal.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100));
            if (coupon.getMaxDiscountValue() != null && discount.compareTo(coupon.getMaxDiscountValue()) > 0) {
                discount = coupon.getMaxDiscountValue();
            }
        } else {
            discount = coupon.getValue();
        }
        return discount.min(subTotal);
    }

    private String generateOrderCode() {
        return "ORD-" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + new Random().nextInt(1000);
    }

    private void clearItemsFromCart(Long customerId, List<Long> variantIds) {
        try {
            cartRepository.findByCustomerId(customerId).ifPresent(cart -> {
                cartItemRepository.deleteByCartIdAndProductVariantIdIn(cart.getId(), variantIds);
                log.info("Cleared {} items from cart for customer {}", variantIds.size(), customerId);
            });
        } catch (Exception e) {
            log.error("Failed to clear cart items", e);
        }
    }

}
