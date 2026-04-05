package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.VariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VariantValueRepository extends JpaRepository<VariantValue, Long>, JpaSpecificationExecutor<VariantValue> {

    Optional<VariantValue> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT vv FROM VariantValue vv WHERE vv.id = :id AND vv.deletedAt IS NOT NULL")
    Optional<VariantValue> findByIdAndDeletedAtIsNotNull(Long id);

    boolean existsByVariantIdAndValue(Long variantId, String value);

    boolean existsByVariantIdAndValueAndIdNot(Long variantId, String value, Long id);

    @Query("SELECT vv FROM VariantValue vv WHERE vv.id IN :ids AND vv.deletedAt IS NOT NULL")
    List<VariantValue> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Query("UPDATE VariantValue vv SET vv.deletedAt = :deletedAt WHERE vv.id IN :ids")
    void softDeleteByIds(LocalDateTime deletedAt, Set<Long> ids);

    @Modifying
    @Query("UPDATE VariantValue vv SET vv.deletedAt = NULL WHERE vv.id IN :ids")
    void restoreDeletedByIds(Set<Long> ids);
}
