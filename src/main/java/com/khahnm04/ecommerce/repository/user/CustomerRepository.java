package com.khahnm04.ecommerce.repository.user;

import com.khahnm04.ecommerce.entity.Customer;
import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.common.enums.CustomerStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    @Query("SELECT c FROM Customer c WHERE c.email = :identifier OR c.phoneNumber = :identifier")
    Optional<Customer> findByIdentifier(@Param("identifier") String identifier);

    Optional<Customer> findByIdAndDeletedAtIsNull(Long id);

    Optional<Customer> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT c FROM Customer c WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    Set<Customer> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Customer c SET c.deletedAt = :now, c.deletedBy = :employee WHERE c.id IN :ids AND c.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("employee") Employee employee, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Customer c SET c.deletedAt = NULL, c.deletedBy = NULL WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.status = :status WHERE c.id IN :ids")
    void updateStatusByIds(@Param("status") CustomerStatus status, @Param("ids") Set<Long> ids);

    Long countByDeletedAtIsNull();

}
