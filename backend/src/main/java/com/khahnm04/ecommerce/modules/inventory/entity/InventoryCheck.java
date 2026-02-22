package com.khahnm04.ecommerce.modules.inventory.entity;

import com.khahnm04.ecommerce.modules.inventory.enums.InventoryCheckStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory_checks")
@SQLDelete(sql = "UPDATE inventory_checks SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class InventoryCheck implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "check_date", nullable = false)
    private LocalDateTime checkDate;

    @Column(name = "check_type", nullable = false)
    private String checkType;

    @Column(name = "system_quantity", nullable = false)
    @Builder.Default
    private Integer systemQuantity = 0;

    @Column(name = "actual_quantity", nullable = false)
    @Builder.Default
    private Integer actualQuantity = 0;

    @Column(name = "difference", nullable = false)
    @Builder.Default
    private Integer difference = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private InventoryCheckStatus status = InventoryCheckStatus.IN_PROGRESS;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "checked_by")
    private Long checkedBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
