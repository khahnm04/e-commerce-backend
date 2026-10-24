package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.entity.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID>, CustomBrandRepository {

    Optional<Brand> findByIdAndIsDeletedFalse(UUID id);
    Optional<Brand> findByIdAndIsDeletedTrue(UUID id);

}
