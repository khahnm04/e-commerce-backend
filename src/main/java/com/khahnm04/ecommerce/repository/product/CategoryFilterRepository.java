package com.khahnm04.ecommerce.repository.product;

import com.khahnm04.ecommerce.entity.CategoryFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryFilterRepository extends JpaRepository<CategoryFilter, Long> {

    List<CategoryFilter> findAllByCategoryIdOrderBySortOrderAsc(Long categoryId);
    
}
