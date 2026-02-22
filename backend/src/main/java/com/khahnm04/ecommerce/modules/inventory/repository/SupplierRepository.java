package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.Supplier;
import com.khahnm04.ecommerce.modules.inventory.enums.SupplierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM suppliers WHERE id = :id", nativeQuery = true)
    Optional<Supplier> findDeletedSupplierById(@Param("id") Long id);

    @Query("SELECT COUNT(s) > 0 FROM Supplier s WHERE s.code = :code AND s.id != :id")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(s) > 0 FROM Supplier s WHERE s.name = :name AND s.id != :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Supplier s SET s.status = :status WHERE s.id IN :ids")
    void updateStatusByIds(@Param("status") SupplierStatus status, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Supplier s SET s.deletedAt = :now WHERE s.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Query(value = "SELECT * FROM suppliers WHERE id IN :ids", nativeQuery = true)
    Set<Supplier> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE suppliers SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
