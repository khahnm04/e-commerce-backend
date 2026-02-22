package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.SerialNumber;
import com.khahnm04.ecommerce.modules.inventory.enums.SerialNumberStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SerialNumberRepository extends JpaRepository<SerialNumber, Long> {

        @EntityGraph(attributePaths = { "productVariant", "warehouse", "createdBy", "updatedBy" })
        Optional<SerialNumber> findWithGraphById(Long id);

        @EntityGraph(attributePaths = { "productVariant", "warehouse", "createdBy", "updatedBy" })
        Optional<SerialNumber> findBySerialCode(String serialCode);

        boolean existsBySerialCode(String serialCode);

        @EntityGraph(attributePaths = { "productVariant", "warehouse" })
        List<SerialNumber> findByWarehouseId(Long warehouseId);

        @EntityGraph(attributePaths = { "productVariant", "warehouse" })
        List<SerialNumber> findByProductVariantId(Long productVariantId);

        @EntityGraph(attributePaths = { "productVariant" })
        List<SerialNumber> findByStatusAndWarehouseId(SerialNumberStatus status, Long warehouseId);

        List<SerialNumber> findByStatusAndProductVariantId(SerialNumberStatus status, Long productVariantId);

        @EntityGraph(attributePaths = { "productVariant", "warehouse" })
        @Query("SELECT sn FROM SerialNumber sn WHERE sn.status = :status " +
                        "AND (:warehouseId IS NULL OR sn.warehouse.id = :warehouseId) " +
                        "AND (:productVariantId IS NULL OR sn.productVariant.id = :productVariantId)")
        List<SerialNumber> findAvailableSerials(
                        @Param("status") SerialNumberStatus status,
                        @Param("warehouseId") Long warehouseId,
                        @Param("productVariantId") Long productVariantId);

        @Modifying
        @Transactional
        @Query("UPDATE SerialNumber sn SET sn.status = :status WHERE sn.id = :id")
        void updateStatusById(@Param("id") Long id, @Param("status") SerialNumberStatus status);

}
