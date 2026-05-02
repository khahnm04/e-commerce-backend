package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.dto.request.filter.ProductFilterRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.ProductResponse;
import com.khahnm04.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts(
        @Valid @ModelAttribute ProductFilterRequest filter
    ) {
        PageResponse<ProductResponse> pageResponse = productService.getAllProducts(filter);
        return ApiResponse.<List<ProductResponse>>builder()
            .data(pageResponse.data())
            .meta(pageResponse.meta())
            .message("Get all products successfully")
            .build();
    }

    @GetMapping("/{identifier}")
    public ApiResponse<ProductResponse> getProductDetail(
        @PathVariable String identifier,
        @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<ProductResponse>builder()
            .data(productService.getProductDetail(identifier, deleted))
            .message("Get product detail successfully")
            .build();
    }

}
