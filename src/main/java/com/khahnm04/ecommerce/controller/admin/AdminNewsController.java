package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.NewsFilterRequest;
import com.khahnm04.ecommerce.dto.request.NewsRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.NewsResponse;
import com.khahnm04.ecommerce.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
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

    @GetMapping("/{identifier}")
    public ApiResponse<NewsResponse> getNewsDetail(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ApiResponse.<NewsResponse>builder()
                .data(newsService.getNewsDetail(identifier, deleted))
                .message("Get news detail successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<NewsResponse>> getAllNews(
            @Valid @ModelAttribute NewsFilterRequest filter
    ) {
        PageResponse<NewsResponse> pageResponse = newsService.getAllNews(filter);
        return ApiResponse.<List<NewsResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all news successfully")
                .build();
    }

    @GetMapping("/deleted")
    public ApiResponse<List<NewsResponse>> getAllDeletedNews(
            @Valid @ModelAttribute NewsFilterRequest filter
    ) {
        PageResponse<NewsResponse> pageResponse = newsService.getAllDeletedNews(filter);
        return ApiResponse.<List<NewsResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
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

    @PatchMapping("/{id}/soft-delete")
    public ApiResponse<Void> softDeleteNews(
            @PathVariable Long id
    ) {
        newsService.softDeleteNews(id);
        return ApiResponse.<Void>builder()
                .message("News soft deleted successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNews(
            @PathVariable Long id
    ) {
        newsService.deleteNews(id);
        return ApiResponse.<Void>builder()
                .message("News deleted successfully")
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
    public ApiResponse<Void> handleBulkAction(
            @RequestBody BulkActionRequest request
    ) {
        newsService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
