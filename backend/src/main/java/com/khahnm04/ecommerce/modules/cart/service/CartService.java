package com.khahnm04.ecommerce.modules.cart.service;

import com.khahnm04.ecommerce.modules.cart.dto.request.AddCartItemRequest;
import com.khahnm04.ecommerce.modules.cart.dto.request.ApplyCouponRequest;
import com.khahnm04.ecommerce.modules.cart.dto.request.UpdateCartItemRequest;
import com.khahnm04.ecommerce.modules.cart.dto.response.CartResponse;
import com.khahnm04.ecommerce.modules.cart.dto.response.CartSummaryResponse;
import com.khahnm04.ecommerce.modules.cart.dto.response.CartValidationResponse;
import java.util.List;

public interface CartService {

    CartResponse getMyCart();

    CartResponse addItemToCart(AddCartItemRequest request);

    CartResponse updateCartItem(Long itemId, UpdateCartItemRequest request);

    void removeCartItem(Long itemId);

    void clearCart();

    CartResponse applyCoupon(ApplyCouponRequest request);

    CartResponse removeCoupon();

    CartSummaryResponse getCartSummary();

    List<CartValidationResponse> validateCart();

    int getCartItemCount();

}
