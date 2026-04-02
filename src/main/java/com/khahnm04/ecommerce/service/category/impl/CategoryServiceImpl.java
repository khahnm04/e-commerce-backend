package com.khahnm04.ecommerce.service.category.impl;

import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.category.CategoryResponse;
import com.khahnm04.ecommerce.entity.category.Category;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.category.CategoryMapper;
import com.khahnm04.ecommerce.repository.category.CategoryRepository;
import com.khahnm04.ecommerce.service.category.CategoryService;
import com.khahnm04.ecommerce.specification.category.CategorySpecification;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        String slug = StringUtils.hasText(request.slug())
                ? request.slug()
                : SlugUtils.toSlug(request.name());
        if (categoryRepository.existsBySlug(slug)) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        Category category = categoryMapper.toCategory(request);
        category.setSlug(slug);

        // Set parent
        category.setParent(Optional.ofNullable(request.parentId())
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

        log.info("Created category with id: {}", savedCategory.getId());
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    public CategoryResponse getCategoryDetail(String identifier, boolean deleted) {
        Category category;

        if (identifier.matches("\\d+")) {
            Long id = Long.parseLong(identifier);
            category = deleted
                    ? getDeletedCategoryById(id)
                    : getCategoryById(id);
        } else {
            category = deleted
                    ? categoryRepository.findBySlugAndDeletedAtIsNotNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND))
                    : categoryRepository.findBySlugAndDeletedAtIsNull(identifier)
                            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public PageResponse<CategoryResponse> getAllCategories(CategoryFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<CategoryResponse> getAllDeletedCategories(CategoryFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = getCategoryById(id);

        if (StringUtils.hasText(request.name())
                && categoryRepository.existsByNameAndIdNot(request.name(), id)) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        if (StringUtils.hasText(request.slug())
                && categoryRepository.existsBySlugAndIdNot(request.slug(), id)) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        categoryMapper.updateCategory(category, request);

        // Handle parent change
        Long currentParentId = (category.getParent() != null) ? category.getParent().getId() : null;
        Long newParentId = request.parentId();

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
    public void softDeleteCategory(Long id) {
        Category category = getCategoryById(id);
        if (category.getDeletedAt() != null) {
            log.warn("Category with id {} is already soft deleted", id);
            return;
        }
        List<Long> allRelatedIds = categoryRepository.findAllIdsByPath(category.getPath());
        categoryRepository.softDeleteAllByIds(allRelatedIds, LocalDateTime.now());
        log.info("Soft-deleted category with id: {} and {} descendants", id, allRelatedIds.size() - 1);
    }

    @Override
    @Transactional
    public void restoreCategory(Long id) {
        Category category = getDeletedCategoryById(id);
        if (category.getDeletedAt() == null) {
            log.warn("Category with id {} is not in a deleted state", id);
            return;
        }

        LocalDateTime deletedTime = category.getDeletedAt();
        LocalDateTime start = deletedTime.minusSeconds(2);
        LocalDateTime end = deletedTime.plusSeconds(2);

        categoryRepository.restoreDescendantsByTimeRange(category.getPath(), start, end);
        log.info("Restored category with id {} and matching descendants", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> categoryIds = request.ids();
        String action = request.action().toUpperCase();

        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                categoryRepository.updateStatusByIds(CategoryStatus.valueOf(action), categoryIds);
                log.info("Bulk update status {} categories ids: {}", action, categoryIds);
            }
            case "DELETE" -> {
                categoryRepository.softDeleteByIds(LocalDateTime.now(), categoryIds);
                log.info("Bulk delete categories ids: {}", categoryIds);
            }
            case "RESTORE" -> {
                categoryRepository.restoreDeletedByIds(categoryIds);
                log.info("Bulk restore categories ids: {}", categoryIds);
            }
            default -> throw new AppException(ErrorCode.CATEGORY_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} categories", action, categoryIds.size());
    }

    // ======================== Private methods ========================

    private Category getDeletedCategoryById(Long id) {
        return categoryRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private PageResponse<CategoryResponse> getAllWithFilterAndSearch(CategoryFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                SortUtils.parseSort(request.sort()));

        Specification<Category> spec = CategorySpecification.buildSpecification(request, isDeleted);
        Page<Category> page = categoryRepository.findAll(spec, pageable);

        List<CategoryResponse> content = page.getContent().stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());

        Page<CategoryResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
