package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.ProductVariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductVariantValueRepository extends JpaRepository<ProductVariantValue, Long> {

    @Query("""
        SELECT pvv
        FROM ProductVariantValue pvv
        JOIN FETCH pvv.variantValue vv
        JOIN FETCH vv.variant v
        WHERE pvv.productVariant.id = :productVariantId
    """)
    List<ProductVariantValue> findAllByProductVariantIdWithVariantValue(
            @Param("productVariantId") Long productVariantId);

    @Query("""
        SELECT pvv
        FROM ProductVariantValue pvv
        JOIN FETCH pvv.variantValue vv
        JOIN FETCH vv.variant v
        WHERE pvv.productVariant.id IN :productVariantIds
    """)
    List<ProductVariantValue> findAllByProductVariantIdInWithVariantValue(
            @Param("productVariantIds") Set<Long> productVariantIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductVariantValue pvv WHERE pvv.productVariant.id = :productVariantId")
    void deleteAllByProductVariantId(@Param("productVariantId") Long productVariantId);

}
