package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.ContactRequest;
import com.khahnm04.ecommerce.dto.request.filter.ContactFilterRequest;
import com.khahnm04.ecommerce.dto.response.ContactResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.entity.Contact;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.mapper.ContactMapper;
import com.khahnm04.ecommerce.repository.ContactRepository;
import com.khahnm04.ecommerce.service.ContactService;
import com.khahnm04.ecommerce.specification.ContactSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional
    public ContactResponse registerContact(ContactRequest request) {
        if (contactRepository.existsByEmail(request.email())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        Contact contact = Contact.builder()
                .email(request.email())
                .build();
        contact = contactRepository.save(contact);
        return contactMapper.toResponse(contact);
    }

    @Override
    public PageResponse<ContactResponse> getAllContacts(ContactFilterRequest filter) {
        Pageable pageable = PageRequest.of(filter.page() - 1, filter.size(), SortUtils.parseSort(filter.sort()));
        Specification<Contact> spec = ContactSpecification.buildSpecification(filter);

        Page<Contact> page = contactRepository.findAll(spec, pageable);
        return PageResponse.fromPage(page.map(contactMapper::toResponse));
    }

    @Override
    @Transactional
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            log.warn("Contact not found: {}", id);
            return;
        }
        contactRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> contactIds = request.ids();
        String action = request.action().toUpperCase();

        if (contactIds == null || contactIds.isEmpty()) {
            throw new AppException(ErrorCode.CONTACT_INVALID_BULK_ACTION);
        }

        if ("DELETE".equals(action)) {
            contactRepository.deleteAllById(contactIds);
            log.info("Bulk permanently delete contacts ids: {}", contactIds);
        } else {
            throw new AppException(ErrorCode.CONTACT_INVALID_BULK_ACTION);
        }
    }

}
