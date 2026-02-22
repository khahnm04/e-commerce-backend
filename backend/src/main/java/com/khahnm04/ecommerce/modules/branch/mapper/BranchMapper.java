package com.khahnm04.ecommerce.modules.branch.mapper;

import com.khahnm04.ecommerce.modules.branch.dto.request.BranchRequest;
import com.khahnm04.ecommerce.modules.branch.dto.response.BranchResponse;
import com.khahnm04.ecommerce.modules.branch.entity.Branch;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toEntity(BranchRequest request);

    @Mapping(target = "createdBy", source = "createdBy.fullName")
    @Mapping(target = "updatedBy", source = "updatedBy.fullName")
    BranchResponse toResponse(Branch branch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBranch(@MappingTarget Branch branch, BranchRequest request);


}
