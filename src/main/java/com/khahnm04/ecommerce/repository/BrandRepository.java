package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.common.enums.BrandStatus;
import com.khahnm04.ecommerce.entity.Brand;
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
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsBySlugAndIdNot(String slug, Long id);

    Optional<Brand> findBySlugAndDeletedAtIsNull(String slug);

    Optional<Brand> findBySlugAndDeletedAtIsNotNull(String slug);

    Optional<Brand> findByIdAndDeletedAtIsNull(Long id);

    Optional<Brand> findByIdAndDeletedAtIsNotNull(Long id);

    @Modifying
    @Query("UPDATE Brand b SET b.status = :status WHERE b.id IN :ids AND b.deletedAt IS NULL")
    void updateStatusByIds(@Param("status") BrandStatus status, @Param("ids") Set<Long> ids);

    @Query("SELECT b FROM Brand b WHERE b.id IN :ids AND b.deletedAt IS NOT NULL")
    Set<Brand> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Brand b SET b.deletedAt = :now, b.deletedBy = :user WHERE b.id IN :ids AND b.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("user") User user, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Brand b SET b.deletedAt = NULL, b.deletedBy = NULL WHERE b.id IN :ids AND b.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
