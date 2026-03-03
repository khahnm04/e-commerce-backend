package com.khahnm04.ecommerce.modules.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBulkActionRequest implements Serializable {

    @NotEmpty(message = "EMPLOYEE_SELECTION_REQUIRED")
    private Set<Long> employees;

    @NotBlank(message = "EMPLOYEE_ACTION_REQUIRED")
    private String action;

}
