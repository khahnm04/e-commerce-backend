package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.entity.ProductVariant;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {

    boolean existsByProductId(Long productId);

    boolean existsByProductIdIn(Set<Long> productIds);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    @EntityGraph(attributePaths = { "product" })
    Optional<ProductVariant> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = { "product" })
    Optional<ProductVariant> findByIdAndDeletedAtIsNotNull(Long id);

    @EntityGraph(attributePaths = { "product" })
    Optional<ProductVariant> findBySkuAndDeletedAtIsNull(String sku);

    @EntityGraph(attributePaths = { "product" })
    Optional<ProductVariant> findBySkuAndDeletedAtIsNotNull(String sku);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.id IN :ids AND pv.deletedAt IS NOT NULL")
    Set<ProductVariant> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.deletedAt = :now, pv.deletedBy = :user WHERE pv.id IN :ids AND pv.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("user") User user, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.deletedAt = NULL, pv.deletedBy = NULL WHERE pv.id IN :ids AND pv.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.status = :status WHERE pv.id IN :ids")
    void updateStatusByIds(ProductVariantStatus status, Set<Long> ids);

}
