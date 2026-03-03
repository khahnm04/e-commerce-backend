package com.khahnm04.ecommerce.modules.cart.repository;

import com.khahnm04.ecommerce.modules.cart.entity.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @EntityGraph(attributePaths = { "productVariant" })
    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    void deleteByCartId(Long cartId);

    @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItem ci WHERE ci.cart.id = :cartId")
    int countTotalItemsByCartId(@Param("cartId") Long cartId);

}
