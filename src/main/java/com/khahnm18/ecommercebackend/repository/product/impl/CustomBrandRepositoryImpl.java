package com.khahnm18.ecommercebackend.repository.product.impl;

import com.khahnm18.ecommercebackend.dto.request.product.BrandSearchRequest;
import com.khahnm18.ecommercebackend.entity.product.Brand;
import com.khahnm18.ecommercebackend.entity.product.QBrand;
import com.khahnm18.ecommercebackend.repository.product.CustomBrandRepository;
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
public class CustomBrandRepositoryImpl implements CustomBrandRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Brand> search(BrandSearchRequest request, Pageable pageable) {
        QBrand brand = QBrand.brand;
        BooleanBuilder where = buildWhere(brand, request);

        List<Brand> content = queryFactory
            .selectFrom(brand)
            .where(where)
            .orderBy(brand.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(brand.count())
            .from(brand)
            .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder buildWhere(QBrand brand, BrandSearchRequest request) {
        BooleanBuilder where = new BooleanBuilder();

        if (request.isDeleted() != null) {
            where.and(brand.isDeleted.eq(request.isDeleted()));
        }
        if (request.status() != null) {
            where.and(brand.status.eq(request.status()));
        }
        if (request.startDate() != null) {
            where.and(brand.createdAt.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            where.and(brand.createdAt.loe(request.endDate()));
        }
        if (request.keyword() != null && !request.keyword().isBlank()) {
            String kw = request.keyword().strip();
            where.and(brand.name.containsIgnoreCase(kw)
                .or(brand.slug.containsIgnoreCase(kw)));
        }

        return where;
    }

}

