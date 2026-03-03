package com.khahnm04.ecommerce.modules.cart.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.modules.cart.dto.request.AddCartItemRequest;
import com.khahnm04.ecommerce.modules.cart.dto.request.ApplyCouponRequest;
import com.khahnm04.ecommerce.modules.cart.dto.request.UpdateCartItemRequest;
import com.khahnm04.ecommerce.modules.cart.dto.response.CartResponse;
import com.khahnm04.ecommerce.modules.cart.dto.response.CartSummaryResponse;
import com.khahnm04.ecommerce.modules.cart.dto.response.CartValidationResponse;
import com.khahnm04.ecommerce.modules.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 1. Lấy giỏ hàng của tôi
    @GetMapping
    public ApiResponse<CartResponse> getMyCart() {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.getMyCart())
                .message("Get cart successfully")
                .build();
    }

    // 2. Thêm sản phẩm vào giỏ
    @PostMapping("/items")
    public ApiResponse<CartResponse> addItemToCart(
            @Valid @RequestBody AddCartItemRequest request) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.addItemToCart(request))
                .message("Item added to cart successfully")
                .build();
    }

    // 3. Cập nhật số lượng sản phẩm
    @PutMapping("/items/{itemId}")
    public ApiResponse<CartResponse> updateCartItem(
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.updateCartItem(itemId, request))
                .message("Cart item updated successfully")
                .build();
    }

    // 4. Xóa sản phẩm khỏi giỏ
    @DeleteMapping("/items/{itemId}")
    public ApiResponse<Void> removeCartItem(
            @PathVariable Long itemId) {
        cartService.removeCartItem(itemId);
        return ApiResponse.<Void>builder()
                .message("Cart item removed successfully")
                .build();
    }

    // 5. Xóa toàn bộ giỏ hàng
    @DeleteMapping
    public ApiResponse<Void> clearCart() {
        cartService.clearCart();
        return ApiResponse.<Void>builder()
                .message("Cart cleared successfully")
                .build();
    }

    // 6. Áp dụng mã giảm giá
    @PostMapping("/coupon")
    public ApiResponse<CartResponse> applyCoupon(
            @Valid @RequestBody ApplyCouponRequest request) {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.applyCoupon(request))
                .message("Coupon applied successfully")
                .build();
    }

    // 7. Hủy mã giảm giá
    @DeleteMapping("/coupon")
    public ApiResponse<CartResponse> removeCoupon() {
        return ApiResponse.<CartResponse>builder()
                .data(cartService.removeCoupon())
                .message("Coupon removed successfully")
                .build();
    }

    // 8. Tính tổng giỏ hàng
    @GetMapping("/summary")
    public ApiResponse<CartSummaryResponse> getCartSummary() {
        return ApiResponse.<CartSummaryResponse>builder()
                .data(cartService.getCartSummary())
                .message("Get cart summary successfully")
                .build();
    }

    // 9. Kiểm tra tồn kho trước khi checkout
    @PostMapping("/validate")
    public ApiResponse<List<CartValidationResponse>> validateCart() {
        return ApiResponse.<List<CartValidationResponse>>builder()
                .data(cartService.validateCart())
                .message("Cart validated successfully")
                .build();
    }

    // 10. Chuyển giỏ hàng sang đơn hàng - TODO: sẽ implement khi có Order module
    @PostMapping("/checkout")
    public ApiResponse<Void> checkout() {
        return ApiResponse.<Void>builder()
                .message("Checkout - coming soon")
                .build();
    }

    // 11. Lấy số lượng items trong giỏ
    @GetMapping("/count")
    public ApiResponse<Integer> getCartItemCount() {
        return ApiResponse.<Integer>builder()
                .data(cartService.getCartItemCount())
                .message("Get cart item count successfully")
                .build();
    }

}
