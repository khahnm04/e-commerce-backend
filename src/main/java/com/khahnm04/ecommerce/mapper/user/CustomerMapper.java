package com.khahnm04.ecommerce.mapper.user;

import com.khahnm04.ecommerce.dto.request.auth.UserCreationRequest;
import com.khahnm04.ecommerce.dto.request.auth.UserUpdateRequest;
import com.khahnm04.ecommerce.dto.request.user.CustomerCreationRequest;
import com.khahnm04.ecommerce.dto.request.user.CustomerUpdateRequest;
import com.khahnm04.ecommerce.dto.response.user.CustomerResponse;
import com.khahnm04.ecommerce.entity.user.Customer;
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
    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomer(@MappingTarget Customer customer, CustomerUpdateRequest request);

    @Mapping(target = "roles", ignore = true)
    UserCreationRequest toUserCreationRequest(CustomerCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    UserUpdateRequest toUserUpdateRequest(CustomerUpdateRequest request);

}
