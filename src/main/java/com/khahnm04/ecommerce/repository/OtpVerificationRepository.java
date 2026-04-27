package com.khahnm04.ecommerce.repository;

import com.khahnm04.ecommerce.entity.OtpVerification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpVerificationRepository extends CrudRepository<OtpVerification, String> {
}
