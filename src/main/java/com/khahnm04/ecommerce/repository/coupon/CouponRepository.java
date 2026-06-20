package com.khahnm04.ecommerce.repository.coupon;

import com.khahnm04.ecommerce.constant.coupon.CouponStatus;
import com.khahnm04.ecommerce.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    boolean existsByCodeAndIsDeletedFalse(String code);
    boolean existsByCodeAndIdNotAndIsDeletedFalse(String code, Long id);

    Optional<Coupon> findByIdAndIsDeletedFalse(Long id);
    Optional<Coupon> findByIdAndIsDeletedTrue(Long id);

    @Modifying
    @Query("""
        UPDATE Coupon c
        SET c.status = :status
        WHERE c.id IN :ids
        AND c.isDeleted = false
    """)
    void updateStatusByIds(@Param("status") CouponStatus status,
                           @Param("ids") Set<Long> ids);

    @Modifying
    @Query("""
        UPDATE Coupon c
        SET c.isDeleted = true,
            c.deletedAt = CURRENT_TIMESTAMP,
            c.deletedBy = :user
        WHERE c.id IN :couponIds
        AND c.isDeleted = false
    """)
    void softDeleteAllByIds(
        @Param("couponIds") Set<Long> couponIds,
        @Param("user") String user
    );

    @Modifying
    @Query("""
        UPDATE Coupon c
        SET c.isDeleted = false,
            c.deletedAt = null,
            c.deletedBy = null
        WHERE c.id IN :couponIds
        AND c.isDeleted = true
    """)
    void restoreAllByIds(@Param("couponIds") Set<Long> couponIds);

    @Modifying
    @Query("""
        DELETE FROM Coupon c
        WHERE c.id IN :couponIds
        AND c.isDeleted = true
    """)
    void deleteAllByIds(@Param("couponIds") Set<Long> couponIds);

}
