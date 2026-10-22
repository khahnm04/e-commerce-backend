package com.khahnm18.ecommercebackend.repository.product.impl;

import com.khahnm18.ecommercebackend.dto.request.product.CategorySearchRequest;
import com.khahnm18.ecommercebackend.entity.product.Category;
import com.khahnm18.ecommercebackend.entity.product.QCategory;
import com.khahnm18.ecommercebackend.repository.product.CustomCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Category> search(CategorySearchRequest request, Pageable pageable) {
        QCategory category = QCategory.category;
        BooleanBuilder where = buildWhere(category, request);

        List<Category> content = queryFactory
            .selectFrom(category)
            .where(where)
            .orderBy(category.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(category.count())
            .from(category)
            .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Category> findActiveCategories() {
        QCategory category = QCategory.category;
        return queryFactory
            .selectFrom(category)
            .where(category.parentId.isNull().and(category.isDeleted.isFalse()))
            .orderBy(category.createdAt.desc())
            .fetch();
    }

    private BooleanBuilder buildWhere(QCategory category, CategorySearchRequest request) {
        BooleanBuilder where = new BooleanBuilder();

        if (request.isDeleted() != null) {
            where.and(category.isDeleted.eq(request.isDeleted()));
        }
        if (request.status() != null) {
            where.and(category.status.eq(request.status()));
        }
        if (request.parentId() != null) {
            where.and(category.parentId.eq(request.parentId()));
        }
        if (request.startDate() != null) {
            where.and(category.createdAt.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            where.and(category.createdAt.loe(request.endDate()));
        }
        if (request.keyword() != null && !request.keyword().isBlank()) {
            String kw = request.keyword().strip();
            where.and(category.name.containsIgnoreCase(kw)
                .or(category.slug.containsIgnoreCase(kw)));
        }

        return where;
    }

}

