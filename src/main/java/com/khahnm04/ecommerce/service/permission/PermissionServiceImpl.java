package com.khahnm04.ecommerce.service.permission;

import com.khahnm04.ecommerce.dto.response.permission.PermissionResponse;
import com.khahnm04.ecommerce.entity.auth.Permission;
import com.khahnm04.ecommerce.mapper.auth.PermissionMapper;
import com.khahnm04.ecommerce.repository.auth.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j(topic = "PERMISSION-SERVICE")
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
            .map(permissionMapper::toPermissionResponse)
            .toList();
    }

}
