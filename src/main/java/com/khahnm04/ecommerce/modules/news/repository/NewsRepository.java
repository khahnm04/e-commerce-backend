package com.khahnm04.ecommerce.modules.news.repository;

import com.khahnm04.ecommerce.modules.news.entity.News;
import com.khahnm04.ecommerce.modules.news.enums.NewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    Optional<News> findByIdAndDeletedAtIsNull(Long id);

    Optional<News> findByIdAndDeletedAtIsNotNull(Long id);

    Optional<News> findBySlugAndDeletedAtIsNull(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE News n SET n.viewCount = n.viewCount + 1 WHERE n.id = :id")
    void incrementViewCount(Long id);

    @Query("SELECT n FROM News n WHERE n.id IN :ids AND n.deletedAt IS NOT NULL")
    Set<News> findAllDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE News n SET n.deletedAt = :now WHERE n.id IN :ids AND n.deletedAt IS NULL")
    void softDeleteByIds(LocalDateTime now, Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE News n SET n.deletedAt = NULL WHERE n.id IN :ids AND n.deletedAt IS NOT NULL")
    void restoreDeletedByIds(Set<Long> ids);

    @Modifying
    @Transactional
    @Query("UPDATE News n SET n.status = :status WHERE n.id IN :ids AND n.deletedAt IS NULL")
    void bulkUpdateStatus(NewsStatus status, Set<Long> ids);

}
