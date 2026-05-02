package com.khahnm04.ecommerce.repository.user;

import com.khahnm04.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("""
        SELECT a FROM Address a
        WHERE a.customer.id = :customerId
        ORDER BY a.isDefault DESC, a.createdAt DESC
    """)
    List<Address> findAllByCustomerId(@Param("customerId") Long customerId);

    Optional<Address> findByIdAndCustomerId(Long id, Long customerId);

    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.customer.id = :customerId")
    void clearDefaultAddressByCustomerId(Long customerId);
    
    Optional<Address> findByCustomerIdAndIsDefaultTrue(Long customerId);

    int countByCustomerId(Long customerId);

}
