package com.khahnm04.ecommerce.repository.product;

import com.khahnm04.ecommerce.entity.TagGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TagGroupRepository extends JpaRepository<TagGroup, Long> {
    
    Optional<TagGroup> findBySlugAndDeletedAtIsNull(String slug);

    boolean existsByNameAndDeletedAtIsNull(String name);

}
