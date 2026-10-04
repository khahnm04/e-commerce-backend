package com.khahnm18.ecommercebackend.repository.category;

import com.khahnm18.ecommercebackend.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {

    Optional<Category> findByIdAndIsDeletedFalse(UUID id);

    Optional<Category> findByIdAndIsDeletedTrue(UUID id);

    @Modifying
    void deleteAllByIdInAndIsDeletedTrue(Set<UUID> ids);

}
