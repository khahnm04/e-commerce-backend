package com.khahnm04.ecommerce.mapper;

import com.khahnm04.ecommerce.dto.response.ContactResponse;
import com.khahnm04.ecommerce.entity.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ContactMapper {

    ContactResponse toResponse(Contact contact);

}
