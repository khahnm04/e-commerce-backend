package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.ProductVariant;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    @EntityGraph(attributePaths = { "product" })
    Optional<ProductVariant> findById(Long id);

    @EntityGraph(attributePaths = { "product" })
    Optional<ProductVariant> findBySku(String sku);

    @Query(value = "SELECT * FROM product_variants WHERE id = :id", nativeQuery = true)
    Optional<ProductVariant> findDeletedProductVariantById(@Param("id") Long id);

    @Query(value = "SELECT * FROM product_variants WHERE id IN :ids", nativeQuery = true)
    Set<ProductVariant> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.deletedAt = :now WHERE pv.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE product_variants SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
