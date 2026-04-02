package com.khahnm04.ecommerce.service.cart.impl;

import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import com.khahnm04.ecommerce.dto.request.cart.AddCartItemRequest;
import com.khahnm04.ecommerce.dto.request.cart.UpdateCartItemRequest;
import com.khahnm04.ecommerce.dto.response.cart.CartItemResponse;
import com.khahnm04.ecommerce.dto.response.cart.CartResponse;
import com.khahnm04.ecommerce.entity.cart.Cart;
import com.khahnm04.ecommerce.entity.cart.CartItem;
import com.khahnm04.ecommerce.entity.product.ProductVariant;
import com.khahnm04.ecommerce.entity.user.Customer;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.repository.cart.CartItemRepository;
import com.khahnm04.ecommerce.repository.cart.CartRepository;
import com.khahnm04.ecommerce.repository.product.ProductVariantRepository;
import com.khahnm04.ecommerce.repository.user.CustomerRepository;
import com.khahnm04.ecommerce.service.cart.CartService;
import com.khahnm04.ecommerce.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CustomerRepository customerRepository;
    private final InventoryService inventoryService;

    @Override
    public CartResponse getCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .map(this::buildCartResponse)
                .orElse(buildEmptyCartResponse());
    }

    @Override
    @Transactional
    public CartResponse addItem(Long customerId, AddCartItemRequest request) {
        Cart cart = getOrCreateCart(customerId);

        ProductVariant productVariant = productVariantRepository
                .findByIdAndDeletedAtIsNull(request.productVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

        if (productVariant.getStatus() != ProductVariantStatus.ACTIVE) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        }

        // Check inventory availability
        inventoryService.checkAvailability(request.productVariantId(), request.quantity());

        // If product variant already in cart, increase quantity
        Optional<CartItem> existingItem = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), productVariant.getId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
            cartItemRepository.save(item);
            log.info("Updated cart item quantity: cartId={}, variantId={}, newQty={}", cart.getId(),
                    productVariant.getId(), item.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(productVariant)
                    .quantity(request.quantity())
                    .build();
            cartItemRepository.save(newItem);
            log.info("Added new cart item: cartId={}, variantId={}, qty={}", cart.getId(), productVariant.getId(),
                    request.quantity());
        }

        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateItem(Long customerId, Long itemId, UpdateCartItemRequest request) {
        Cart cart = getCartByCustomerId(customerId);

        CartItem cartItem = cartItemRepository.findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        // Check inventory availability
        inventoryService.checkAvailability(cartItem.getProductVariant().getId(), request.quantity());

        cartItem.setQuantity(request.quantity());
        cartItemRepository.save(cartItem);

        log.info("Updated cart item: cartId={}, itemId={}, newQty={}", cart.getId(), itemId, request.quantity());
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public void removeItem(Long customerId, Long itemId) {
        Cart cart = getCartByCustomerId(customerId);

        CartItem cartItem = cartItemRepository.findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        cartItemRepository.delete(cartItem);
        log.info("Removed cart item: cartId={}, itemId={}", cart.getId(), itemId);
    }

    @Override
    @Transactional
    public void removeItems(Long customerId, Set<Long> itemIds) {
        Cart cart = getCartByCustomerId(customerId);

        Set<Long> existingIds = cartItemRepository.findIdsByIdInAndCartId(itemIds, cart.getId());
        if (existingIds.size() != itemIds.size()) {
            Set<Long> notFoundIds = new HashSet<>(itemIds);
            notFoundIds.removeAll(existingIds);
            log.warn("Cart items not found: cartId={}, notFoundIds={}", cart.getId(), notFoundIds);
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        cartItemRepository.deleteByIdsAndCartId(itemIds, cart.getId());
        log.info("Removed cart items: cartId={}, itemIds={}", cart.getId(), itemIds);
    }

    @Override
    @Transactional
    public void clearCart(Long customerId) {
        Cart cart = getCartByCustomerId(customerId);

        List<CartItem> items = cartItemRepository.findAllByCartId(cart.getId());
        if (items.isEmpty()) {
            log.warn("Cart is already empty: cartId={}", cart.getId());
            return;
        }

        cartItemRepository.deleteAllByCartId(cart.getId());
        log.info("Cleared cart: cartId={}", cart.getId());
    }

    // ======================== Private methods ========================

    private Cart getCartByCustomerId(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    }

    private Cart getOrCreateCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Customer customer = customerRepository.findByIdAndDeletedAtIsNull(customerId)
                            .orElseThrow(() -> new AppException(
                                    ErrorCode.CUSTOMER_NOT_FOUND));
                    Cart newCart = Cart.builder()
                            .customer(customer)
                            .build();
                    Cart saved = cartRepository.save(newCart);
                    log.info("Created new cart for customer: {}", customerId);
                    return saved;
                });
    }

    private CartResponse buildEmptyCartResponse() {
        return CartResponse.builder()
                .items(Collections.emptyList())
                .totalItems(0)
                .totalAmount(BigDecimal.ZERO)
                .build();
    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItem> items = cartItemRepository.findAllByCartId(cart.getId());

        List<CartItemResponse> itemResponses = items.stream()
                .map(this::toCartItemResponse)
                .toList();

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::quantity)
                .sum();

        return CartResponse.builder()
                .id(cart.getId())
                .items(itemResponses)
                .totalItems(totalItems)
                .totalAmount(totalAmount)
                .build();
    }

    private CartItemResponse toCartItemResponse(CartItem cartItem) {
        ProductVariant productVariant = cartItem.getProductVariant();
        BigDecimal subtotal = productVariant.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemResponse.builder()
                .id(cartItem.getId())
                .productVariantId(productVariant.getId())
                .productName(productVariant.getProduct().getName())
                .productVariantName(productVariant.getName())
                .sku(productVariant.getSku())
                .image(productVariant.getImage())
                .price(productVariant.getPrice())
                .oldPrice(productVariant.getOldPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(subtotal)
                .build();
    }

}
