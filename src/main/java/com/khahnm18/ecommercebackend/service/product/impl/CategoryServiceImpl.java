package com.khahnm18.ecommercebackend.service.product.impl;

import com.khahnm18.ecommercebackend.enums.CategoryStatus;
import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.CategoryRequest;
import com.khahnm18.ecommercebackend.dto.response.product.CategoryResponse;
import com.khahnm18.ecommercebackend.entity.product.Category;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.entity.product.QCategory;
import com.khahnm18.ecommercebackend.mapper.CategoryMapper;
import com.khahnm18.ecommercebackend.repository.product.CategoryRepository;
import com.khahnm18.ecommercebackend.service.product.CategoryService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "CATEGORY-SERVICE")
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final JPAQueryFactory queryFactory;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse getCategoryDetailById(UUID categoryId, boolean isDeleted) {
        Category category = isDeleted
            ? this.getSoftDeletedCategoryById(categoryId)
            : this.getCategoryById(categoryId);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(
        int page,
        int size,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        CategoryStatus status,
        UUID parentId
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        QCategory category = QCategory.category;

        Predicate[] predicates = {
            category.isDeleted.eq(isDeleted),
            Optional.ofNullable(status).map(category.status::eq).orElse(null),
            Optional.ofNullable(parentId).map(category.parentId::eq).orElse(null),
            Optional.ofNullable(startDate).map(category.createdAt::goe).orElse(null),
            Optional.ofNullable(endDate).map(category.createdAt::loe).orElse(null),
            Optional.ofNullable(keyword)
                .filter(kw -> !kw.isBlank())
                .map(String::strip)
                .map(kw -> category.name.containsIgnoreCase(kw)
                    .or(category.slug.containsIgnoreCase(kw)))
                .orElse(null)
        };

        List<CategoryResponse> content = queryFactory
            .selectFrom(category)
            .where(predicates)
            .orderBy(category.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream()
            .map(categoryMapper::toCategoryResponse)
            .toList();

        JPAQuery<Long> countQuery = queryFactory
            .select(category.count())
            .from(category)
            .where(predicates);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (request.parentId() != null) {
            this.getCategoryById(request.parentId());
        }
        Category category = categoryMapper.toCategory(request);
        category = this.saveCategory(category);
        log.info("Category created: name={}, slug={}", category.getName(), category.getSlug());
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID categoryId, CategoryRequest request) {
        Category category = getCategoryById(categoryId);
        this.validateParentCategory(categoryId, request);
        categoryMapper.updateCategory(category, request);
        category = this.saveCategory(category);
        log.info("Category updated: categoryId={}, name={}", categoryId, category.getName());
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @Transactional
    public void softDeleteCategory(UUID categoryId) {
        Category category = getCategoryById(categoryId);
        this.softDelete(category);
        log.info("Category soft deleted with category_id: {}", categoryId);
    }

    @Override
    @Transactional
    public void restoreCategory(UUID categoryId) {
        Category category = getSoftDeletedCategoryById(categoryId);
        this.restore(category);
        log.info("Category restored with category_id: {}", categoryId);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> categoryIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "ACTIVE", "INACTIVE" -> {
                SecurityUtil.requireAuthority("CATEGORY_UPDATE");
                List<Category> categories = categoryRepository.findAllById(categoryIds);
                categories.forEach(category -> category.setStatus(CategoryStatus.valueOf(action)));
                log.info("Bulk update status {} category with categorys: {}", action, categoryIds);
            }
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("CATEGORY_TRASH");
                List<Category> categories = categoryRepository.findAllById(categoryIds);
                categories.forEach(this::softDelete);
                log.info("Bulk action soft delete category with categorys: {}", categoryIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("CATEGORY_TRASH");
                List<Category> categories = categoryRepository.findAllById(categoryIds);
                categories.forEach(this::restore);
                log.info("Bulk action restore category with categorys: {}", categoryIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("CATEGORY_DELETE");
                categoryRepository.deleteAllByIdInAndIsDeletedTrue(categoryIds);
                log.info("Bulk action delete category with categorys: {}", categoryIds);
            }
        }
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        Category category = getSoftDeletedCategoryById(categoryId);
        categoryRepository.delete(category);
        log.info("Category deleted success with category_id: {}", categoryId);
    }

    private Category saveCategory(Category category) {
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Category save failed - duplicate entry: name={}, slug={}", category.getName(), category.getSlug());
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
    }

    private Category getCategoryById(UUID id) {
        return categoryRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private Category getSoftDeletedCategoryById(UUID id) {
        return categoryRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private void softDelete(Category category) {
        category.setIsDeleted(true);
        category.setDeletedAt(Instant.now());
        category.setDeletedBy(SecurityUtil.getCurrentAccount());
    }

    private void restore(Category category) {
        category.setIsDeleted(false);
        category.setDeletedAt(null);
        category.setDeletedBy(null);
    }

    private void validateParentCategory(UUID categoryId, CategoryRequest request) {
        if (request.parentId() != null) {
            UUID parentId = request.parentId();
            Set<UUID> visited = new HashSet<>();
            while (parentId != null) {
                if (parentId.equals(categoryId) || visited.contains(parentId)) {
                    throw new AppException(ErrorCode.CATEGORY_PARENT_INVALID);
                }
                visited.add(parentId);
                parentId = this.getCategoryById(parentId).getParentId();
            }
            this.getCategoryById(request.parentId());
        }
    }

}
