package com.khahnm04.ecommerce.repository.product;

import com.khahnm04.ecommerce.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, Long> {

    List<ProductTag> findAllByProductId(Long productId);
    
    @Modifying
    void deleteAllByProductId(Long productId);

    @Modifying
    @Query("DELETE FROM ProductTag pt WHERE pt.product.id = :productId AND pt.tag.id NOT IN :tagIds")
    void deleteByProductIdAndTagIdNotIn(Long productId, Collection<Long> tagIds);
    
    @Query("SELECT pt.tag.id FROM ProductTag pt WHERE pt.product.id = :productId")
    List<Long> findTagIdsByProductId(Long productId);

}
