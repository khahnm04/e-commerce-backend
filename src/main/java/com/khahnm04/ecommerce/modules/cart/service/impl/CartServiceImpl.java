package com.khahnm04.ecommerce.modules.cart.service.impl;

import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SecurityUtils;
import com.khahnm04.ecommerce.modules.cart.dto.request.AddCartItemRequest;
import com.khahnm04.ecommerce.modules.cart.dto.request.ApplyCouponRequest;
import com.khahnm04.ecommerce.modules.cart.dto.request.UpdateCartItemRequest;
import com.khahnm04.ecommerce.modules.cart.dto.response.*;
import com.khahnm04.ecommerce.modules.cart.entity.Cart;
import com.khahnm04.ecommerce.modules.cart.entity.CartItem;
import com.khahnm04.ecommerce.modules.cart.repository.CartItemRepository;
import com.khahnm04.ecommerce.modules.cart.repository.CartRepository;
import com.khahnm04.ecommerce.modules.cart.service.CartService;
import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import com.khahnm04.ecommerce.modules.catalog.repository.ProductVariantRepository;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import com.khahnm04.ecommerce.modules.inventory.repository.SerialNumberRepository;
import com.khahnm04.ecommerce.modules.user.entity.Customer;
import com.khahnm04.ecommerce.modules.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductVariantRepository productVariantRepository;
    private final SerialNumberRepository serialNumberRepository;

    @Override
    public CartResponse getMyCart() {
        Cart cart = getOrCreateCart();
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItemToCart(AddCartItemRequest request) {
        Cart cart = getOrCreateCart();

        ProductVariant variant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

        // If item already exists, increase quantity
        Optional<CartItem> existingItem = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), variant.getId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
            log.info("Increased cart item quantity: cartId={}, variantId={}, newQty={}",
                    cart.getId(), variant.getId(), item.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant)
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(newItem);
            log.info("Added new cart item: cartId={}, variantId={}", cart.getId(), variant.getId());
        }

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(Long itemId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCart();
        CartItem item = getCartItemBelongingToCart(itemId, cart);

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        log.info("Updated cart item quantity: itemId={}, newQty={}", itemId, request.getQuantity());

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public void removeCartItem(Long itemId) {
        Cart cart = getOrCreateCart();
        CartItem item = getCartItemBelongingToCart(itemId, cart);

        cartItemRepository.delete(item);
        log.info("Removed cart item: itemId={}", itemId);
    }

    @Override
    @Transactional
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setCouponCode(null);
        cartRepository.save(cart);
        log.info("Cleared cart: cartId={}", cart.getId());
    }

    @Override
    @Transactional
    public CartResponse applyCoupon(ApplyCouponRequest request) {
        Cart cart = getOrCreateCart();
        // TODO: Validate coupon code when Coupon module is ready
        cart.setCouponCode(request.getCouponCode());
        cartRepository.save(cart);
        log.info("Applied coupon '{}' to cart: cartId={}", request.getCouponCode(), cart.getId());

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeCoupon() {
        Cart cart = getOrCreateCart();
        cart.setCouponCode(null);
        cartRepository.save(cart);
        log.info("Removed coupon from cart: cartId={}", cart.getId());

        return buildCartResponse(cart);
    }

    @Override
    public CartSummaryResponse getCartSummary() {
        Cart cart = getOrCreateCart();
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        return buildSummary(items, cart.getCouponCode());
    }

    @Override
    public List<CartValidationResponse> validateCart() {
        Cart cart = getOrCreateCart();
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        List<CartValidationResponse> results = new ArrayList<>();

        for (CartItem item : items) {
            ProductVariant variant = item.getProductVariant();

            // Count available serials for this variant (IN_STOCK)
            long availableCount = serialNumberRepository
                    .findByStatusAndProductVariantId(SerialNumberStatus.IN_STOCK, variant.getId())
                    .size();

            if (availableCount < item.getQuantity()) {
                results.add(CartValidationResponse.builder()
                        .valid(false)
                        .productVariantId(variant.getId())
                        .productVariantName(variant.getName())
                        .message(
                                "Only " + availableCount + " units available, but " + item.getQuantity() + " requested")
                        .build());
            } else {
                results.add(CartValidationResponse.builder()
                        .valid(true)
                        .productVariantId(variant.getId())
                        .productVariantName(variant.getName())
                        .message("In stock")
                        .build());
            }
        }

        return results;
    }

    @Override
    public int getCartItemCount() {
        Cart cart = getOrCreateCart();
        return cartItemRepository.countTotalItemsByCartId(cart.getId());
    }

    // ======================== Private methods ========================

    private Customer getCurrentCustomer() {
        String principal = SecurityUtils.extractPrincipal();
        if (principal == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return customerRepository.findByUser_PhoneNumber(principal)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    private Cart getOrCreateCart() {
        Customer customer = getCurrentCustomer();
        return cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .customer(customer)
                            .build();
                    Cart saved = cartRepository.save(newCart);
                    log.info("Created new cart for customer: {}", customer.getId());
                    return saved;
                });
    }

    private CartItem getCartItemBelongingToCart(Long itemId, Cart cart) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        return item;
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        List<CartItemResponse> itemResponses = items.stream()
                .map(this::toCartItemResponse)
                .toList();

        CartSummaryResponse summary = buildSummary(items, cart.getCouponCode());

        return CartResponse.builder()
                .id(cart.getId())
                .couponCode(cart.getCouponCode())
                .items(itemResponses)
                .summary(summary)
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        ProductVariant variant = item.getProductVariant();
        BigDecimal subtotal = variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemResponse.builder()
                .id(item.getId())
                .productVariantId(variant.getId())
                .productVariantSku(variant.getSku())
                .productVariantName(variant.getName())
                .productVariantImage(variant.getImage())
                .price(variant.getPrice())
                .oldPrice(variant.getOldPrice())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    private CartSummaryResponse buildSummary(List<CartItem> items, String couponCode) {
        BigDecimal subtotal = items.stream()
                .map(item -> item.getProductVariant().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // TODO: Calculate discount from coupon when Coupon module is ready
        BigDecimal discount = BigDecimal.ZERO;
        // TODO: Calculate shipping when Shipping module is ready
        BigDecimal shipping = BigDecimal.ZERO;

        BigDecimal total = subtotal.subtract(discount).add(shipping);

        int totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();

        return CartSummaryResponse.builder()
                .subtotal(subtotal)
                .discount(discount)
                .shipping(shipping)
                .total(total)
                .couponCode(couponCode)
                .totalItems(totalItems)
                .build();
    }

}
