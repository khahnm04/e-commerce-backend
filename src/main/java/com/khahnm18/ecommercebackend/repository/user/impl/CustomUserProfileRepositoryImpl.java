package com.khahnm18.ecommercebackend.repository.user.impl;

import com.khahnm18.ecommercebackend.entity.auth.QAccount;
import com.khahnm18.ecommercebackend.entity.user.QUserProfile;
import com.khahnm18.ecommercebackend.dto.request.user.UserSearchRequest;
import com.khahnm18.ecommercebackend.dto.response.user.UserProfileAccount;
import com.khahnm18.ecommercebackend.repository.user.CustomUserProfileRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomUserProfileRepositoryImpl implements CustomUserProfileRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserProfileAccount> search(UserSearchRequest request, Pageable pageable) {
        QAccount account = QAccount.account;
        QUserProfile profile = QUserProfile.userProfile;
        BooleanBuilder where = buildWhere(account, profile, request);

        List<UserProfileAccount> content = queryFactory
            .select(profile, account)
            .from(account)
            .innerJoin(profile).on(profile.accountId.eq(account.id))
            .where(where)
            .orderBy(account.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream()
            .map(tuple -> new UserProfileAccount(tuple.get(profile), tuple.get(account)))
            .toList();

        JPAQuery<Long> countQuery = queryFactory
            .select(account.count())
            .from(account)
            .innerJoin(profile).on(profile.accountId.eq(account.id))
            .where(where);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder buildWhere(QAccount account, QUserProfile profile, UserSearchRequest request) {
        BooleanBuilder where = new BooleanBuilder();
        if (request.isDeleted() == null) {
            where.and(account.isDeleted.isTrue());
        } else {
            where.and(account.isDeleted.eq(request.isDeleted()));
        }
        if (request.status() != null) {
            where.and(account.status.eq(request.status()));
        }
        if (request.startDate() != null) {
            where.and(account.createdAt.goe(request.startDate()));
        }
        if (request.endDate() != null) {
            where.and(account.createdAt.loe(request.endDate()));
        }
        if (request.keyword() != null && !request.keyword().isBlank()) {
            String kw = request.keyword().strip();
            where.and(account.email.containsIgnoreCase(kw)
                .or(account.phoneNumber.containsIgnoreCase(kw))
                .or(profile.fullName.containsIgnoreCase(kw)));
        }
        return where;
    }

}
