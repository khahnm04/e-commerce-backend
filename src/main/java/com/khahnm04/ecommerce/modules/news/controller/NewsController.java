package com.khahnm04.ecommerce.modules.news.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsFilterRequest;
import com.khahnm04.ecommerce.modules.news.dto.response.NewsResponse;
import com.khahnm04.ecommerce.modules.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ApiResponse<List<NewsResponse>> getPublishedNews(
            @Valid @ModelAttribute NewsFilterRequest filter
    ) {
        PageResponse<NewsResponse> pageResponse = newsService.getPublishedNews(filter);
        return ApiResponse.<List<NewsResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get published news successfully")
                .build();
    }

    @GetMapping("/{slug}")
    public ApiResponse<NewsResponse> getNewsBySlug(
            @PathVariable String slug
    ) {
        return ApiResponse.<NewsResponse>builder()
                .data(newsService.getNewsBySlug(slug))
                .message("Get news detail successfully")
                .build();
    }

}
