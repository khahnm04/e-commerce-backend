package com.khahnm18.ecommercebackend.repository.product.impl;

import com.khahnm18.ecommercebackend.dto.request.product.ProductSearchRequest;
import com.khahnm18.ecommercebackend.entity.product.Product;
import com.khahnm18.ecommercebackend.entity.product.QProduct;
import com.khahnm18.ecommercebackend.repository.product.CustomProductRepository;
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
public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> search(ProductSearchRequest request, Pageable pageable) {
        QProduct product = QProduct.product;
        BooleanBuilder where = buildWhere(product, request);

        List<Product> content = queryFactory
            .selectFrom(product)
            .where(where)
            .orderBy(product.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(product.count())
            .from(product)
            .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder buildWhere(QProduct product, ProductSearchRequest request) {
        BooleanBuilder where = new BooleanBuilder();

        if (request.isDeleted() != null) {
            where.and(product.isDeleted.eq(request.isDeleted()));
        }
        if (request.status() != null) {
            where.and(product.status.eq(request.status()));
        }
        if (request.versionId() != null) {
            where.and(product.versionId.eq(request.versionId()));
        }
        if (request.startDate() != null) {
            where.and(product.createdAt.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            where.and(product.createdAt.loe(request.endDate()));
        }
        if (request.keyword() != null && !request.keyword().isBlank()) {
            String kw = request.keyword().strip();
            where.and(product.name.containsIgnoreCase(kw)
                .or(product.slug.containsIgnoreCase(kw))
                .or(product.color.containsIgnoreCase(kw))
                .or(product.sku.containsIgnoreCase(kw)));
        }

        return where;
    }

}

