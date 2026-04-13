package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.common.enums.CategoryStatus;
import com.khahnm04.ecommerce.entity.Category;
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
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    boolean existsByParentId(Long parentId);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    Optional<Category> findBySlugAndDeletedAtIsNull(String slug);

    Optional<Category> findBySlugAndDeletedAtIsNotNull(String slug);

    Optional<Category> findByIdAndDeletedAtIsNull(Long id);

    Optional<Category> findByIdAndDeletedAtIsNotNull(Long id);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.deletedAt = :now WHERE c.id IN :ids AND c.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.deletedAt = NULL WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.status = :status WHERE c.id IN :ids AND c.deletedAt IS NULL")
    void updateStatusByIds(@Param("status") CategoryStatus status, @Param("ids") Set<Long> ids);

    @Query("SELECT c FROM Category c WHERE c.id IN :ids AND c.deletedAt IS NOT NULL")
    Set<Category> findAllDeletedByIds(@Param("ids") Set<Long> ids);

}
