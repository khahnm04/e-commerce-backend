package com.khahnm04.ecommerce.service.cart;

import com.khahnm04.ecommerce.dto.request.cart.AddCartItemRequest;
import com.khahnm04.ecommerce.dto.request.cart.UpdateCartItemRequest;
import com.khahnm04.ecommerce.dto.response.cart.CartResponse;
import java.util.Set;

public interface CartService {

    CartResponse getCart(Long customerId);

    CartResponse addItem(Long customerId, AddCartItemRequest request);

    CartResponse updateItem(Long customerId, Long itemId, UpdateCartItemRequest request);

    void removeItem(Long customerId, Long itemId);

    void removeItems(Long customerId, Set<Long> itemIds);

    void clearCart(Long customerId);

}
