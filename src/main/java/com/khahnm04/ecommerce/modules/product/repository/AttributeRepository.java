package com.khahnm04.ecommerce.modules.product.repository;

import com.khahnm04.ecommerce.modules.product.entity.Attribute;
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
public interface AttributeRepository extends JpaRepository<Attribute, Long>, JpaSpecificationExecutor<Attribute> {

    boolean existsByName(String name);

    Optional<Attribute> findByIdAndDeletedAtIsNull(Long id);

    Optional<Attribute> findByIdAndDeletedAtIsNotNull(Long id);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT a FROM Attribute a WHERE a.id IN :ids AND a.deletedAt IS NOT NULL")
    Set<Attribute> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Attribute a SET a.deletedAt = :now WHERE a.id IN :ids AND a.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Attribute a SET a.deletedAt = NULL WHERE a.id IN :ids AND a.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

}
