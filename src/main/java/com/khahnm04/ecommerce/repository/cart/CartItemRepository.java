package com.khahnm04.ecommerce.repository.cart;

import com.khahnm04.ecommerce.entity.cart.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @EntityGraph(attributePaths = { "productVariant", "productVariant.product" })
    List<CartItem> findAllByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);

    @EntityGraph(attributePaths = { "productVariant", "productVariant.product" })
    List<CartItem> findAllByCartIdAndIdIn(Long cartId, Set<Long> ids);

    @Modifying
    @Transactional
    void deleteAllByCartId(Long cartId);

    @Query("SELECT ci.id FROM CartItem ci WHERE ci.id IN :ids AND ci.cart.id = :cartId")
    Set<Long> findIdsByIdInAndCartId(Set<Long> ids, Long cartId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.productVariant.id IN :variantIds")
    void deleteByCartIdAndProductVariantIdIn(Long cartId, List<Long> variantIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.id IN :ids AND ci.cart.id = :cartId")
    void deleteByIdsAndCartId(Set<Long> ids, Long cartId);

}
