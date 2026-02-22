package com.khahnm04.ecommerce.modules.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerialUpdateNotesRequest implements Serializable {

    @NotBlank(message = "SERIAL_NOTES_REQUIRED")
    private String notes;

}
