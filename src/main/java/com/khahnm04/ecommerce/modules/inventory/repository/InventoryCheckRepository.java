package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.InventoryCheck;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryCheckRepository extends JpaRepository<InventoryCheck, Long> {

    @EntityGraph(attributePaths = { "warehouse" })
    Optional<InventoryCheck> findWithGraphById(Long id);

    boolean existsByCode(String code);

    @Query("SELECT COUNT(ic) > 0 FROM InventoryCheck ic WHERE ic.code = :code AND ic.id != :id")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

}
