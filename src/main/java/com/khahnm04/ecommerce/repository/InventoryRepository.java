package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductVariantId(Long productVariantId);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.quantity = i.quantity + :qty WHERE i.productVariant.id = :variantId")
    void addQuantity(@Param("variantId") Long variantId, @Param("qty") Integer qty);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.quantity = i.quantity - :qty WHERE i.productVariant.id = :variantId AND i.quantity >= :qty")
    int decreaseQuantity(@Param("variantId") Long variantId, @Param("qty") Integer qty);

    List<Inventory> findAllByProductVariantIdIn(Collection<Long> productVariantIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM Inventory i WHERE i.productVariant.id = :productVariantId")
    void deleteByProductVariantId(@Param("productVariantId") Long productVariantId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Inventory i WHERE i.productVariant.id IN :productVariantIds")
    void deleteByProductVariantIdIn(@Param("productVariantIds") Collection<Long> productVariantIds);

}
