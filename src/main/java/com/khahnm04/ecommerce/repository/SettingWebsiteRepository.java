package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.SettingWebsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingWebsiteRepository extends JpaRepository<SettingWebsite, Long> {
}
