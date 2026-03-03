package com.khahnm04.ecommerce.modules.auth.service.impl;

import com.khahnm04.ecommerce.modules.auth.dto.response.PermissionResponse;
import com.khahnm04.ecommerce.modules.auth.entity.Permission;
import com.khahnm04.ecommerce.modules.auth.repository.PermissionRepository;
import com.khahnm04.ecommerce.modules.auth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream()
                .map(permission -> PermissionResponse.builder()
                        .id(permission.getId())
                        .code(permission.getCode())
                        .name(permission.getName())
                        .module(permission.getModule())
                        .description(permission.getDescription())
                        .createdAt(permission.getCreatedAt())
                        .build())
                .toList();
    }

}
