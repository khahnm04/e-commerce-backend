package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.dto.request.product.ProductSearchRequest;
import com.khahnm18.ecommercebackend.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductRepository {

    Page<Product> search(ProductSearchRequest request, Pageable pageable);

}

