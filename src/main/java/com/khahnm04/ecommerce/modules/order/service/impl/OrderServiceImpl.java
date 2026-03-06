package com.khahnm04.ecommerce.modules.order.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.branch.entity.Branch;
import com.khahnm04.ecommerce.modules.branch.repository.BranchRepository;
import com.khahnm04.ecommerce.modules.cart.entity.CartItem;
import com.khahnm04.ecommerce.modules.cart.repository.CartItemRepository;
import com.khahnm04.ecommerce.modules.cart.repository.CartRepository;
import com.khahnm04.ecommerce.modules.order.dto.request.CreateOrderRequest;
import com.khahnm04.ecommerce.modules.order.dto.request.OrderFilterRequest;
import com.khahnm04.ecommerce.modules.order.dto.request.UpdateOrderStatusRequest;
import com.khahnm04.ecommerce.modules.order.dto.response.OrderDetailResponse;
import com.khahnm04.ecommerce.modules.order.dto.response.OrderResponse;
import com.khahnm04.ecommerce.modules.order.entity.Order;
import com.khahnm04.ecommerce.modules.order.entity.OrderDetail;
import com.khahnm04.ecommerce.modules.order.enums.OrderSortField;
import com.khahnm04.ecommerce.modules.order.enums.OrderStatus;
import com.khahnm04.ecommerce.modules.order.mapper.OrderMapper;
import com.khahnm04.ecommerce.modules.order.mapper.mybatis.OrderMyBatisMapper;
import com.khahnm04.ecommerce.modules.order.repository.OrderDetailRepository;
import com.khahnm04.ecommerce.modules.order.repository.OrderRepository;
import com.khahnm04.ecommerce.modules.order.service.OrderService;
import com.khahnm04.ecommerce.modules.payment.entity.Payment;
import com.khahnm04.ecommerce.modules.payment.repository.PaymentRepository;
import com.khahnm04.ecommerce.modules.user.entity.Customer;
import com.khahnm04.ecommerce.modules.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;
    private final OrderMapper orderMapper;
    private final OrderMyBatisMapper orderMyBatisMapper;

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = Map.of(
            OrderStatus.PENDING, Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
            OrderStatus.CONFIRMED, Set.of(OrderStatus.PROCESSING, OrderStatus.CANCELLED),
            OrderStatus.PROCESSING, Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
            OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED, OrderStatus.DELIVERY_FAILED),
            OrderStatus.DELIVERY_FAILED, Set.of(OrderStatus.RETURNED, OrderStatus.SHIPPED)
    );

    private static final Set<OrderStatus> CUSTOMER_CANCELLABLE_STATUSES = Set.of(
            OrderStatus.PENDING,
            OrderStatus.CONFIRMED
    );

    // ======================== Customer APIs ========================

    @Override
    @Transactional
    public OrderResponse createOrder(Long customerId, CreateOrderRequest request) {
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        // Validate cart items belong to this customer
        var cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        List<CartItem> cartItems = cartItemRepository
            .findAllByCartIdAndIdIn(cart.getId(), request.getCartItemIds());

        if (cartItems.isEmpty()) {
            throw new AppException(ErrorCode.CART_EMPTY);
        }

        if (cartItems.size() != request.getCartItemIds().size()) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        Branch branch = branchRepository.findFirstByDeletedAtIsNull()
                .orElseThrow(() -> new AppException(ErrorCode.BRANCH_NOT_FOUND));

        BigDecimal totalAmount = cartItems.stream()
                .map(item -> item.getProductVariant().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String orderCode = generateOrderCode();

        // Create order
        Order order = Order.builder()
                .orderCode(orderCode)
                .customer(customer)
                .branch(branch)
                .totalAmount(totalAmount)
                .orderStatus(OrderStatus.PENDING)
                .shippingName(request.getShippingName())
                .shippingPhone(request.getShippingPhone())
                .shippingProvince(request.getShippingProvince())
                .shippingDistrict(request.getShippingDistrict())
                .shippingWard(request.getShippingWard())
                .shippingAddressDetail(request.getShippingAddressDetail())
                .note(request.getNote())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Create order details from cart items
        List<OrderDetail> orderDetails = cartItems.stream()
                .map(cartItem -> OrderDetail.builder()
                        .order(savedOrder)
                        .productVariant(cartItem.getProductVariant())
                        .unitPrice(cartItem.getProductVariant().getPrice())
                        .quantity(Long.valueOf(cartItem.getQuantity()))
                        .build())
                .toList();

        orderDetailRepository.saveAll(orderDetails);

        // Create payment
        Payment payment = Payment.builder()
                .order(savedOrder)
                .paymentMethod(request.getPaymentMethod())
                .amount(totalAmount)
                .build();

        paymentRepository.save(payment);

        // Remove ordered items from cart
        cartItemRepository.deleteAll(cartItems);

        log.info("Order created: orderCode={}, customerId={}, totalAmount={}", orderCode, customerId, totalAmount);
        return buildOrderResponse(savedOrder, payment, orderDetails);
    }

    @Override
    public PageResponse<OrderResponse> getCustomerOrders(Long customerId, OrderFilterRequest request) {
        request.setCustomerId(customerId);
        return getAllWithFilterAndSearch(request);
    }

    @Override
    public OrderResponse getCustomerOrderById(Long customerId, Long orderId) {
        Order order = orderRepository.findByIdAndCustomerIdAndDeletedAtIsNull(orderId, customerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return buildFullOrderResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long customerId, Long orderId) {
        Order order = orderRepository.findByIdAndCustomerIdAndDeletedAtIsNull(orderId, customerId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (!CUSTOMER_CANCELLABLE_STATUSES.contains(order.getOrderStatus())) {
            throw new AppException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled by customer: orderCode={}, customerId={}", order.getOrderCode(), customerId);
    }

    // ======================== Admin APIs ========================

    @Override
    public PageResponse<OrderResponse> getAllOrders(OrderFilterRequest request) {
        return getAllWithFilterAndSearch(request);
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        return buildFullOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findByIdAndDeletedAtIsNull(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus newStatus = request.getStatus();

        Set<OrderStatus> allowedStatuses = VALID_TRANSITIONS.get(currentStatus);
        if (allowedStatuses == null || !allowedStatuses.contains(newStatus)) {
            throw new AppException(ErrorCode.ORDER_INVALID_STATUS_TRANSITION);
        }

        order.setOrderStatus(newStatus);

        if (newStatus == OrderStatus.SHIPPED) {
            order.setShippedDate(LocalDate.now());
        }

        if (StringUtils.hasText(request.getNote())) {
            order.setNote(request.getNote());
        }

        orderRepository.save(order);

        log.info("Order status updated: orderCode={}, {} -> {}", order.getOrderCode(), currentStatus, newStatus);
        return buildFullOrderResponse(order);
    }

    // ======================== Private methods ========================

    private PageResponse<OrderResponse> getAllWithFilterAndSearch(OrderFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), OrderSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<OrderResponse> content = orderMyBatisMapper.findAllWithFilterAndSearch(
                request.getCustomerId(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate(),
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = orderMyBatisMapper.countWithFilterAndSearch(
                request.getCustomerId(),
                request.getStatus(),
                keyword,
                request.getStartDate(),
                request.getEndDate());

        Page<OrderResponse> page = new PageImpl<>(content, pageable, total);
        return PageResponse.fromPage(page);
    }

    private OrderResponse buildFullOrderResponse(Order order) {
        OrderResponse response = orderMapper.toOrderResponse(order);

        paymentRepository.findByOrderIdAndDeletedAtIsNull(order.getId())
                .ifPresent(payment -> {
                    response.setPaymentMethod(payment.getPaymentMethod());
                    response.setPaymentStatus(payment.getPaymentStatus());
                });

        List<OrderDetailResponse> details = orderMyBatisMapper.findDetailsByOrderId(order.getId());
        response.setDetails(details);

        return response;
    }

    private OrderResponse buildOrderResponse(Order order, Payment payment, List<OrderDetail> orderDetails) {
        OrderResponse response = orderMapper.toOrderResponse(order);
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentStatus(payment.getPaymentStatus());

        List<OrderDetailResponse> detailResponses = orderDetails.stream()
                .map(detail -> {
                    var variant = detail.getProductVariant();
                    return OrderDetailResponse.builder()
                            .id(detail.getId())
                            .productVariantId(variant.getId())
                            .productName(variant.getProduct().getName())
                            .productVariantName(variant.getName())
                            .sku(variant.getSku())
                            .image(variant.getImage())
                            .unitPrice(detail.getUnitPrice())
                            .quantity(detail.getQuantity())
                            .subtotal(detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                            .build();
                })
                .toList();

        response.setDetails(detailResponses);
        return response;
    }

    private String generateOrderCode() {
        String prefix = "ORD";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf((int) (Math.random() * 9000) + 1000);
        return prefix + timestamp + random;
    }

}
