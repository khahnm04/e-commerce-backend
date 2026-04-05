package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.dto.response.PermissionResponse;
import com.khahnm04.ecommerce.entity.Permission;
import com.khahnm04.ecommerce.repository.PermissionRepository;
import com.khahnm04.ecommerce.service.PermissionService;
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
                        .subModule(permission.getSubModule())
                        .module(permission.getModule())
                        .description(permission.getDescription())
                        .createdAt(permission.getCreatedAt())
                        .build())
                .toList();
    }

}
