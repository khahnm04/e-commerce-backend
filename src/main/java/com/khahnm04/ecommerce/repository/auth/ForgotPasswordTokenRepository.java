package com.khahnm04.ecommerce.repository.auth;

import com.khahnm04.ecommerce.entity.ForgotPasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordTokenRepository extends CrudRepository<ForgotPasswordToken, String> {
}
