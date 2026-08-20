package com.khahnm04.ecommerce.service.category;

import com.khahnm04.ecommerce.constant.category.CategoryStatus;
import com.khahnm04.ecommerce.dto.projection.CategoryProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryFilterRequest;
import com.khahnm04.ecommerce.dto.request.category.CategoryRequest;
import com.khahnm04.ecommerce.dto.response.category.CategoryResponse;
import com.khahnm04.ecommerce.entity.category.Category;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.category.CategoryMapper;
import com.khahnm04.ecommerce.repository.category.CategoryRepository;
import com.khahnm04.ecommerce.specification.CategorySpecification;
import com.khahnm04.ecommerce.util.PageableUtil;
import com.khahnm04.ecommerce.util.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Slf4j(topic = "CATEGORY-SERVICE")
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsBySlugAndIsDeletedFalse(request.slug())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = categoryMapper.toCategory(request);
        categoryRepository.save(category);
        log.info("Role created: slug={}, name={}", category.getSlug(), category.getName());
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponse getCategoryDetailById(Long id, boolean isDeleted) {
        Category category = isDeleted
            ? getSoftDeletedCategoryById(id)
            : getCategoryById(id);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(CategoryFilterRequest request) {
        Pageable pageable = PageableUtil.createPageable(
            request.page(),
            request.size(),
            request.sort()
        );
        Page<CategoryProjection> categoryPage = categoryRepository.findBy(
            CategorySpecification.buildQuery(request, request.isDeleted()),
            query -> query.as(CategoryProjection.class).page(pageable)
        );
        return categoryPage.map(categoryMapper::toCategoryResponse);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        if (categoryRepository.existsBySlugAndIdNotAndIsDeletedFalse(request.slug(), id)) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = getCategoryById(id);
        if (request.parentId() != null) {
            if (request.parentId().equals(id)) {
                throw new AppException(ErrorCode.CATEGORY_PARENT_INVALID);
            }
            if (isDescendant(id, request.parentId())) {
                throw new AppException(ErrorCode.CATEGORY_CIRCULAR_REFERENCE);
            }
            category.setParentId(request.parentId());
        } else {
            category.setParentId(null);
        }
        categoryMapper.updateCategory(category, request);
        log.info("Category updated: slug={}, name={}", category.getSlug(), category.getName());
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public void softDeleteCategory(Long id) {
        Category category = getCategoryById(id);
        category.setIsDeleted(true);
        category.setDeletedAt(Instant.now());
        category.setDeletedBy(SecurityUtil.getCurrentUserFullName());
        categoryRepository.save(category);
        log.info("Soft deleted category with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getSoftDeletedCategoryById(id);
        if (categoryRepository.existsByParentIdAndIsDeletedFalse(id)) {
            throw new AppException(ErrorCode.CATEGORY_HAS_CHILDREN);
        }
        categoryRepository.delete(category);
        log.info("Deleted category with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreCategory(Long id) {
        Category category = getSoftDeletedCategoryById(id);
        category.setIsDeleted(false);
        category.setDeletedAt(null);
        category.setDeletedBy(null);
        categoryRepository.save(category);
        log.info("Category restored with id: {}", id);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<Long> categoryIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case  "ACTIVE", "INACTIVE" -> {
                categoryRepository.updateStatusByIds(CategoryStatus.valueOf(action), categoryIds);
                log.info("Bulk update status {} categories ids: {}", action, categoryIds);
            }
            case "SOFT_DELETE" -> {
                categoryRepository.softDeleteAllByIds(categoryIds, SecurityUtil.getCurrentUserFullName());
                log.info("Bulk action soft delete categories ids: {}", categoryIds);
            }
            case "RESTORE" -> {
                categoryRepository.restoreAllByIds(categoryIds);
                log.info("Bulk action restore categories ids: {}", categoryIds);
            }
            case "DELETE" -> {
                if (categoryRepository.existsChildrenByParentIds(categoryIds)) {
                    throw new AppException(ErrorCode.CATEGORY_HAS_CHILDREN);
                }
                categoryRepository.deleteAllByIds(categoryIds);
                log.info("Bulk action delete categories ids: {}", categoryIds);
            }
        }
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private Category getSoftDeletedCategoryById(Long id) {
        return categoryRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private boolean isDescendant(Long categoryId, Long targetId) {
        List<Category> children = categoryRepository.findByParentIdAndIsDeletedFalse(categoryId);
        for (Category child : children) {
            if (child.getId().equals(targetId)) return true;
            if (isDescendant(child.getId(), targetId)) return true;
        }
        return false;
    }

}
