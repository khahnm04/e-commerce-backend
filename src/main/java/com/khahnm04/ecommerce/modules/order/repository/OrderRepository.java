package com.khahnm04.ecommerce.modules.order.repository;

import com.khahnm04.ecommerce.modules.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = { "customer", "branch" })
    Optional<Order> findByIdAndDeletedAtIsNull(Long id);

    @EntityGraph(attributePaths = { "customer", "branch" })
    Optional<Order> findByIdAndCustomerIdAndDeletedAtIsNull(Long id, Long customerId);

}
