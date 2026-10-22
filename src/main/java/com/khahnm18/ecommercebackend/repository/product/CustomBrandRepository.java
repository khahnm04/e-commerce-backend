package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.dto.request.product.BrandSearchRequest;
import com.khahnm18.ecommercebackend.entity.product.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBrandRepository {

    Page<Brand> search(BrandSearchRequest request, Pageable pageable);

}

