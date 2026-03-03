package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.ImportDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImportDetailRepository extends JpaRepository<ImportDetail, Long> {

    @EntityGraph(attributePaths = { "serialNumber" })
    List<ImportDetail> findByImportOrderId(Long importOrderId);

    void deleteByImportOrderId(Long importOrderId);

    boolean existsBySerialNumberId(Long serialNumberId);

}
