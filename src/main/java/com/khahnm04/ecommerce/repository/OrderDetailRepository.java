package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    boolean existsByProductVariantId(Long id);

    boolean existsByProductVariantIdIn(java.util.Set<Long> ids);

    List<OrderDetail> findByOrderId(Long orderId);

    List<OrderDetail> findByOrderIdIn(List<Long> orderIds);

}
