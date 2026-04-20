package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.common.enums.BannerStatus;
import com.khahnm04.ecommerce.entity.Banner;
import com.khahnm04.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long>, JpaSpecificationExecutor<Banner> {

    Optional<Banner> findByIdAndDeletedAtIsNull(Long id);

    Optional<Banner> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT b FROM Banner b WHERE b.id IN :ids AND b.deletedAt IS NOT NULL")
    Set<Banner> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Banner b SET b.status = :status WHERE b.id IN :ids AND b.deletedAt IS NULL")
    void updateStatusByIds(@Param("status") BannerStatus status, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Banner b SET b.deletedAt = :now, b.deletedBy = :user WHERE b.id IN :ids AND b.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("user") User user, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Banner b SET b.deletedAt = NULL, b.deletedBy = NULL WHERE b.id IN :ids AND b.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
