package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.dto.request.ContactRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.ContactResponse;
import com.khahnm04.ecommerce.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContactResponse> registerContact(
            @RequestBody @Valid ContactRequest request
    ) {
        log.info("Registering contact: {}", request.email());
        return ApiResponse.<ContactResponse>builder()
                .data(contactService.registerContact(request))
                .message("Contact registered successfully")
                .build();
    }

}
