package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    boolean existsByCustomerId(Long customerId);

    Optional<Order> findByCode(String code);

    Optional<Order> findByIdAndCustomerId(Long id, Long customerId);

    @Query("SELECT o FROM Order o WHERE o.id IN :ids AND o.deletedAt IS NOT NULL")
    Set<Order> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.deletedAt = :now WHERE o.id IN :ids AND o.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.deletedAt = NULL WHERE o.id IN :ids AND o.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

}
