package com.khahnm18.ecommercebackend.repository.product.impl;

import com.khahnm18.ecommercebackend.dto.request.product.ProductGroupSearchRequest;
import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import com.khahnm18.ecommercebackend.entity.product.QProductGroup;
import com.khahnm18.ecommercebackend.entity.product.QProductGroupCategory;
import com.khahnm18.ecommercebackend.repository.product.CustomProductGroupRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
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
public class CustomProductGroupRepositoryImpl implements CustomProductGroupRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductGroup> search(ProductGroupSearchRequest request, Pageable pageable) {
        QProductGroup productGroup = QProductGroup.productGroup;
        QProductGroupCategory pgc = QProductGroupCategory.productGroupCategory;

        BooleanBuilder where = buildWhere(productGroup, pgc, request);

        List<ProductGroup> content = queryFactory
            .selectFrom(productGroup)
            .where(where)
            .orderBy(productGroup.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(productGroup.count())
            .from(productGroup)
            .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder buildWhere(QProductGroup productGroup, QProductGroupCategory pgc, ProductGroupSearchRequest request) {
        BooleanBuilder where = new BooleanBuilder();

        if (request.isDeleted() != null) {
            where.and(productGroup.isDeleted.eq(request.isDeleted()));
        }
        if (request.startDate() != null) {
            where.and(productGroup.createdAt.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            where.and(productGroup.createdAt.loe(request.endDate()));
        }
        if (request.categoryId() != null) {
            where.and(JPAExpressions.selectOne()
                .from(pgc)
                .where(pgc.groupId.eq(productGroup.id).and(pgc.categoryId.eq(request.categoryId())))
                .exists());
        }
        if (request.keyword() != null && !request.keyword().isBlank()) {
            String kw = request.keyword().strip();
            where.and(productGroup.name.containsIgnoreCase(kw)
                .or(productGroup.slug.containsIgnoreCase(kw)));
        }

        return where;
    }

}
