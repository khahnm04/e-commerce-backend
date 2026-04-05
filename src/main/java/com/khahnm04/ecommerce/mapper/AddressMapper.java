package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.request.AddressRequest;
import com.khahnm04.ecommerce.dto.response.AddressResponse;
import com.khahnm04.ecommerce.entity.Address;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "addressType", source = "addressType")
    Address toAddress(AddressRequest request);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "fullAddress", expression = "java(buildFullAddress(address))")
    AddressResponse toResponse(Address address);

    List<AddressResponse> toResponseList(List<Address> addresses);

    @Mapping(target = "customer", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddress(@MappingTarget Address address, AddressRequest request);

    default String buildFullAddress(Address address) {
        StringBuilder sb = new StringBuilder();
        sb.append(address.getDetailAddress());
        sb.append(", ").append(address.getWardName());
        sb.append(", ").append(address.getDistrictName());
        sb.append(", ").append(address.getProvinceName());
        return sb.toString();
    }

}
