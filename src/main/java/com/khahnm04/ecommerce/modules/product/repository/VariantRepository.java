package com.khahnm04.ecommerce.modules.product.repository;

import com.khahnm04.ecommerce.modules.product.entity.Variant;
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
public interface VariantRepository extends JpaRepository<Variant, Long>, JpaSpecificationExecutor<Variant> {

    Optional<Variant> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT v FROM Variant v WHERE v.id = :id AND v.deletedAt IS NOT NULL")
    Optional<Variant> findByIdAndDeletedAtIsNotNull(Long id);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT v FROM Variant v WHERE v.id IN :ids AND v.deletedAt IS NOT NULL")
    List<Variant> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Query("UPDATE Variant v SET v.deletedAt = :deletedAt WHERE v.id IN :ids")
    void softDeleteByIds(LocalDateTime deletedAt, Set<Long> ids);

    @Modifying
    @Query("UPDATE Variant v SET v.deletedAt = NULL WHERE v.id IN :ids")
    void restoreDeletedByIds(Set<Long> ids);
}
