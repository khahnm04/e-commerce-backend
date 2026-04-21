package com.khahnm04.ecommerce.specification;

import com.khahnm04.ecommerce.dto.request.filter.ContactFilterRequest;
import com.khahnm04.ecommerce.entity.Contact;
import org.springframework.data.jpa.domain.Specification;

public class ContactSpecification {

    public static Specification<Contact> buildSpecification(ContactFilterRequest request) {
        if (request == null) {
            request = ContactFilterRequest.builder().build();
        }

        return BaseSpecification.<Contact>withDateRange("createdAt", request.startDate(), request.endDate())
                .and(BaseSpecification.withDeletedStatus(false))
                .and(BaseSpecification.withKeyword(request.keyword(), "email"));
    }

}
