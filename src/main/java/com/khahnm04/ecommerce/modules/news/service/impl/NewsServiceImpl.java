package com.khahnm04.ecommerce.modules.news.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.entity.Product;
import com.khahnm04.ecommerce.modules.catalog.repository.ProductRepository;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsBulkActionRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsFilterRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.NewsRequest;
import com.khahnm04.ecommerce.modules.news.dto.request.UpdateNewsStatusRequest;
import com.khahnm04.ecommerce.modules.news.dto.response.NewsProductResponse;
import com.khahnm04.ecommerce.modules.news.dto.response.NewsResponse;
import com.khahnm04.ecommerce.modules.news.entity.News;
import com.khahnm04.ecommerce.modules.news.entity.NewsProduct;
import com.khahnm04.ecommerce.modules.news.enums.NewsSortField;
import com.khahnm04.ecommerce.modules.news.enums.NewsStatus;
import com.khahnm04.ecommerce.modules.news.mapper.NewsMapper;
import com.khahnm04.ecommerce.modules.news.mapper.mybatis.NewsMyBatisMapper;
import com.khahnm04.ecommerce.modules.news.repository.NewsProductRepository;
import com.khahnm04.ecommerce.modules.news.repository.NewsRepository;
import com.khahnm04.ecommerce.modules.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsProductRepository newsProductRepository;
    private final ProductRepository productRepository;
    private final NewsMapper newsMapper;
    private final NewsMyBatisMapper newsMyBatisMapper;

    @Override
    public News getNewsEntityById(Long id) {
        return newsRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
    }

    // ======================== Admin APIs ========================

    @Override
    @Transactional
    public NewsResponse createNews(NewsRequest request) {
        if (newsRepository.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.NEWS_SLUG_EXISTED);
        }

        News news = newsMapper.toNews(request);
        news.setStatus(NewsStatus.DRAFT);
        news.setViewCount(0L);

        News savedNews = newsRepository.save(news);
        saveNewsProducts(savedNews, request.getProductIds());

        log.info("News created: id={}, title={}", savedNews.getId(), savedNews.getTitle());
        return buildNewsResponse(savedNews);
    }

    @Override
    public NewsResponse getNewsDetail(Long id, boolean deleted) {
        News news = deleted ? getDeletedNewsById(id) : getNewsEntityById(id);
        return buildNewsResponse(news);
    }

    @Override
    public PageResponse<NewsResponse> getAllNews(NewsFilterRequest request) {
        return getAllWithFilterAndSearch(request, false, false);
    }

    @Override
    public PageResponse<NewsResponse> getAllDeletedNews(NewsFilterRequest request) {
        return getAllWithFilterAndSearch(request, false, true);
    }

    @Override
    @Transactional
    public NewsResponse updateNews(Long id, NewsRequest request) {
        News news = getNewsEntityById(id);

        if (newsRepository.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new AppException(ErrorCode.NEWS_SLUG_EXISTED);
        }

        newsMapper.updateNews(news, request);
        News savedNews = newsRepository.save(news);

        newsProductRepository.deleteAllByNewsId(id);
        saveNewsProducts(savedNews, request.getProductIds());

        log.info("News updated: id={}, title={}", savedNews.getId(), savedNews.getTitle());
        return buildNewsResponse(savedNews);
    }

    @Override
    @Transactional
    public void updateNewsStatus(Long id, UpdateNewsStatusRequest request) {
        News news = getNewsEntityById(id);
        NewsStatus newStatus = request.getStatus();

        if (newStatus == NewsStatus.PUBLISHED && news.getPublishedAt() == null) {
            news.setPublishedAt(LocalDateTime.now());
        }

        news.setStatus(newStatus);
        newsRepository.save(news);

        log.info("News status updated: id={}, {} -> {}", id, news.getStatus(), newStatus);
    }

    @Override
    @Transactional
    public void softDeleteNews(Long id) {
        News news = getNewsEntityById(id);
        if (news.getDeletedAt() != null) {
            log.warn("News with id {} is already soft deleted", id);
            return;
        }
        news.setDeletedAt(LocalDateTime.now());
        newsRepository.save(news);
        log.info("Soft-deleted news with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreNews(Long id) {
        News news = getDeletedNewsById(id);
        if (news.getDeletedAt() == null) {
            log.warn("News with id {} is not in a deleted state", id);
            return;
        }
        news.setDeletedAt(null);
        newsRepository.save(news);
        log.info("News restored with id: {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(NewsBulkActionRequest request) {
        Set<Long> newsIds = request.getNews();
        if (newsIds == null || newsIds.isEmpty()) {
            throw new AppException(ErrorCode.NEWS_SELECTION_REQUIRED);
        }

        String action = request.getAction().toUpperCase();

        Set<News> existingNews = new HashSet<>(
                action.equals("RESTORE")
                        ? newsRepository.findAllDeletedByIds(newsIds)
                        : newsRepository.findAllById(newsIds));

        Set<Long> existingIds = existingNews.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (newsIds.size() != existingIds.size()) {
            Set<Long> missingIds = new HashSet<>(newsIds);
            missingIds.removeAll(existingIds);
            log.error("Bulk action news failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.NEWS_NOT_FOUND);
        }

        switch (action) {
            case "DELETE" -> {
                newsRepository.softDeleteByIds(LocalDateTime.now(), existingIds);
                log.info("Bulk delete news ids: {}", existingIds);
            }
            case "RESTORE" -> {
                newsRepository.restoreDeletedByIds(existingIds);
                log.info("Bulk restore news ids: {}", existingIds);
            }
            case "PUBLISH" -> {
                newsRepository.bulkUpdateStatus(NewsStatus.PUBLISHED, existingIds);
                log.info("Bulk publish news ids: {}", existingIds);
            }
            case "HIDE" -> {
                newsRepository.bulkUpdateStatus(NewsStatus.HIDDEN, existingIds);
                log.info("Bulk hide news ids: {}", existingIds);
            }
            default -> throw new AppException(ErrorCode.NEWS_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} news", action, existingIds.size());
    }

    // ======================== Public APIs ========================

    @Override
    public PageResponse<NewsResponse> getPublishedNews(NewsFilterRequest request) {
        return getAllWithFilterAndSearch(request, true, false);
    }

    @Override
    @Transactional
    public NewsResponse getNewsBySlug(String slug) {
        News news = newsRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));

        if (news.getStatus() != NewsStatus.PUBLISHED) {
            throw new AppException(ErrorCode.NEWS_NOT_FOUND);
        }

        newsRepository.incrementViewCount(news.getId());

        NewsResponse response = buildNewsResponse(news);
        response.setViewCount(news.getViewCount() + 1);
        return response;
    }

    // ======================== Private methods ========================

    private News getDeletedNewsById(Long id) {
        return newsRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
    }

    private PageResponse<NewsResponse> getAllWithFilterAndSearch(
            NewsFilterRequest request, boolean publishedOnly, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), NewsSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<NewsResponse> content = newsMyBatisMapper.findAllWithFilterAndSearch(
                request.getStatus(),
                keyword,
                request.getIsFeatured(),
                publishedOnly,
                isDeleted,
                request.getStartDate(),
                request.getEndDate(),
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = newsMyBatisMapper.countWithFilterAndSearch(
                request.getStatus(),
                keyword,
                request.getIsFeatured(),
                publishedOnly,
                isDeleted,
                request.getStartDate(),
                request.getEndDate());

        Page<NewsResponse> page = new PageImpl<>(content, pageable, total);
        return PageResponse.fromPage(page);
    }

    private NewsResponse buildNewsResponse(News news) {
        NewsResponse response = newsMapper.toNewsResponse(news);
        List<NewsProductResponse> products = newsMyBatisMapper.findProductsByNewsId(news.getId());
        response.setProducts(products);
        return response;
    }

    private void saveNewsProducts(News news, Set<Long> productIds) {
        if (CollectionUtils.isEmpty(productIds)) {
            return;
        }
        List<Product> products = productRepository.findAllById(productIds);
        List<NewsProduct> newsProducts = products.stream()
                .map(product -> NewsProduct.builder()
                        .news(news)
                        .product(product)
                        .build())
                .toList();
        newsProductRepository.saveAll(newsProducts);
    }

}
