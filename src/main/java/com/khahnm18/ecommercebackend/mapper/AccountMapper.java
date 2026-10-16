package com.khahnm18.ecommercebackend.mapper;

import com.khahnm18.ecommercebackend.dto.request.auth.RegisterRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.AdminUpdateRequest;
import com.khahnm18.ecommercebackend.dto.request.user.MyProfileAdminRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserCreationRequest;
import com.khahnm18.ecommercebackend.dto.request.user.UserUpdateRequest;
import com.khahnm18.ecommercebackend.entity.auth.Account;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {

    @Mapping(target = "status", defaultValue = "ACTIVE")
    @Mapping(target = "password", ignore = true)
    Account toAccount(AdminCreationRequest request);

    @Mapping(target = "status", defaultValue = "ACTIVE")
    @Mapping(target = "password", ignore = true)
    Account toAccount(UserCreationRequest request);

    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "password", ignore = true)
    Account toAccount(RegisterRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccount(@MappingTarget Account account, AdminUpdateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccount(@MappingTarget Account account, UserUpdateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccount(@MappingTarget Account account, MyProfileAdminRequest request);

}
