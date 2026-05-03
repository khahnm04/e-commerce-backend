package com.khahnm04.ecommerce.repository.product;

import com.khahnm04.ecommerce.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findBySlugAndDeletedAtIsNull(String slug);

    List<Tag> findAllByIdInAndDeletedAtIsNull(Set<Long> ids);

}
