package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.Product;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductStatus;
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
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    boolean existsByCode(String code);

    Optional<Product> findBySlugAndDeletedAtIsNull(String slug);

    Optional<Product> findBySlugAndDeletedAtIsNotNull(String slug);

    Optional<Product> findByIdAndDeletedAtIsNull(Long id);

    Optional<Product> findByIdAndDeletedAtIsNotNull(Long id);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.status = :status WHERE p.id IN :ids")
    void updateStatusByIds(ProductStatus status, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.deletedAt = :now WHERE p.id IN :ids AND p.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids AND p.deletedAt IS NOT NULL")
    Set<Product> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.deletedAt = NULL WHERE p.id IN :ids AND p.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

}
