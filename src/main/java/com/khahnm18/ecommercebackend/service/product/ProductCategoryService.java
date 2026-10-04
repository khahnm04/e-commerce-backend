package com.khahnm18.ecommercebackend.service.product;

import com.khahnm18.ecommercebackend.entity.category.Category;
import com.khahnm18.ecommercebackend.entity.product.Product;
import java.util.List;
import java.util.UUID;

public interface ProductCategoryService {

    List<Category> assignPermissionsToRole(Product product, List<UUID> catgeoryIds);

}
