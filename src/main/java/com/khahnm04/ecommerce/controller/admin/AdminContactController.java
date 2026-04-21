package com.khahnm04.ecommerce.controller.admin;

import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.filter.ContactFilterRequest;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.response.ContactResponse;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/admin/contacts")
@RequiredArgsConstructor
public class AdminContactController {

    private final ContactService contactService;

    @GetMapping
    public ApiResponse<List<ContactResponse>> getAllContacts(
            @Valid @ModelAttribute ContactFilterRequest filter) {
        PageResponse<ContactResponse> pageResponse = contactService.getAllContacts(filter);
        return ApiResponse.<List<ContactResponse>>builder()
                .data(pageResponse.data())
                .meta(pageResponse.meta())
                .message("Get all contacts successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteContact(
            @PathVariable Long id) {
        contactService.deleteContact(id);
        return ApiResponse.<Void>builder()
                .message("Contact deleted successfully")
                .build();
    }

    @PatchMapping("/bulk")
    public ApiResponse<Void> handleBulkAction(
            @Valid @RequestBody BulkActionRequest request) {
        contactService.handleBulkAction(request);
        return ApiResponse.<Void>builder()
                .message("Bulk action executed successfully")
                .build();
    }

}
