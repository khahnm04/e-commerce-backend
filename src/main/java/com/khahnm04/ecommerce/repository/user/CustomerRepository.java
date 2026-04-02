package com.khahnm04.ecommerce.repository.user;

import com.khahnm04.ecommerce.entity.user.Customer;
import org.springframework.data.jpa.repository.*;
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
    Set<Customer> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.deletedAt = :now WHERE c.id IN :ids AND c.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.deletedAt = NULL WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

}
