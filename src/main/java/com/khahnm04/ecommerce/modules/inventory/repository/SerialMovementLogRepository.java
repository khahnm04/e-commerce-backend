package com.khahnm04.ecommerce.modules.inventory.repository;

import com.khahnm04.ecommerce.modules.inventory.entity.SerialMovementLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SerialMovementLogRepository extends JpaRepository<SerialMovementLog, Long> {

    List<SerialMovementLog> findBySerialNumberIdOrderByMovementDateDesc(Long serialNumberId);

}
