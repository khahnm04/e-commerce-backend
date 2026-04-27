package com.khahnm04.ecommerce.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@RedisHash("ForgotPasswordToken")
public class ForgotPasswordToken implements Serializable {
    @Id
    String email;
    
    String token;

    @TimeToLive
    Long expirationTime;
}
