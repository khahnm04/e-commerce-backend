package com.khahnm04.ecommerce.modules.news.repository;

import com.khahnm04.ecommerce.modules.news.entity.NewsProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NewsProductRepository extends JpaRepository<NewsProduct, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM NewsProduct np WHERE np.news.id = :newsId")
    void deleteAllByNewsId(Long newsId);

}
