package com.khahnm18.ecommercebackend.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.CreatedBy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by")
    private UUID deletedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

}
