package com.khahnm04.ecommerce.repository.brand;

import com.khahnm04.ecommerce.constant.brand.BrandStatus;
import com.khahnm04.ecommerce.entity.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {

    boolean existsBySlugAndIsDeletedFalse(String slug);
    boolean existsBySlugAndIdNotAndIsDeletedFalse(String slug, Long id);

    Optional<Brand> findByIdAndIsDeletedFalse(Long id);
    Optional<Brand> findByIdAndIsDeletedTrue(Long id);

    @Modifying
    @Query("""
        UPDATE Brand b
        SET b.status = :status
        WHERE b.id IN :ids
        AND b.isDeleted = false
    """)
    void updateStatusByIds(@Param("status") BrandStatus status,
                           @Param("ids") Set<Long> ids);

    @Modifying
    @Query("""
        UPDATE Brand b
        SET b.isDeleted = true,
            b.deletedAt = CURRENT_TIMESTAMP,
            b.deletedBy = :user
        WHERE b.id IN :brandIds
        AND b.isDeleted = false
    """)
    void softDeleteAllByIds(
        @Param("brandIds") Set<Long> brandIds,
        @Param("user") String user
    );

    @Modifying
    @Query("""
        UPDATE Brand b
        SET b.isDeleted = false,
            b.deletedAt = null,
            b.deletedBy = null
        WHERE b.id IN :brandIds
        AND b.isDeleted = true
    """)
    void restoreAllByIds(@Param("brandIds") Set<Long> brandIds);

    @Modifying
    @Query("""
        DELETE FROM Brand b
        WHERE b.id IN :brandIds
        AND b.isDeleted = true
    """)
    void deleteAllByIds(@Param("brandIds") Set<Long> brandIds);

}
