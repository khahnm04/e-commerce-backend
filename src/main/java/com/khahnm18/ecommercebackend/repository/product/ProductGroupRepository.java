package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.entity.product.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, UUID>, JpaSpecificationExecutor<ProductGroup> {

    Optional<ProductGroup> findByIdAndIsDeletedFalse(UUID id);
    Optional<ProductGroup> findByIdAndIsDeletedTrue(UUID id);

    @Modifying
    void deleteAllByIdInAndIsDeletedTrue(Set<UUID> ids);

}
