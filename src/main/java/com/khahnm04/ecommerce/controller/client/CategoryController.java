package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.dto.request.filter.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories(
        @Valid @ModelAttribute CategoryFilterRequest filter
    ) {
        PageResponse<CategoryResponse> pageResponse = categoryService.getAllCategories(filter);
        return ApiResponse.<List<CategoryResponse>>builder()
            .data(pageResponse.data())
            .meta(pageResponse.meta())
            .message("Get all categories successfully")
            .build();
    }

}
