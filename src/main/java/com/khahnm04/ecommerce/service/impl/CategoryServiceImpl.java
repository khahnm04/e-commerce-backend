package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import com.khahnm04.ecommerce.common.util.SlugUtils;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.projection.CategoryProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.request.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.response.CategoryResponse;
import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.entity.Category;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.CategoryMapper;
import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.repository.CategoryRepository;
import com.khahnm04.ecommerce.repository.ProductCategoryRepository;
import com.khahnm04.ecommerce.service.CategoryService;
import com.khahnm04.ecommerce.specification.CategorySpecification;
import jakarta.persistence.EntityManager;
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
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryMapper categoryMapper;
    private final AuditorAwareConfig auditorAwareConfig;
    private final EntityManager entityManager;

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

        category.setParent(Optional.ofNullable(request.parentId())
                .map(this::getCategoryById)
                .orElse(null));

        Category savedCategory = categoryRepository.save(category);

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

        Long currentParentId = (category.getParent() != null) ? category.getParent().getId() : null;
        Long newParentId = request.parentId();

        if (!Objects.equals(currentParentId, newParentId)) {
            Category newParent = null;
            if (newParentId != null) {
                newParent = getCategoryById(newParentId);
                if (Objects.equals(newParent.getId(), category.getId())) {
                    throw new AppException(ErrorCode.CANNOT_MOVE_PARENT_TO_CHILD);
                }
            }
            category.setParent(newParent);
        }

        Category savedCategory = categoryRepository.save(category);
        log.info("Updated category for id {}", savedCategory.getId());
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
        category.setDeletedAt(LocalDateTime.now());
        category.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        categoryRepository.save(category);
        log.info("Soft-deleted category with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getDeletedCategoryById(id);
        if (category.getDeletedAt() == null) {
            log.warn("Category with id {} is already deleted", id);
            return;
        }
        if (categoryRepository.existsByParentId(id)) {
            throw new AppException(ErrorCode.CATEGORY_HAS_CHILDREN);
        }
        if (productCategoryRepository.existsByCategoryId(id)) {
            throw new AppException(ErrorCode.CATEGORY_HAS_PRODUCTS);
        }
        categoryRepository.delete(category);
        log.info("Permanently deleted category with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreCategory(Long id) {
        Category category = getDeletedCategoryById(id);
        if (category.getDeletedAt() == null) {
            log.warn("Category with id {} is not in a deleted state", id);
            return;
        }
        category.setDeletedAt(null);
        category.setDeletedBy(null);
        categoryRepository.save(category);
        log.info("Restored category with id {}", id);
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
            case "SOFT_DELETE" -> {
                User currentAuditor = auditorAwareConfig.getCurrentAuditor()
                        .map(user -> entityManager.getReference(User.class, user.getId()))
                        .orElse(null);
                categoryRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, categoryIds);
                log.info("Bulk soft-delete categories ids: {}", categoryIds);
            }
            case "DELETE" -> {
                if (categoryRepository.existsByParentIdIn(categoryIds)) {
                    throw new AppException(ErrorCode.CATEGORY_HAS_CHILDREN);
                }
                if (productCategoryRepository.existsByCategoryIdIn(categoryIds)) {
                    throw new AppException(ErrorCode.CATEGORY_HAS_PRODUCTS);
                }
                Set<Category> categories = categoryRepository.findAllDeletedByIds(categoryIds);
                categoryRepository.deleteAll(categories);
                log.info("Bulk permanently delete categories ids: {}", categoryIds);
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
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(), SortUtils.parseSort(request.sort()));

        Specification<Category> spec = CategorySpecification.buildSpecification(request, isDeleted);
        Page<CategoryProjection> page = categoryRepository
                .findBy(spec, q -> q.as(CategoryProjection.class).page(pageable));

        List<CategoryResponse> content = page.getContent().stream()
                .map(proj -> CategoryResponse.builder()
                        .id(proj.getId())
                        .name(proj.getName())
                        .slug(proj.getSlug())
                        .description(proj.getDescription())
                        .thumbnail(proj.getThumbnail())
                        .position(proj.getPosition())
                        .icon(proj.getIcon())
                        .isFeatured(proj.getIsFeatured())
                        .status(proj.getStatus())
                        .parentId(proj.getParentId())
                        .parentName(proj.getParentName())
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<CategoryResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
