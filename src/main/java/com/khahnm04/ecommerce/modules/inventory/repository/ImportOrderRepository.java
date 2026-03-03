package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.ImportOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ImportOrderRepository extends JpaRepository<ImportOrder, Long> {

    @EntityGraph(attributePaths = { "warehouse", "supplier", "createdBy", "updatedBy" })
    Optional<ImportOrder> findWithGraphById(Long id);

    boolean existsByCode(String code);

    @Query("SELECT COUNT(io) > 0 FROM ImportOrder io WHERE io.code = :code AND io.id != :id")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

    @Query(value = "SELECT * FROM import_orders WHERE id = :id", nativeQuery = true)
    Optional<ImportOrder> findDeletedById(@Param("id") Long id);

}
