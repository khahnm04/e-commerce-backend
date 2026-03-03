package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.Attribute;
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
public interface AttributeRepository extends JpaRepository<Attribute, Long>, JpaSpecificationExecutor<Attribute> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    Optional<Attribute> findByCode(String code);

    @Query(value = "SELECT * FROM attributes WHERE id = :id", nativeQuery = true)
    Optional<Attribute> findDeletedAttributeById(@Param("id") Long id);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Attribute a SET a.deletedAt = :now WHERE a.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Query(value = "SELECT * FROM attributes WHERE id IN :ids", nativeQuery = true)
    Set<Attribute> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE attributes SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
