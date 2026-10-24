package com.khahnm18.ecommercebackend.repository.auth.impl;

import com.khahnm18.ecommercebackend.dto.request.auth.RoleSearchRequest;
import com.khahnm18.ecommercebackend.entity.auth.QAccountRole;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import com.khahnm18.ecommercebackend.entity.auth.QRole;
import com.khahnm18.ecommercebackend.repository.auth.CustomRoleRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomRoleRepositoryImpl implements CustomRoleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Role> search(RoleSearchRequest request, Pageable pageable) {
        QRole role = QRole.role;
        BooleanBuilder where = buildWhere(role, request);

        List<Role> content = queryFactory
            .selectFrom(role)
            .where(where)
            .orderBy(role.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(role.count())
            .from(role)
            .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder buildWhere(QRole role, RoleSearchRequest request) {
        BooleanBuilder where = new BooleanBuilder();
        if (request.isDeleted() == null) {
            where.and(role.isDeleted.isFalse());
        } else {
            where.and(role.isDeleted.eq(request.isDeleted()));
        }
        if (request.startDate() != null) {
            where.and(role.createdAt.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            where.and(role.createdAt.loe(request.endDate()));
        }
        if (request.keyword() != null && !request.keyword().isBlank()) {
            String kw = request.keyword().strip();
            where.and(role.code.containsIgnoreCase(kw)
                .or(role.name.containsIgnoreCase(kw)));
        }
        return where;
    }

    @Override
    public List<Role> findRolesByAccountId(UUID accountId) {
        QRole role = QRole.role;
        QAccountRole accountRole = QAccountRole.accountRole;
        return queryFactory
            .selectFrom(role)
            .join(accountRole).on(accountRole.roleId.eq(role.id))
            .where(accountRole.accountId.eq(accountId))
            .fetch();
    }

}
