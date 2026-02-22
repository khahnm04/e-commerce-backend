package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.TransferDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransferDetailRepository extends JpaRepository<TransferDetail, Long> {

    @EntityGraph(attributePaths = { "serialNumber", "serialNumber.warehouse" })
    List<TransferDetail> findByTransferOrderId(Long transferOrderId);

    void deleteByTransferOrderId(Long transferOrderId);

}
