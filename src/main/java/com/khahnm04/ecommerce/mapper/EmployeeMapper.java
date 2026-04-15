package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.UserCreationRequest;
import com.khahnm04.ecommerce.dto.request.UserUpdateRequest;
import com.khahnm04.ecommerce.dto.request.EmployeeCreationRequest;
import com.khahnm04.ecommerce.dto.request.EmployeeUpdateRequest;
import com.khahnm04.ecommerce.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "code", source = "code", qualifiedByName = "normalizeCode")
    Employee toEmployee(EmployeeCreationRequest request);

    @Mapping(target = "roles", ignore = true)
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
    EmployeeResponse toResponse(Employee employee);

    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployee(@MappingTarget Employee employee, EmployeeUpdateRequest request);

    @Named("normalizeCode")
    default String normalizeCode(String code) {
        return code == null ? null : code.trim().toUpperCase();
    }

    UserCreationRequest toUserCreationRequest(EmployeeCreationRequest request);

    UserUpdateRequest toUserUpdateRequest(EmployeeUpdateRequest request);

}
