package com.khahnm04.ecommerce.repository.auth;

import com.khahnm04.ecommerce.entity.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("SELECT p.code FROM Permission p WHERE p.deletedAt IS NULL")
    List<String> findAllPermissionCodes();

    // Application Init
    Optional<Permission> findByCodeAndDeletedAtIsNull(String code);

}
