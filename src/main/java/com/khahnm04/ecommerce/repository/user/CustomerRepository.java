package com.khahnm04.ecommerce.repository.user;

import com.khahnm04.ecommerce.entity.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByIdAndDeletedAtIsNull(Long id);

    Optional<Customer> findByIdAndDeletedAtIsNotNull(Long id);

    Optional <Customer> findByEmail(String email);

    @Modifying
    @Query("""
        UPDATE Customer c
        SET c.deletedAt = CURRENT_TIMESTAMP
        WHERE c.id IN :customerIds
        AND c.deletedAt IS NULL
    """)
    void softDeleteByIds(@Param("customerIds") Set<Long> customerIds);

    @Modifying
    @Query("""
        UPDATE Customer c
        SET c.deletedAt = null
        WHERE c.id IN :customerIds
        AND c.deletedAt IS NOT NULL
    """)
    void restoreByIds(@Param("customerIds") Set<Long> customerIds);

    @Modifying
    @Query("DELETE FROM Customer c WHERE c.id IN :ids")
    void deleteAllByIds(Set<Long> ids);

}
