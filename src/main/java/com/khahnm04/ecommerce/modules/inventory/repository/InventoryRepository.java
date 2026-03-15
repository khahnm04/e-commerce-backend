package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductVariantId(Long productVariantId);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.quantity = i.quantity + :qty WHERE i.productVariant.id = :variantId")
    void addQuantity(@Param("variantId") Long variantId, @Param("qty") Integer qty);

    java.util.List<Inventory> findAllByProductVariantIdIn(java.util.Collection<Long> productVariantIds);

}
