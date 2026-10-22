package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.dto.request.product.ProductVersionSearchRequest;
import com.khahnm18.ecommercebackend.entity.product.ProductVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductVersionRepository {

    Page<ProductVersion> search(ProductVersionSearchRequest request, Pageable pageable);

}

