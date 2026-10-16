package com.khahnm18.ecommercebackend.repository.user;

import com.khahnm18.ecommercebackend.entity.user.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile, UUID> {

    boolean existsByAccountIdAndIsDeletedFalse(UUID accountId);

    Optional<AdminProfile> findByAccountIdAndIsDeletedFalse(UUID accountId);

    List<AdminProfile> findAllByAccountIdIn(List<UUID> accountIds);

}
