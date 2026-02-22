package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query("SELECT pc.category.id FROM ProductCategory pc WHERE pc.product.id = :productId")
    Set<Long> findCategoryIdsByProductId(@Param("productId") Long productId);

    @Query("""
                SELECT pc
                FROM ProductCategory pc
                JOIN FETCH pc.category
                WHERE pc.product.id IN :productIds
            """)
    List<ProductCategory> findAllByProductIdInWithCategory(@Param("productIds") List<Long> productIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductCategory pc WHERE pc.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);

    @Modifying
    @Transactional
    @Query("""
                DELETE FROM ProductCategory pc
                WHERE pc.product.id = :productId
                AND pc.category.id NOT IN :categoryIds
            """)
    void deleteByProductIdAndCategoryIdNotIn(
            @Param("productId") Long productId,
            @Param("categoryIds") Set<Long> categoryIds);

}
