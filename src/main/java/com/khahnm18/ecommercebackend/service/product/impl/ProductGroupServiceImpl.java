package com.khahnm18.ecommercebackend.service.product.impl;

import com.khahnm18.ecommercebackend.dto.request.BulkActionRequest;
import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupRequest;
import com.khahnm18.ecommercebackend.dto.response.product.ProductGroupResponse;
import com.khahnm18.ecommercebackend.entity.product.Category;
import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import com.khahnm18.ecommercebackend.entity.product.QProductGroup;
import com.khahnm18.ecommercebackend.entity.product.QProductGroupCategory;
import com.khahnm18.ecommercebackend.exception.AppException;
import com.khahnm18.ecommercebackend.exception.ErrorCode;
import com.khahnm18.ecommercebackend.mapper.ProductGroupMapper;
import com.khahnm18.ecommercebackend.repository.product.CategoryRepository;
import com.khahnm18.ecommercebackend.repository.product.ProductGroupRepository;
import com.khahnm18.ecommercebackend.service.product.ProductGroupCategoryService;
import com.khahnm18.ecommercebackend.service.product.ProductGroupService;
import com.khahnm18.ecommercebackend.util.SecurityUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j(topic = "PRODUCT-GROUP-SERVICE")
@Service
@RequiredArgsConstructor
public class ProductGroupServiceImpl implements ProductGroupService {

    private final JPAQueryFactory queryFactory;
    private final ProductGroupMapper productGroupMapper;
    private final CategoryRepository categoryRepository;
    private final ProductGroupRepository productGroupRepository;
    private final ProductGroupCategoryService productGroupCategoryService;

    @Override
    public ProductGroupResponse getProductGroupDetailById(UUID productGroupId, boolean isDeleted) {
        ProductGroup productGroup = isDeleted
            ? this.getSoftDeletedProductGroupById(productGroupId)
            : this.getProductGroupById(productGroupId);
        List<Category> categories = categoryRepository.findCategoriesByGroupId(productGroup.getId());
        return productGroupMapper.toProductGroupResponse(productGroup, categories);
    }

    @Override
    public Page<ProductGroupResponse> getAllProductGroups(
        int page,
        int size,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate,
        UUID categoryId
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        QProductGroup productGroup = QProductGroup.productGroup;
        QProductGroupCategory pgc = QProductGroupCategory.productGroupCategory;

        Predicate[] predicates = {
            productGroup.isDeleted.eq(isDeleted),
            Optional.ofNullable(startDate).map(productGroup.createdAt::goe).orElse(null),
            Optional.ofNullable(endDate).map(productGroup.createdAt::loe).orElse(null),
            Optional.ofNullable(categoryId)
                .map(cid -> JPAExpressions.selectOne()
                    .from(pgc)
                    .where(pgc.groupId.eq(productGroup.id).and(pgc.categoryId.eq(cid)))
                    .exists())
                .orElse(null),
            Optional.ofNullable(keyword)
                .filter(kw -> !kw.isBlank())
                .map(String::strip)
                .map(kw -> productGroup.name.containsIgnoreCase(kw)
                    .or(productGroup.slug.containsIgnoreCase(kw)))
                .orElse(null)
        };

        List<ProductGroupResponse> content = queryFactory
            .selectFrom(productGroup)
            .where(predicates)
            .orderBy(productGroup.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream()
            .map(productGroupMapper::toProductGroupResponse)
            .toList();

        JPAQuery<Long> countQuery = queryFactory
            .select(productGroup.count())
            .from(productGroup)
            .where(predicates);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    @Transactional
    public ProductGroupResponse createProductGroup(ProductGroupRequest request) {
        ProductGroup productGroup = productGroupMapper.toProductGroup(request);
        productGroup = this.saveProductGroup(productGroup);

        List<Category> categories =
            productGroupCategoryService.assignCategoriesToProductGroup(productGroup, request.categories());

        log.info("Product group created: name={}, slug={}", productGroup.getName(), productGroup.getSlug());
        return productGroupMapper.toProductGroupResponse(productGroup, categories);
    }

    @Override
    @Transactional
    public ProductGroupResponse updateProductGroup(UUID productGroupId, ProductGroupRequest request) {
        ProductGroup productGroup = getProductGroupById(productGroupId);
        productGroupMapper.updateProductGroup(productGroup, request);
        productGroup = this.saveProductGroup(productGroup);

        List<Category> categories =
            productGroupCategoryService.replaceCategoriesForProductGroup(productGroup, request.categories());

        log.info("Product group updated: productGroupId={}, name={}", productGroupId, productGroup.getName());
        return productGroupMapper.toProductGroupResponse(productGroup, categories);
    }

    @Override
    @Transactional
    public void softDeleteProductGroup(UUID productGroupId) {
        ProductGroup productGroup = getProductGroupById(productGroupId);
        this.softDelete(productGroup);
        log.info("Product group soft deleted with product_group_id: {}", productGroupId);
    }

    @Override
    @Transactional
    public void restoreProductGroup(UUID productGroupId) {
        ProductGroup productGroup = getSoftDeletedProductGroupById(productGroupId);
        this.restore(productGroup);
        log.info("Product restored with product_group_id: {}", productGroupId);
    }

    @Override
    @Transactional
    public void bulkAction(BulkActionRequest request) {
        Set<UUID> productGroupIds = request.ids();
        String action = request.action().toUpperCase();
        switch (action) {
            case "SOFT_DELETE" -> {
                SecurityUtil.requireAuthority("PRODUCT_GROUP_TRASH");
                List<ProductGroup> groups = productGroupRepository.findAllById(productGroupIds);
                groups.forEach(this::softDelete);
                log.info("Bulk action soft delete product group with productGroupIds: {}", productGroupIds);
            }
            case "RESTORE" -> {
                SecurityUtil.requireAuthority("PRODUCT_GROUP_TRASH");
                List<ProductGroup> groups = productGroupRepository.findAllById(productGroupIds);
                groups.forEach(this::restore);
                log.info("Bulk action restore product group with productGroupIds: {}", productGroupIds);
            }
            case "DELETE" -> {
                SecurityUtil.requireAuthority("PRODUCT_GROUP_DELETE");
                productGroupRepository.deleteAllByIdInAndIsDeletedTrue(productGroupIds);
                log.info("Bulk action delete product group with productGroupIds: {}", productGroupIds);
            }
        }
    }

    @Override
    @Transactional
    public void deleteProductGroup(UUID productGroupId) {
        ProductGroup productGroup = getSoftDeletedProductGroupById(productGroupId);
        productGroupRepository.delete(productGroup);
        log.info("Product group deleted success with product_group_id: {}", productGroupId);
    }

    private ProductGroup saveProductGroup(ProductGroup productGroup) {
        try {
            return productGroupRepository.save(productGroup);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Product group save failed - duplicate entry: name={}, slug={}", productGroup.getName(), productGroup.getSlug());
            throw new AppException(ErrorCode.PRODUCT_GROUP_EXISTED);
        }
    }

    private ProductGroup getProductGroupById(UUID id) {
        return productGroupRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_GROUP_NOT_FOUND));
    }

    private ProductGroup getSoftDeletedProductGroupById(UUID id) {
        return productGroupRepository.findByIdAndIsDeletedTrue(id)
            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_GROUP_NOT_FOUND));
    }

    private void softDelete(ProductGroup productGroup) {
        productGroup.setIsDeleted(true);
        productGroup.setDeletedAt(Instant.now());
        productGroup.setDeletedBy(SecurityUtil.getCurrentAccount());
    }

    private void restore(ProductGroup productGroup) {
        productGroup.setIsDeleted(false);
        productGroup.setDeletedAt(null);
        productGroup.setDeletedBy(null);
    }

}
