package com.khahnm04.ecommerce.dto.response;

import lombok.Builder;
import java.io.Serializable;

@Builder
public record IntrospectResponse(

    boolean valid

) implements Serializable {
}
