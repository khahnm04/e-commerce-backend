package com.khahnm04.ecommerce.modules.branch.dto.request;

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
public class BranchBulkActionRequest implements Serializable {

    @NotEmpty(message = "BRANCH_SELECTION_REQUIRED")
    private Set<Long> branches;

    @NotBlank(message = "BRANCH_ACTION_REQUIRED")
    private String action;

}
