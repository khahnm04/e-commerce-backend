package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.NewsFilterRequest;
import com.khahnm04.ecommerce.dto.request.NewsRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.NewsResponse;
import com.khahnm04.ecommerce.entity.News;

public interface NewsService {

    NewsResponse createNews(NewsRequest request);

    News getNewsById(Long id);

    NewsResponse getNewsDetail(String identifier, boolean deleted);

    PageResponse<NewsResponse> getAllNews(NewsFilterRequest filter);

    PageResponse<NewsResponse> getAllDeletedNews(NewsFilterRequest filter);

    NewsResponse updateNews(Long id, NewsRequest request);

    void softDeleteNews(Long id);

    void deleteNews(Long id);

    void restoreNews(Long id);

    void handleBulkAction(BulkActionRequest request);

}
