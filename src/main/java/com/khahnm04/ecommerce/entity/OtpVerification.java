package com.khahnm04.ecommerce.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("OtpVerification")
public class OtpVerification implements Serializable {

    @Id
    private String email;

    private String otp;

    @Builder.Default
    private Integer attempts = 0;

    @Builder.Default
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl = 300L; // 5 minutes

}
