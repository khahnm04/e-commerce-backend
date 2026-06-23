package com.khahnm04.ecommerce.service.auth;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.auth.RoleRequest;
import com.khahnm04.ecommerce.dto.response.auth.RoleResponse;
import org.springframework.data.domain.Page;
import java.time.Instant;

public interface RoleService {

    RoleResponse createRole(RoleRequest request);

    RoleResponse getRoleDetailById(Long id, boolean isDeleted);

    Page<RoleResponse> getAllRoles(
        int page,
        int size,
        String sort,
        String keyword,
        boolean isDeleted,
        Instant startDate,
        Instant endDate
    );

    RoleResponse updateRole(Long id, RoleRequest request);

    void softDeleteRole(Long id);

    void deleteRole(Long id);

    void restoreRole(Long id);

    void bulkAction(BulkActionRequest request);

}
