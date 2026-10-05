package com.khahnm18.ecommercebackend.service.product.impl;

import com.khahnm18.ecommercebackend.dto.request.product.ProductRequest;
import com.khahnm18.ecommercebackend.entity.category.Category;
import com.khahnm18.ecommercebackend.entity.product.Product;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.ProductMapper;
import com.khahnm18.ecommercebackend.repository.product.ProductRepository;
import com.khahnm18.ecommercebackend.service.product.ProductCategoryService;
import com.khahnm18.ecommercebackend.service.product.ProductService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j(topic = "PRODUCT-OPTION-SERVICE")
@Service
@RequiredArgsConstructor
public class ProductOptionServiceImpl {



}
