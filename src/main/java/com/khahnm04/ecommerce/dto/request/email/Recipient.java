package com.khahnm04.ecommerce.dto.request.email;

import lombok.Builder;
import java.io.Serializable;

@Builder
public record Recipient(

    String name,
    String email

) implements Serializable {
}
