package com.khahnm18.ecommercebackend.repository.user;

import com.khahnm18.ecommercebackend.entity.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    boolean existsByAccountIdAndIsDeletedFalse(UUID accountId);

    Optional<UserProfile> findByAccountIdAndIsDeletedFalse(UUID accountId);

    List<UserProfile> findAllByAccountIdIn(List<UUID> accountIds);

}
