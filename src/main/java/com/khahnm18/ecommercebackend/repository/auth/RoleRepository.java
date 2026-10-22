package com.khahnm18.ecommercebackend.repository.auth;

import com.khahnm18.ecommercebackend.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID>, CustomRoleRepository {

    Optional<Role> findByIdAndIsDeletedFalse(UUID id);
    Optional<Role> findByIdAndIsDeletedTrue(UUID id);
    Optional<Role> findByCodeAndIsDeletedFalse(String code);

}
