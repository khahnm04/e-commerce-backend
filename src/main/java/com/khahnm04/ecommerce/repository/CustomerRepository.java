package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    @EntityGraph(attributePaths = { "user" })
    Optional<Customer> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = { "user" })
    Optional<Customer> findByIdAndDeletedAtIsNotNull(Long id);

    Optional<Customer> findByUser_PhoneNumber(String phoneNumber);

    @Query("SELECT c FROM Customer c WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    Set<Customer> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.deletedAt = :now, c.deletedBy = :user WHERE c.id IN :ids AND c.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("user") User user, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.deletedAt = NULL, c.deletedBy = NULL WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
