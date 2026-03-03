package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.Product;
import com.khahnm04.ecommerce.modules.catalog.enums.ProductStatus;
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
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    boolean existsByProductCode(String productCode);

    Optional<Product> findBySlug(String slug);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsByProductCodeAndIdNot(String productCode, Long id);

    @Query(value = "SELECT * FROM products WHERE id = :id", nativeQuery = true)
    Optional<Product> findDeletedProductById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.status = :status WHERE p.id IN :ids")
    void updateStatusByIds(@Param("status") ProductStatus status, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.deletedAt = :now WHERE p.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Query(value = "SELECT * FROM products WHERE id IN :ids", nativeQuery = true)
    Set<Product> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
