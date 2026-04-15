package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.common.enums.CouponStatus;
import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    Optional<Coupon> findByIdAndDeletedAtIsNull(Long id);

    Optional<Coupon> findByIdAndDeletedAtIsNotNull(Long id);

    Optional<Coupon> findByCodeAndDeletedAtIsNull(String code);

    @Modifying
    @Transactional
    @Query("UPDATE Coupon c SET c.deletedAt = :now, c.deletedBy = :user WHERE c.id IN :ids AND c.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("user") User user, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Coupon c SET c.deletedAt = NULL, c.deletedBy = NULL WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Coupon c SET c.status = :status WHERE c.id IN :ids")
    void updateStatusByIds(@Param("status") CouponStatus status, @Param("ids") Set<Long> ids);

    @Query("SELECT c FROM Coupon c WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    Set<Coupon> findAllDeletedByIds(@Param("ids") Set<Long> ids);

}
