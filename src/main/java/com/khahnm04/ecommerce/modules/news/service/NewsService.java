package com.khahnm04.ecommerce.modules.news.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsBulkActionRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsFilterRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.UpdateNewsStatusRequest;
import com.khahnm04.ecommerce.modules.news.dto.response.NewsResponse;
import com.khahnm04.ecommerce.modules.news.entity.News;

public interface NewsService {

    News getNewsEntityById(Long id);

    // Admin APIs
    NewsResponse createNews(NewsRequest request);

    NewsResponse getNewsDetail(Long id, boolean deleted);

    PageResponse<NewsResponse> getAllNews(NewsFilterRequest request);

    PageResponse<NewsResponse> getAllDeletedNews(NewsFilterRequest request);

    NewsResponse updateNews(Long id, NewsRequest request);

    void updateNewsStatus(Long id, UpdateNewsStatusRequest request);

    void softDeleteNews(Long id);

    void restoreNews(Long id);

    void handleBulkAction(NewsBulkActionRequest request);

    // Public APIs
    PageResponse<NewsResponse> getPublishedNews(NewsFilterRequest request);

    NewsResponse getNewsBySlug(String slug);

}
