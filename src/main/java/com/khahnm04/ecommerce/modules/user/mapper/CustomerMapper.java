package com.khahnm04.ecommerce.modules.user.mapper;

import com.khahnm04.ecommerce.modules.auth.dto.request.UserCreationRequest;
import com.khahnm04.ecommerce.modules.auth.dto.request.UserUpdateRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerCreationRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerUpdateRequest;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.modules.user.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "user", ignore = true)
    Customer toCustomer(CustomerCreationRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "dateOfBirth", source = "user.dateOfBirth")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "avatarUrl", source = "user.avatarUrl")
    @Mapping(target = "gender", source = "user.gender")
    @Mapping(target = "status", source = "user.status")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "addressCount", ignore = true)
    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(@MappingTarget Customer customer, CustomerUpdateRequest request);

    @Mapping(target = "roles", ignore = true)
    UserCreationRequest toUserCreationRequest(CustomerCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    UserUpdateRequest toUserUpdateRequest(CustomerUpdateRequest request);

}
