package com.khahnm04.ecommerce.specification.user;

import com.khahnm04.ecommerce.constant.user.AccountAdminStatus;
import com.khahnm04.ecommerce.constant.user.Gender;
import com.khahnm04.ecommerce.dto.request.user.AccountAdminFilterRequest;
import com.khahnm04.ecommerce.entity.user.AccountAdmin;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class AccountAdminSpecification {

    private static final String FIELD_IS_DELETED = "isDeleted";
    private static final String FIELD_CREATED_AT = "createdAt";
    private static final String FIELD_GENDER = "gender";
    private static final String FIELD_STATUS = "status";
    private static final String[] KEYWORD_FIELDS = {"code", "fullName", "email", "jobTitle"};

    private AccountAdminSpecification() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<AccountAdmin> buildQuery(AccountAdminFilterRequest request, boolean isDeleted) {
        return byIsDeleted(isDeleted)
            .and(byKeyword(request.keyword()))
            .and(byStartDate(request.startDate()))
            .and(byEndDate(request.endDate()))
            .and(byGender(request.gender()))
            .and(byStatus(request.status()));
    }

    private static Specification<AccountAdmin> byIsDeleted(boolean isDeleted) {
        return (root, query, cb) -> isDeleted
            ? cb.isNotNull(root.get(FIELD_IS_DELETED))
            : cb.isNull(root.get(FIELD_IS_DELETED));
    }

    private static Specification<AccountAdmin> byKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        return (root, query, cb) -> {
            String pattern = "%" + keyword.strip().toLowerCase() + "%";
            List<Predicate> likes = new ArrayList<>(KEYWORD_FIELDS.length);
            for (String field : KEYWORD_FIELDS) {
                likes.add(cb.like(cb.lower(root.get(field)), pattern));
            }
            return cb.or(likes.toArray(Predicate[]::new));
        };
    }

    private static Specification<AccountAdmin> byStartDate(Instant startDate) {
        if (startDate == null) return null;
        return (root, query, cb) ->
            cb.greaterThanOrEqualTo(root.get(FIELD_CREATED_AT), startDate);
    }

    private static Specification<AccountAdmin> byEndDate(Instant endDate) {
        if (endDate == null) return null;
        return (root, query, cb) ->
            cb.lessThanOrEqualTo(root.get(FIELD_CREATED_AT), endDate);
    }

    private static Specification<AccountAdmin> byStatus(AccountAdminStatus status) {
        if (status == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_STATUS), status);
    }

    private static Specification<AccountAdmin> byGender(Gender gender) {
        if (gender == null) return null;
        return (root, query, cb) ->
            cb.equal(root.get(FIELD_GENDER), gender);
    }

}
