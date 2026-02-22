package com.khahnm04.ecommerce.modules.user.repository;

import com.khahnm04.ecommerce.modules.user.entity.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @EntityGraph(attributePaths = { "user" })
    Optional<Customer> findById(Long id);

    Optional<Customer> findByUser_PhoneNumber(String phoneNumber);

    @Query(value = "SELECT * FROM customers WHERE id = :id", nativeQuery = true)
    Optional<Customer> findDeletedCustomerById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.deletedAt = :now WHERE c.id IN :ids")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Query(value = "SELECT * FROM customers WHERE id IN :ids", nativeQuery = true)
    Set<Customer> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE customers SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(Set<Long> ids);

}
