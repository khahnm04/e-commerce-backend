package com.khahnm04.ecommerce.modules.catalog.repository;

import com.khahnm04.ecommerce.modules.catalog.entity.VariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface VariantValueRepository
        extends JpaRepository<VariantValue, Long>, JpaSpecificationExecutor<VariantValue> {

    List<VariantValue> findAllByVariantIdOrderByDisplayOrderAsc(Long variantId);

    @Query("SELECT vv FROM VariantValue vv WHERE vv.variant.id IN :variantIds ORDER BY vv.displayOrder ASC")
    List<VariantValue> findAllByVariantIdIn(@Param("variantIds") List<Long> variantIds);

    @Modifying
    @Transactional
    @Query("DELETE FROM VariantValue vv WHERE vv.variant.id = :variantId")
    void deleteAllByVariantId(@Param("variantId") Long variantId);

    @Modifying
    @Transactional
    @Query("DELETE FROM VariantValue vv WHERE vv.variant.id = :variantId AND vv.id NOT IN :ids")
    void deleteByVariantIdAndIdNotIn(@Param("variantId") Long variantId, @Param("ids") Set<Long> ids);

    Optional<VariantValue> findByIdAndDeletedAtIsNull(Long id);

    Optional<VariantValue> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT vv FROM VariantValue vv WHERE vv.id IN :ids AND vv.deletedAt IS NOT NULL")
    Set<VariantValue> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE VariantValue vv SET vv.deletedAt = :now WHERE vv.id IN :ids AND vv.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE VariantValue vv SET vv.deletedAt = NULL WHERE vv.id IN :ids AND vv.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

}
