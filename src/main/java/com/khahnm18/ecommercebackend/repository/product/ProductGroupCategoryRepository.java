package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.entity.product.ProductGroupCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductGroupCategoryRepository extends JpaRepository<ProductGroupCategory, UUID> {

    @Modifying
    void deleteByGroupIdAndCategoryIdIn(UUID groupId, List<UUID> categoryIds);

}
