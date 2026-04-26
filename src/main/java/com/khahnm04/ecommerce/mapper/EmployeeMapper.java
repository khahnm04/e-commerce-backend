package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.*;
import com.khahnm04.ecommerce.dto.response.EmployeeResponse;
import com.khahnm04.ecommerce.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "code", source = "code", qualifiedByName = "normalizeCode")
    Employee toEmployee(EmployeeCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    @Mapping(target = "deletedBy", source = "deletedBy.fullName")
    EmployeeResponse toResponse(Employee employee);

    @Mapping(target = "roles", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployee(@MappingTarget Employee employee, EmployeeUpdateRequest request);

    @Mapping(target = "roles", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployeeProfile(@MappingTarget Employee employee, EmployeeProfileUpdateRequest request);

    @Named("normalizeCode")
    default String normalizeCode(String code) {
        return code == null ? null : code.trim().toUpperCase();
    }

}
