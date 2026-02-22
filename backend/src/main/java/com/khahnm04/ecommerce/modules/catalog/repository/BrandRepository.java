package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.Brand;
import com.khahnm04.ecommerce.modules.catalog.enums.BrandStatus;
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
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    Optional<Brand> findBySlug(String slug);

    @Query(value = "SELECT * FROM brands WHERE id = :id", nativeQuery = true)
    Optional<Brand> findDeletedBrandById(@Param("id") Long id);

    @Query("SELECT COUNT(b) > 0 FROM Brand b WHERE UPPER(b.name) = UPPER(:name) AND b.id != :id")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Query("SELECT COUNT(b) > 0 FROM Brand b WHERE UPPER(b.slug) = UPPER(:slug) AND b.id != :id")
    boolean existsBySlugIgnoreCaseAndIdNot(@Param("slug") String slug, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Brand b SET b.status = :status WHERE b.id IN :ids")
    void updateStatusByIds(@Param("status") BrandStatus status, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Brand b SET b.deletedAt = :now WHERE b.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Query(value = "SELECT * FROM brands WHERE id IN :ids", nativeQuery = true)
    Set<Brand> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE brands SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
