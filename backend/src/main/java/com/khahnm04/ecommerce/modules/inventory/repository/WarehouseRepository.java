package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.Warehouse;
import com.khahnm04.ecommerce.modules.inventory.enums.WarehouseStatus;
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
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    @Query(value = "SELECT * FROM warehouses WHERE id = :id", nativeQuery = true)
    Optional<Warehouse> findDeletedWarehouseById(@Param("id") Long id);

    @Query("SELECT COUNT(w) > 0 FROM Warehouse w WHERE w.code = :code AND w.id != :id")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

    @Query("SELECT COUNT(w) > 0 FROM Warehouse w WHERE w.name = :name AND w.id != :id")
    boolean existsByNameAndIdNot(@Param("name") String name, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Warehouse w SET w.status = :status WHERE w.id IN :ids")
    void updateStatusByIds(@Param("status") WarehouseStatus status, @Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE Warehouse w SET w.deletedAt = :now WHERE w.id IN :ids")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("ids") Set<Long> ids);

    @Query(value = "SELECT * FROM warehouses WHERE id IN :ids", nativeQuery = true)
    Set<Warehouse> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE warehouses SET deleted_at = NULL WHERE id IN :ids", nativeQuery = true)
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
