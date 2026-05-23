package com.khahnm04.ecommerce.mapper.user;

import com.khahnm04.ecommerce.dto.request.user.EmployeeRequest;
import com.khahnm04.ecommerce.dto.response.user.EmployeeResponse;
import com.khahnm04.ecommerce.entity.user.Employee;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    Employee toEmployee(EmployeeRequest request);

    EmployeeResponse toEmployeeResponse(Employee employee);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEmployee(@MappingTarget Employee employee, EmployeeRequest request);

}
