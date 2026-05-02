package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.common.util.SecurityUtils;
import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.request.AddressRequest;
import com.khahnm04.ecommerce.dto.response.AddressResponse;
import com.khahnm04.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/addresses")
@RequiredArgsConstructor
public class CustomerAddressController {

    private final AddressService addressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AddressResponse> createAddress(
            @Valid @RequestBody AddressRequest request
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        return ApiResponse.<AddressResponse>builder()
                .data(addressService.createAddress(customerId, request))
                .message("Address created successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<AddressResponse> getAddressDetailById(
        @PathVariable Long id
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        return ApiResponse.<AddressResponse>builder()
            .data(addressService.getAddressDetailById(customerId, id))
            .message("Get address detail successfully")
            .build();
    }

    @GetMapping
    public ApiResponse<List<AddressResponse>> getAllAddressesByCustomerId(
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        return ApiResponse.<List<AddressResponse>>builder()
                .data(addressService.getAllAddressesByCustomerId(customerId))
                .message("Get addresses successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<AddressResponse> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        return ApiResponse.<AddressResponse>builder()
                .data(addressService.updateAddress(customerId, id, request))
                .message("Address updated successfully")
                .build();
    }

    @PatchMapping("/{id}/default")
    public ApiResponse<Void> setDefaultAddress(
            @PathVariable Long id
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        addressService.setDefaultAddress(customerId, id);
        return ApiResponse.<Void>builder()
                .message("Address set as default successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAddress(
            @PathVariable Long id
    ) {
        Long customerId = SecurityUtils.getAuthenticatedUserId();
        addressService.deleteAddress(customerId, id);
        return ApiResponse.<Void>builder()
                .message("Address deleted successfully")
                .build();
    }

}
