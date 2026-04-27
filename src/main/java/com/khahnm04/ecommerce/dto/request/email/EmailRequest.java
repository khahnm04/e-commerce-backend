package com.khahnm04.ecommerce.dto.request.email;

import lombok.Builder;
import java.io.Serializable;
import java.util.List;

@Builder
public record EmailRequest(

    Sender sender,
    List<Recipient> to,
    String subject,
    String htmlContent

) implements Serializable {
}
