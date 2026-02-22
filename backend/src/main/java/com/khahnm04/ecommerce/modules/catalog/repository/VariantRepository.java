package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.Variant;
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
public interface VariantRepository extends JpaRepository<Variant, Long>, JpaSpecificationExecutor<Variant> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    Optional<Variant> findByCode(String code);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Query(value = "SELECT * FROM variants WHERE id = :id", nativeQuery = true)
    Optional<Variant> findDeletedVariantById(@Param("id") Long id);

    @Query(value = "SELECT * FROM variants WHERE id IN :ids", nativeQuery = true)
    Set<Variant> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Variant v SET v.deletedAt = :now WHERE v.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE variants SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
