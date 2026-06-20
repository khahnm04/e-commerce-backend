package com.khahnm04.ecommerce.repository.category;

import com.khahnm04.ecommerce.constant.category.CategoryStatus;
import com.khahnm04.ecommerce.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    boolean existsBySlugAndIsDeletedFalse(String slug);
    boolean existsBySlugAndIdNotAndIsDeletedFalse(String slug, Long id);

    boolean existsByParentIdAndIsDeletedFalse(Long parentId);

    Optional<Category> findByIdAndIsDeletedFalse(Long id);
    Optional<Category> findByIdAndIsDeletedTrue(Long id);

    List<Category> findByParentIdAndIsDeletedFalse(Long id);

    @Query("""
        SELECT CASE WHEN EXISTS (
            SELECT 1
            FROM Category c
            WHERE c.parentId IN :parentIds
        ) THEN true ELSE false END
    """)
    boolean existsChildrenByParentIds(@Param("parentIds") Set<Long> parentIds);

    @Modifying
    @Query("""
        UPDATE Category c
        SET c.status = :status
        WHERE c.id IN :ids
        AND c.isDeleted = false
    """)
    void updateStatusByIds(@Param("status") CategoryStatus status,
                           @Param("ids") Set<Long> ids);

    @Modifying
    @Query("""
        UPDATE Category c
        SET c.isDeleted = true,
            c.deletedAt = CURRENT_TIMESTAMP,
            c.deletedBy = :user
        WHERE c.id IN :categoryIds
        AND c.isDeleted = false
    """)
    void softDeleteAllByIds(
        @Param("categoryIds") Set<Long> categoryIds,
        @Param("user") String user
    );

    @Modifying
    @Query("""
        UPDATE Category c
        SET c.isDeleted = false,
            c.deletedAt = null,
            c.deletedBy = null
        WHERE c.id IN :categoryIds
        AND c.isDeleted = true
    """)
    void restoreAllByIds(@Param("categoryIds") Set<Long> categoryIds);

    @Modifying
    @Query("""
        DELETE FROM Category c
        WHERE c.id IN :categoryIds
        AND c.isDeleted = true
    """)
    void deleteAllByIds(@Param("categoryIds") Set<Long> categoryIds);

}
