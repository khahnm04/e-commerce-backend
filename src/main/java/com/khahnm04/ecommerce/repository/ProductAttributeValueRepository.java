package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long> {

    List<ProductAttributeValue> findAllByProductId(Long productId);

    @Query("SELECT pav FROM ProductAttributeValue pav JOIN FETCH pav.attribute WHERE pav.product.id IN :productIds")
    List<ProductAttributeValue> findAllByProductIdInWithAttribute(@Param("productIds") List<Long> productIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductAttributeValue pav WHERE pav.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductAttributeValue pav WHERE pav.product.id = :productId AND pav.id NOT IN :ids")
    void deleteByProductIdAndIdNotIn(@Param("productId") Long productId, @Param("ids") Set<Long> ids);

}
