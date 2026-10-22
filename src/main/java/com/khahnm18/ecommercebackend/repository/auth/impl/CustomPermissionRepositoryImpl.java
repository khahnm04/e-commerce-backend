package com.khahnm18.ecommercebackend.repository.auth.impl;

import com.khahnm18.ecommercebackend.repository.auth.CustomPermissionRepository;
import com.khahnm18.ecommercebackend.entity.auth.Permission;
import com.khahnm18.ecommercebackend.entity.auth.QAccountRole;
import com.khahnm18.ecommercebackend.entity.auth.QPermission;
import com.khahnm18.ecommercebackend.entity.auth.QRolePermission;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomPermissionRepositoryImpl implements CustomPermissionRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Permission> findAllPermissionByAccountId(UUID accountId) {
        QAccountRole accountRole = QAccountRole.accountRole;
        QRolePermission rolePermission = QRolePermission.rolePermission;
        QPermission permission = QPermission.permission;
        return queryFactory
            .selectFrom(permission)
            .join(rolePermission).on(rolePermission.permissionId.eq(permission.id))
            .join(accountRole).on(accountRole.roleId.eq(rolePermission.roleId))
            .where(accountRole.accountId.eq(accountId))
            .distinct()
            .fetch();
    }

    @Override
    public List<Permission> findPermissionsByRoleId(UUID roleId) {
        QPermission permission = QPermission.permission;
        QRolePermission rolePermission = QRolePermission.rolePermission;
        return queryFactory
            .selectFrom(permission)
            .join(rolePermission).on(rolePermission.permissionId.eq(permission.id))
            .where(rolePermission.roleId.eq(roleId))
            .fetch();
    }

}
