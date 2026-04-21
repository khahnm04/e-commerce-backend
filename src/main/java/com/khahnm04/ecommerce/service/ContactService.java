package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.ContactRequest;
import com.khahnm04.ecommerce.dto.request.filter.ContactFilterRequest;
import com.khahnm04.ecommerce.dto.response.ContactResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;

public interface ContactService {

    ContactResponse registerContact(ContactRequest request);

    PageResponse<ContactResponse> getAllContacts(ContactFilterRequest filter);

    void deleteContact(Long id);

    void handleBulkAction(BulkActionRequest request);

}
