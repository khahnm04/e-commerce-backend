package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.NewsStatus;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.projection.NewsProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.NewsFilterRequest;
import com.khahnm04.ecommerce.dto.request.NewsRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.NewsResponse;
import com.khahnm04.ecommerce.entity.News;
import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.NewsMapper;
import com.khahnm04.ecommerce.repository.NewsRepository;
import com.khahnm04.ecommerce.service.NewsService;
import com.khahnm04.ecommerce.specification.NewsSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final AuditorAwareConfig auditorAwareConfig;

    @Override
    @Transactional
    public NewsResponse createNews(NewsRequest request) {
        if (newsRepository.existsByTitle(request.title())) {
            throw new AppException(ErrorCode.NEWS_SLUG_EXISTED);
        }

        String slug = StringUtils.hasText(request.slug())
                ? request.slug()
                : SlugUtils.toSlug(request.title());

        if (newsRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.NEWS_SLUG_EXISTED);
        }

        News news = newsMapper.toNews(request);
        news.setSlug(slug);
        News savedNews = newsRepository.save(news);
        log.info("Created news with id: {}", savedNews.getId());
        return newsMapper.toNewsResponse(savedNews);
    }

    @Override
    public News getNewsById(Long id) {
        return newsRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
    }

    @Override
    public NewsResponse getNewsDetail(String identifier, boolean deleted) {
        News news;

        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            news = deleted
                    ? getDeletedNewsById(id)
                    : getNewsById(id);
        } else {
            news = deleted
                    ? newsRepository.findBySlugAndDeletedAtIsNotNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND))
                    : newsRepository.findBySlugAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
        }

        return newsMapper.toNewsResponse(news);
    }

    @Override
    public PageResponse<NewsResponse> getAllNews(NewsFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<NewsResponse> getAllDeletedNews(NewsFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public NewsResponse updateNews(Long id, NewsRequest request) {
        News news = getNewsById(id);

        if (StringUtils.hasText(request.title())
                && newsRepository.existsByTitleAndIdNot(request.title(), id)) {
            throw new AppException(ErrorCode.NEWS_SLUG_EXISTED);
        }
        if (StringUtils.hasText(request.slug())
                && newsRepository.existsBySlugAndIdNot(request.slug(), id)) {
            throw new AppException(ErrorCode.NEWS_SLUG_EXISTED);
        }

        newsMapper.updateNews(news, request);

        if (StringUtils.hasText(request.slug())) {
            news.setSlug(request.slug());
        } else if (StringUtils.hasText(request.title())) {
            news.setSlug(SlugUtils.toSlug(request.title()));
        }

        News savedNews = newsRepository.save(news);
        log.info("Updated news with id: {}", savedNews.getId());
        return newsMapper.toNewsResponse(savedNews);
    }

    @Override
    @Transactional
    public void softDeleteNews(Long id) {
        News news = getNewsById(id);
        if (news.getDeletedAt() != null) {
            log.warn("News with id {} is already soft deleted", id);
            return;
        }
        news.setDeletedAt(LocalDateTime.now());
        news.setDeletedBy(auditorAwareConfig.getCurrentAuditorReference());
        newsRepository.save(news);
        log.info("Soft-deleted news with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteNews(Long id) {
        News news = getDeletedNewsById(id);
        if (news.getDeletedAt() == null) {
            log.warn("News with id {} is not deleted", id);
            return;
        }
        newsRepository.delete(news);
        log.info("Permanently deleted news with id: {}", id);
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
        news.setDeletedBy(null);
        newsRepository.save(news);
        log.info("News restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> newsIds = request.ids();
        String action = request.action().toUpperCase();

        if (newsIds == null || newsIds.isEmpty()) {
            throw new AppException(ErrorCode.NEWS_SELECTION_REQUIRED);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                newsRepository.updateStatusByIds(NewsStatus.valueOf(action), newsIds);
                log.info("Bulk update status {} news ids: {}", action, newsIds);
            }
            case "SOFT_DELETE" -> {
                User currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                newsRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, newsIds);
                log.info("Bulk soft-delete news ids: {}", newsIds);
            }
            case "DELETE" -> {
                Set<News> newsItems = newsRepository.findAllDeletedByIds(newsIds);
                newsRepository.deleteAll(newsItems);
                log.info("Bulk permanently delete news ids: {}", newsIds);
            }
            case "RESTORE" -> {
                newsRepository.restoreDeletedByIds(newsIds);
                log.info("Bulk restore news ids: {}", newsIds);
            }
            default -> throw new AppException(ErrorCode.NEWS_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} news items", action, newsIds.size());
    }

    // ======================== Private methods ========================

    private News getDeletedNewsById(Long id) {
        return newsRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_FOUND));
    }

    private PageResponse<NewsResponse> getAllWithFilterAndSearch(NewsFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), SortUtils.parseSort(request.sort()));

        Specification<News> spec = NewsSpecification.buildSpecification(request, isDeleted);
        Page<NewsProjection> page = newsRepository
                .findBy(spec, q -> q.as(NewsProjection.class).page(pageable));

        List<NewsResponse> content = page.getContent().stream()
                .map(proj -> NewsResponse.builder()
                        .id(proj.getId())
                        .title(proj.getTitle())
                        .slug(proj.getSlug())
                        .summary(proj.getSummary())
                        .content(proj.getContent())
                        .thumbnail(proj.getThumbnail())
                        .status(proj.getStatus())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<NewsResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
