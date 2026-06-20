package com.khahnm04.ecommerce.service.user;

import com.khahnm04.ecommerce.dto.request.user.*;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.response.user.AccountAdminResponse;
import com.khahnm04.ecommerce.dto.response.user.AdminProfileResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface AccountAdminService {

    AccountAdminResponse createAccountAdmin(AccountAdminCreationRequest request);

    AccountAdminResponse getAccountAdminDetailById(Long id, boolean isDeleted);

    Page<AccountAdminResponse> getAllAccountAdmins(AccountAdminFilterRequest filter);

    AccountAdminResponse updateAccountAdmin(Long id, AccountAdminUpdateRequest request);

    void softDeleteAccountAdmin(Long id);

    void deleteAccountAdmin(Long id);

    void restoreAccountAdmin(Long id);

    void bulkAction(BulkActionRequest request);

    AdminProfileResponse getMyProfile();

    AdminProfileResponse updateMyProfile(AdminProfileRequest request);

    void changePassword(ChangePasswordRequest request);

    List<String> getMyPermissions();

}
