package com.khahnm18.ecommercebackend.service.user;

import com.khahnm18.ecommercebackend.entity.auth.Account;
import com.khahnm18.ecommercebackend.entity.auth.Role;
import java.util.List;
import java.util.UUID;

public interface AccountRoleService {

    List<Role> assignRolesToAccount(UUID accountId, List<UUID> roleIds);

    List<Role> replaceRolesForAdmin(UUID accountId, List<UUID> roleIds);

}
