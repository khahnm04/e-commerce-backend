package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import com.khahnm04.ecommerce.entity.Category;
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
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    boolean existsByParentId(Long parentId);

    boolean existsByParentIdIn(Set<Long> parentIds);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsBySlugAndIdNot(String slug, Long id);

    Optional<Category> findBySlugAndDeletedAtIsNull(String slug);

    Optional<Category> findBySlugAndDeletedAtIsNotNull(String slug);

    Optional<Category> findByIdAndDeletedAtIsNull(Long id);

    Optional<Category> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT c FROM Category c WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    Set<Category> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Category c SET c.deletedAt = :now, c.deletedBy = :user WHERE c.id IN :ids AND c.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("user") User user, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Category c SET c.deletedAt = NULL, c.deletedBy = NULL WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Category c SET c.status = :status WHERE c.id IN :ids AND c.deletedAt IS NULL")
    void updateStatusByIds(@Param("status") CategoryStatus status, @Param("ids") Set<Long> ids);

}
