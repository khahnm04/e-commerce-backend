package com.khahnm18.ecommercebackend.service.product;

import com.khahnm18.ecommercebackend.entity.category.Category;
import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import java.util.List;
import java.util.UUID;

public interface ProductGroupCategoryService {

    List<Category> assignCategoriesToProductGroup(ProductGroup productGroup, List<UUID> categoryIds);

    List<Category> replaceCategoriesForProductGroup(ProductGroup productGroup, List<UUID> categoryIds);

}

