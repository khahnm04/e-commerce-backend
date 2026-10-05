package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.entity.product.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

    @Modifying
    void deleteByProductIdAndCategoryIdIn(UUID productId, List<UUID> categoryIds);

}
