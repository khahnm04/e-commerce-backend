package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.Variant;
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
public interface VariantRepository extends JpaRepository<Variant, Long>, JpaSpecificationExecutor<Variant> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    Optional<Variant> findByCodeAndDeletedAtIsNull(String code);

    Optional<Variant> findByCodeAndDeletedAtIsNotNull(String code);

    Optional<Variant> findByIdAndDeletedAtIsNull(Long id);

    Optional<Variant> findByIdAndDeletedAtIsNotNull(Long id);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByCodeAndIdNot(String code, Long id);

    @Query("SELECT v FROM Variant v WHERE v.id IN :ids AND v.deletedAt IS NOT NULL")
    Set<Variant> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Variant v SET v.deletedAt = :now WHERE v.id IN :ids AND v.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Variant v SET v.deletedAt = NULL WHERE v.id IN :ids AND v.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

}
