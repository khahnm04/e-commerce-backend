package com.khahnm04.ecommerce.modules.branch.dto.request;

import com.khahnm04.ecommerce.modules.branch.enums.BranchStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchUpdateStatusRequest implements Serializable {

    @NotNull(message = "BRANCH_STATUS_REQUIRED")
    private BranchStatus status;

}
