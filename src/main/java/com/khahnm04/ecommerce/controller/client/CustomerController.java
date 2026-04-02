package com.khahnm04.ecommerce.controller.client;

import com.khahnm04.ecommerce.dto.response.ApiResponse;
import com.khahnm04.ecommerce.dto.request.user.CustomerCreationRequest;
import com.khahnm04.ecommerce.dto.response.user.CustomerResponse;
import com.khahnm04.ecommerce.service.user.CustomerService;
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
