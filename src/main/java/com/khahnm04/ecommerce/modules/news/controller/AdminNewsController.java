package com.khahnm04.ecommerce.modules.news.controller;

import com.khahnm04.ecommerce.common.dto.ApiResponse;
import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsBulkActionRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsFilterRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.UpdateNewsStatusRequest;
import com.khahnm04.ecommerce.modules.news.dto.response.NewsResponse;
import com.khahnm04.ecommerce.modules.news.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/admin/news")
@RequiredArgsConstructor
public class AdminNewsController {

    private final NewsService newsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<NewsResponse> createNews(
            @Valid @RequestBody NewsRequest request
    ) {
        return ApiResponse.<NewsResponse>builder()
                .data(newsService.createNews(request))
                .message("News created successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<NewsResponse> getNewsDetail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<NewsResponse>builder()
                .data(newsService.getNewsDetail(id, deleted))
                .message("Get news detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<NewsResponse>> getAllNews(
            @Valid @ModelAttribute NewsFilterRequest filter
    ) {
        PageResponse<NewsResponse> pageResponse = newsService.getAllNews(filter);
        return ApiResponse.<List<NewsResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get all news successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<NewsResponse>> getAllDeletedNews(
            @Valid @ModelAttribute NewsFilterRequest filter
    ) {
        PageResponse<NewsResponse> pageResponse = newsService.getAllDeletedNews(filter);
        return ApiResponse.<List<NewsResponse>>builder()
                .data(pageResponse.getData())
                .meta(pageResponse.getMeta())
                .message("Get deleted news successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<NewsResponse> updateNews(
            @PathVariable Long id,
            @Valid @RequestBody NewsRequest request
    ) {
        return ApiResponse.<NewsResponse>builder()
                .data(newsService.updateNews(id, request))
                .message("News updated successfully")
                .build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateNewsStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateNewsStatusRequest request
    ) {
        newsService.updateNewsStatus(id, request);
        return ApiResponse.<Void>builder()
                .message("News status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> softDeleteNews(
            @PathVariable Long id
    ) {
        newsService.softDeleteNews(id);
        return ApiResponse.<Void>builder()
                .message("News soft deleted successfully")
                .build();
    }

    @PatchMapping("/{id}/restore")
    public ApiResponse<Void> restoreNews(
            @PathVariable Long id
    ) {
        newsService.restoreNews(id);
        return ApiResponse.<Void>builder()
                .message("News restored successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> bulkAction(
            @Valid @RequestBody NewsBulkActionRequest request
    ) {
        newsService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
