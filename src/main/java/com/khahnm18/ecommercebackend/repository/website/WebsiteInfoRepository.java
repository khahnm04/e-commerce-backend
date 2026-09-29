package com.khahnm18.ecommercebackend.repository.website;

import com.khahnm18.ecommercebackend.entity.website.WebsiteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WebsiteInfoRepository extends JpaRepository<WebsiteInfo, UUID> {

    Optional<WebsiteInfo> findFirstByOrderByCreatedAtDesc();

}
