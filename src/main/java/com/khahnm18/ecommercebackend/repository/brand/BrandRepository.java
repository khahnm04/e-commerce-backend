package com.khahnm18.ecommercebackend.repository.brand;

import com.khahnm18.ecommercebackend.entity.brand.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID>, JpaSpecificationExecutor<Brand> {

    Optional<Brand> findByIdAndIsDeletedFalse(UUID id);

    Optional<Brand> findByIdAndIsDeletedTrue(UUID id);

    @Modifying
    void deleteAllByIdInAndIsDeletedTrue(Set<UUID> ids);

}
