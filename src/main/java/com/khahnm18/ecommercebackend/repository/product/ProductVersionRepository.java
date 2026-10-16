package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.entity.product.ProductVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductVersionRepository extends JpaRepository<ProductVersion, UUID> {

    Optional<ProductVersion> findByIdAndIsDeletedFalse(UUID id);
    Optional<ProductVersion> findByIdAndIsDeletedTrue(UUID id);

    @Modifying
    void deleteAllByIdInAndIsDeletedTrue(Set<UUID> ids);

}
