package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

  boolean existsByCouponId(Long couponId);

  boolean existsByCouponIdIn(java.util.Set<Long> couponIds);

}
