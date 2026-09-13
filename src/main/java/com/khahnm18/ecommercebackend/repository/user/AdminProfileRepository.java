package com.khahnm18.ecommercebackend.repository.user;

import com.khahnm18.ecommercebackend.entity.user.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AdminProfileRepository  extends JpaRepository<AdminProfile, UUID>, JpaSpecificationExecutor<AdminProfile> {
}
