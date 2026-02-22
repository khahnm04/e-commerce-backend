package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.TransferOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TransferOrderRepository extends JpaRepository<TransferOrder, Long> {

    @EntityGraph(attributePaths = { "fromWarehouse", "toWarehouse", "createdBy", "updatedBy" })
    Optional<TransferOrder> findWithGraphById(Long id);

    boolean existsByCode(String code);

    @Query("SELECT COUNT(t) > 0 FROM TransferOrder t WHERE t.code = :code AND t.id != :id")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);

}
