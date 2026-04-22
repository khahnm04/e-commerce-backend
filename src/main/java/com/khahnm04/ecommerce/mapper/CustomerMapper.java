package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.*;
import com.khahnm04.ecommerce.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "user", ignore = true)
    Customer toCustomer(CustomerCreationRequest request);

    @Mapping(target = "addressCount", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "dateOfBirth", source = "user.dateOfBirth")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "avatar", source = "user.avatar")
    @Mapping(target = "gender", source = "user.gender")
    @Mapping(target = "status", source = "user.status")
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "deletedBy", source = "deletedBy.fullName")
    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(@MappingTarget Customer customer, CustomerUpdateRequest request);

    @Mapping(target = "roles", ignore = true)
    UserCreationRequest toUserCreationRequest(CustomerCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    UserUpdateRequest toUserUpdateRequest(CustomerUpdateRequest request);

    UserUpdateRequest toUserUpdateRequest(CustomerProfileUpdateRequest request);

    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(@MappingTarget Customer customer, CustomerProfileUpdateRequest request);

}
