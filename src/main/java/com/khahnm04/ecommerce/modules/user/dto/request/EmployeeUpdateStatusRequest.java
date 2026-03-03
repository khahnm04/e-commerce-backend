package com.khahnm04.ecommerce.modules.user.dto.request;

import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateStatusRequest implements Serializable {

    @NotNull(message = "EMPLOYEE_STATUS_REQUIRED")
    private UserStatus status;

}
