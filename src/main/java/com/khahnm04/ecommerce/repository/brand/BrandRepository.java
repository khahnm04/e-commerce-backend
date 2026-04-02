package com.khahnm04.ecommerce.repository.brand;

import com.khahnm04.ecommerce.common.enums.BrandStatus;
import com.khahnm04.ecommerce.entity.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    Optional<Brand> findBySlugAndDeletedAtIsNull(String slug);

    Optional<Brand> findByIdAndDeletedAtIsNull(Long id);

    Optional<Brand> findByIdAndDeletedAtIsNotNull(Long id);

    Optional<Brand> findBySlugAndDeletedAtIsNotNull(String slug);

    @Query("SELECT COUNT(b) > 0 FROM Brand b WHERE UPPER(b.name) = UPPER(:name) AND b.id != :id")
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    @Query("SELECT COUNT(b) > 0 FROM Brand b WHERE UPPER(b.slug) = UPPER(:slug) AND b.id != :id")
    boolean existsBySlugIgnoreCaseAndIdNot(String slug, Long id);

    @Query("SELECT b FROM Brand b WHERE b.id IN :ids AND b.deletedAt IS NOT NULL")
    Set<Brand> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Brand b SET b.deletedAt = :now WHERE b.id IN :ids AND b.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Brand b SET b.deletedAt = NULL WHERE b.id IN :ids AND b.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Brand b SET b.status = :status WHERE b.id IN :ids")
    void updateStatusByIds(BrandStatus status, Set<Long> ids);

}
