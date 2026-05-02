package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.dto.request.AddCartItemRequest;
import com.khahnm04.ecommerce.dto.request.RemoveCartItemsRequest;
import com.khahnm04.ecommerce.dto.request.UpdateCartItemRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.CartResponse;
import com.khahnm04.ecommerce.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.khahnm04.ecommerce.common.util.SecurityUtils;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ApiResponse<CartResponse> getCart() {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        return ApiResponse.<CartResponse>builder()
                .data(cartService.getCart(customerId))
                .message("Get cart successfully")
                .build();
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CartResponse> addItem(
            @Valid @RequestBody AddCartItemRequest request
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        return ApiResponse.<CartResponse>builder()
                .data(cartService.addItem(customerId, request))
                .message("Item added to cart successfully")
                .build();
    }

    @PutMapping("/items/{itemId}")
    public ApiResponse<CartResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        return ApiResponse.<CartResponse>builder()
                .data(cartService.updateItem(customerId, itemId, request))
                .message("Cart item updated successfully")
                .build();
    }

    @DeleteMapping("/items/{itemId}")
    public ApiResponse<Void> removeItem(
            @PathVariable Long itemId
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        cartService.removeItem(customerId, itemId);
        return ApiResponse.<Void>builder()
                .message("Cart item removed successfully")
                .build();
    }

    @DeleteMapping("/items")
    public ApiResponse<Void> removeItems(
            @Valid @RequestBody RemoveCartItemsRequest request
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        cartService.removeItems(customerId, request.itemIds());
        return ApiResponse.<Void>builder()
                .message("Cart items removed successfully")
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> clearCart() {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        cartService.clearCart(customerId);
        return ApiResponse.<Void>builder()
                .message("Cart cleared successfully")
                .build();
    }

}
