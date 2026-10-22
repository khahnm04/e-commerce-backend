package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupSearchRequest;
import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductGroupRepository {

    Page<ProductGroup> search(ProductGroupSearchRequest request, Pageable pageable);

}

