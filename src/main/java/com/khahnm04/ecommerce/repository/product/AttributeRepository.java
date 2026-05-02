package com.khahnm04.ecommerce.repository.product;

import com.khahnm04.ecommerce.entity.Attribute;
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
public interface AttributeRepository extends JpaRepository<Attribute, Long>, JpaSpecificationExecutor<Attribute> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<Attribute> findByIdAndDeletedAtIsNull(Long id);

    Optional<Attribute> findByIdAndDeletedAtIsNotNull(Long id);

    @Query("SELECT a FROM Attribute a WHERE a.id IN :ids AND a.deletedAt IS NOT NULL")
    Set<Attribute> findAllDeletedByIds(@Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Attribute a SET a.deletedAt = :now, a.deletedBy = :employee WHERE a.id IN :ids AND a.deletedAt IS NULL")
    void softDeleteByIds(@Param("now") LocalDateTime now, @Param("employee") Employee employee, @Param("ids") Set<Long> ids);

    @Modifying
    @Query("UPDATE Attribute a SET a.deletedAt = NULL, a.deletedBy = NULL WHERE a.id IN :ids AND a.deletedAt IS NOT NULL")
    void restoreDeletedByIds(@Param("ids") Set<Long> ids);

}
