package com.khahnm04.ecommerce.modules.auth.repository;

import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.phoneNumber = :identifier")
    Optional<User> findByIdentifier(@Param("identifier") String identifier);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status WHERE u.id IN :ids")
    void updateStatusByIds(@Param("status") UserStatus status, @Param("ids") Set<Long> ids);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id IN :ids")
    List<User> findAllByIdInWithRoles(@Param("ids") Set<Long> ids);

}
