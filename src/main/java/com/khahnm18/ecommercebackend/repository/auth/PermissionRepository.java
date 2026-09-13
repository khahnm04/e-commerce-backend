package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    @Query("SELECT p.code FROM Permission p")
    List<String> findAllPermissionCodes();

}
