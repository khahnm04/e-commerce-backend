package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.common.enums.NewsStatus;
import com.khahnm04.ecommerce.entity.News;
import com.khahnm04.ecommerce.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    boolean existsBySlug(String slug);

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    boolean existsBySlugAndIdNot(String slug, Long id);

    Optional<News> findBySlugAndDeletedAtIsNull(String slug);

    Optional<News> findBySlugAndDeletedAtIsNotNull(String slug);

    Optional<News> findByIdAndDeletedAtIsNull(Long id);

    Optional<News> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT n FROM News n WHERE n.id IN :ids AND n.deletedAt IS NOT NULL")
    Set<News> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE News n SET n.status = :status WHERE n.id IN :ids AND n.deletedAt IS NULL")
    void updateStatusByIds(@Param("status") NewsStatus status, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE News n SET n.deletedAt = :now, n.deletedBy = :employee WHERE n.id IN :ids AND n.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("employee") Employee employee, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE News n SET n.deletedAt = NULL, n.deletedBy = NULL WHERE n.id IN :ids AND n.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
