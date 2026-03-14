package com.khahnm04.ecommerce.modules.user.controller;

import com.khahnm04.ecommerce.common.base.dto.ApiResponse;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerCreationRequest;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.modules.user.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${api.prefix}/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerResponse> createCustomer (
        @Valid @RequestBody CustomerCreationRequest request
    ) {
        return ApiResponse.<CustomerResponse>builder()
            .data(customerService.createCustomer(request))
            .message("Customer created successfully")
            .build();
    }

}
