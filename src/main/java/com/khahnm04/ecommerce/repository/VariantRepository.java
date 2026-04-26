package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.Employee;
import com.khahnm04.ecommerce.entity.Variant;
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
public interface VariantRepository extends JpaRepository<Variant, Long>, JpaSpecificationExecutor<Variant> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<Variant> findByIdAndDeletedAtIsNull(Long id);

    Optional<Variant> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT v FROM Variant v WHERE v.id IN :ids AND v.deletedAt IS NOT NULL")
    List<Variant> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Variant v SET v.deletedAt = :now, v.deletedBy = :employee WHERE v.id IN :ids AND v.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("employee") Employee employee, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Variant v SET v.deletedAt = NULL, v.deletedBy = NULL WHERE v.id IN :ids AND v.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
