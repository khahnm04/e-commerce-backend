package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.common.enums.UserStatus;
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

    boolean existsByRoles_Id(@Param("roleId") Long roleId);

    boolean existsByRoles_IdIn(@Param("roleIds") Set<Long> roleIds);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.status = :status WHERE u.id IN :ids")
    void updateStatusByIds(@Param("status") UserStatus status, @Param("ids") Set<Long> ids);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id IN :ids")
    List<User> findAllByIdInWithRoles(@Param("ids") Set<Long> ids);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permissions WHERE u.id = :id")
    Optional<User> findByIdWithRolesAndPermissions(@Param("id") Long id);

}
