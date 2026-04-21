package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    boolean existsByProductVariantId(Long id);

    boolean existsByProductVariantIdIn(java.util.Set<Long> ids);

    @Query("SELECT od FROM OrderDetail od WHERE od.order.id = :orderId")
    List<OrderDetail> findByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT od FROM OrderDetail od WHERE od.order.id IN :orderIds")
    List<OrderDetail> findByOrderIdIn(@Param("orderIds") List<Long> orderIds);

}
