package com.khahnm04.ecommerce.modules.cart.service;

import com.khahnm04.ecommerce.modules.cart.dto.request.AddCartItemRequest;
import com.khahnm04.ecommerce.modules.cart.dto.request.UpdateCartItemRequest;
import com.khahnm04.ecommerce.modules.cart.dto.response.CartResponse;
import java.util.Set;

public interface CartService {

    CartResponse getCart(Long customerId);

    CartResponse addItem(Long customerId, AddCartItemRequest request);

    CartResponse updateItem(Long customerId, Long itemId, UpdateCartItemRequest request);

    void removeItem(Long customerId, Long itemId);

    void removeItems(Long customerId, Set<Long> itemIds);

    void clearCart(Long customerId);

}
