package com.khahnm04.ecommerce.modules.catalog.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.catalog.dto.request.*;
import com.khahnm04.ecommerce.modules.catalog.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.modules.catalog.entity.Category;
import com.khahnm04.ecommerce.modules.catalog.enums.CategorySortField;
import com.khahnm04.ecommerce.modules.catalog.enums.CategoryStatus;
import com.khahnm04.ecommerce.modules.catalog.mapper.CategoryMapper;
import com.khahnm04.ecommerce.modules.catalog.mapper.mybaits.CategoryMyBatisMapper;
import com.khahnm04.ecommerce.modules.catalog.repository.CategoryRepository;
import com.khahnm04.ecommerce.modules.catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryMyBatisMapper categoryMyBatisMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        Category category = categoryMapper.toCategory(request);

        // Set parent
        category.setParent(Optional.ofNullable(request.getParentId())
                .map(this::getCategoryById)
                .orElse(null));

        // Calculate path and level
        String parentPath = Optional.ofNullable(category.getParent())
                .map(Category::getPath)
                .orElse("");

        int parentLevel = Optional.ofNullable(category.getParent())
                .map(Category::getLevel)
                .orElse(-1);

        category.setLevel(parentLevel + 1);

        Category savedCategory = categoryRepository.save(category);
        savedCategory.setPath(parentPath + savedCategory.getId() + "/");

        log.info("Created category id: {}", savedCategory.getId());
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<CategoryResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<CategoryResponse> getAllDeletedCategories(CategoryFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<CategoryResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public CategoryResponse getCategoryDetailById(Long id) {
        return categoryMapper.toCategoryResponse(getCategoryById(id));
    }

    @Override
    public CategoryResponse getCategoryDetailBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = getCategoryById(id);

        if (StringUtils.hasText(request.getName())
                && categoryRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        if (StringUtils.hasText(request.getSlug())
                && categoryRepository.existsBySlugIgnoreCaseAndIdNot(request.getSlug(), id)) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        categoryMapper.updateCategory(category, request);

        // Handle parent change
        Long currentParentId = (category.getParent() != null) ? category.getParent().getId() : null;
        Long newParentId = request.getParentId();

        if (Objects.equals(currentParentId, newParentId)) {
            return categoryMapper.toCategoryResponse(categoryRepository.save(category));
        }

        String oldPath = category.getPath();
        String newPath = category.getId() + "/";
        Category newParent = null;

        if (newParentId != null) {
            newParent = getCategoryById(newParentId);
            if (newParent.getPath().startsWith(oldPath)) {
                throw new AppException(ErrorCode.CANNOT_MOVE_PARENT_TO_CHILD);
            }
            newPath = newParent.getPath() + newPath;
        }

        int newLevel = (newParent != null) ? newParent.getLevel() + 1 : 0;

        category.setParent(newParent);
        category.setPath(newPath);
        category.setLevel(newLevel);

        Category savedCategory = categoryRepository.save(category);
        categoryRepository.updateDescendantPaths(oldPath + "%", newPath, oldPath.length());
        log.info("Updated category tree for id {}", savedCategory.getId());
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public void updateCategoryStatus(Long id, CategoryUpdateStatusRequest request) {
        Category category = getCategoryById(id);

        if (category.getStatus() == request.getStatus()) {
            return;
        }

        category.setStatus(request.getStatus());
        categoryRepository.save(category);
        log.info("Changed status for category id {} to {}", id, request.getStatus());
    }

    @Override
    @Transactional
    public void softDeleteCategory(Long id) {
        Category category = getCategoryById(id);
        List<Long> allRelatedIds = categoryRepository.findAllIdsByPath(category.getPath());
        categoryRepository.softDeleteAllByIds(allRelatedIds, LocalDateTime.now());
        log.info("Soft deleted category id {} and {} descendants", id, allRelatedIds.size() - 1);
    }

    @Override
    @Transactional
    public void restoreCategory(Long id) {
        categoryRepository.findDeletedCategoryById(id)
                .ifPresentOrElse(category -> {
                    if (category.getDeletedAt() == null) {
                        log.warn("Category with id {} is not deleted, no restoration needed", id);
                        return;
                    }

                    LocalDateTime deletedTime = category.getDeletedAt();
                    LocalDateTime start = deletedTime.minusSeconds(2);
                    LocalDateTime end = deletedTime.plusSeconds(2);

                    categoryRepository.restoreDescendantsByTimeRange(category.getPath(), start, end);
                    log.info("Restored category id {} and matching descendants", id);
                }, () -> {
                    throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(CategoryBulkActionRequest request) {
        Set<Long> categoryIds = request.getCategories();
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

        Set<Category> existingCategories = new HashSet<>(
                action.equals("RESTORE")
                        ? categoryRepository.findAllDeletedByIds(categoryIds)
                        : categoryRepository.findAllById(categoryIds));

        Set<Long> existingCategoryIds = existingCategories.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (categoryIds.size() != existingCategoryIds.size()) {
            Set<Long> missingIds = new HashSet<>(categoryIds);
            missingIds.removeAll(existingCategoryIds);
            log.error("Bulk action categories failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                CategoryStatus status = CategoryStatus.valueOf(action);
                categoryRepository.updateStatusByIds(status, existingCategoryIds);
                log.info("Bulk update status {} categories ids: {}", action, existingCategoryIds);
            }
            case "DELETE" -> {
                categoryRepository.softDeleteByIds(LocalDateTime.now(), existingCategoryIds);
                log.info("Bulk delete categories ids: {}", existingCategoryIds);
            }
            case "RESTORE" -> {
                categoryRepository.restoreDeletedByIds(existingCategoryIds);
                log.info("Bulk restore categories ids: {}", existingCategoryIds);
            }
            default -> throw new AppException(ErrorCode.CATEGORY_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} categories", action, existingCategoryIds.size());
    }

    // ======================== Private methods ========================

    private Page<CategoryResponse> getAllWithFilterAndSearch(CategoryFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        // Parse sort
        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), CategorySortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<CategoryResponse> content = categoryMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getParentId(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = categoryMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getParentId(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
