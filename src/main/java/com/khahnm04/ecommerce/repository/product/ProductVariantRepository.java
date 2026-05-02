package com.khahnm04.ecommerce.repository.product;

import com.khahnm04.ecommerce.common.enums.ProductVariantStatus;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.entity.ProductVariant;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {

    boolean existsByProductId(Long productId);

    boolean existsByProductIdIn(Set<Long> productIds);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    List<ProductVariant> findAllByProductIdAndDeletedAtIsNull(Long productId);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.deletedAt = :now, pv.deletedBy = :employee WHERE pv.product.id = :productId AND pv.id NOT IN :ids AND pv.deletedAt IS NULL")
    void softDeleteByProductIdAndIdNotIn(@Param("productId") Long productId, @Param("ids") Set<Long> ids, @Param("now") LocalDateTime now, @Param("employee") Employee employee);

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
    @Query("UPDATE ProductVariant pv SET pv.deletedAt = :now, pv.deletedBy = :employee WHERE pv.id IN :ids AND pv.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("employee") Employee employee, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.deletedAt = NULL, pv.deletedBy = NULL WHERE pv.id IN :ids AND pv.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE ProductVariant pv SET pv.status = :status WHERE pv.id IN :ids")
    void updateStatusByIds(ProductVariantStatus status, Set<Long> ids);

}
