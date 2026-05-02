package com.khahnm04.ecommerce.repository.product;

import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.entity.VariantValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface VariantValueRepository
        extends JpaRepository<VariantValue, Long>, JpaSpecificationExecutor<VariantValue> {

    boolean existsByVariantId(Long variantId);

    boolean existsByVariantIdIn(Set<Long> variantIds);

    boolean existsByVariantIdAndValueAndDeletedAtIsNull(Long variantId, String value);

    boolean existsByVariantIdAndValueAndDeletedAtIsNotNull(Long variantId, String value);

    boolean existsByVariantIdAndValueAndIdNot(Long variantId, String value, Long id);

    Optional<VariantValue> findByIdAndDeletedAtIsNull(Long id);

    Optional<VariantValue> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT vv FROM VariantValue vv WHERE vv.id IN :ids AND vv.deletedAt IS NOT NULL")
    List<VariantValue> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE VariantValue vv SET vv.deletedAt = :now, vv.deletedBy = :employee WHERE vv.id IN :ids AND vv.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("employee") Employee employee, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE VariantValue vv SET vv.deletedAt = NULL, vv.deletedBy = NULL WHERE vv.id IN :ids AND vv.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
