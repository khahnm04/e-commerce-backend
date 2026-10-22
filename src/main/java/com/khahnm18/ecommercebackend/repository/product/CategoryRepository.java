package com.khahnm18.ecommercebackend.repository.product;

import com.khahnm18.ecommercebackend.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID>, CustomCategoryRepository {

    Optional<Category> findByIdAndIsDeletedFalse(UUID id);
    Optional<Category> findByIdAndIsDeletedTrue(UUID id);

    @Modifying
    void deleteAllByIdInAndIsDeletedTrue(Set<UUID> ids);

    @Query("""
        SELECT c FROM Category c
        JOIN ProductGroupCategory pgc ON pgc.categoryId = c.id
        WHERE pgc.groupId = :groupId
    """)
    List<Category> findCategoriesByGroupId(@Param("groupId") UUID groupId);

}
