package com.khahnm18.ecommercebackend.repository.product.impl;

import com.khahnm18.ecommercebackend.dto.request.product.ProductVersionSearchRequest;
import com.khahnm18.ecommercebackend.entity.product.ProductVersion;
import com.khahnm18.ecommercebackend.entity.product.QProductVersion;
import com.khahnm18.ecommercebackend.repository.product.CustomProductVersionRepository;
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
public class CustomProductVersionRepositoryImpl implements CustomProductVersionRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductVersion> search(ProductVersionSearchRequest request, Pageable pageable) {
        QProductVersion productVersion = QProductVersion.productVersion;
        BooleanBuilder where = buildWhere(productVersion, request);

        List<ProductVersion> content = queryFactory
            .selectFrom(productVersion)
            .where(where)
            .orderBy(productVersion.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(productVersion.count())
            .from(productVersion)
            .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder buildWhere(QProductVersion productVersion, ProductVersionSearchRequest request) {
        BooleanBuilder where = new BooleanBuilder();

        if (request.isDeleted() != null) {
            where.and(productVersion.isDeleted.eq(request.isDeleted()));
        }
        if (request.startDate() != null) {
            where.and(productVersion.createdAt.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            where.and(productVersion.createdAt.loe(request.endDate()));
        }
        if (request.groupId() != null) {
            where.and(productVersion.groupId.eq(request.groupId()));
        }
        if (request.keyword() != null && !request.keyword().isBlank()) {
            String kw = request.keyword().strip();
            where.and(productVersion.name.containsIgnoreCase(kw)
                .or(productVersion.slug.containsIgnoreCase(kw))
                .or(productVersion.version.containsIgnoreCase(kw)));
        }

        return where;
    }

}

