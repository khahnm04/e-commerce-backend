package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.CustomerCreationRequest;
import com.khahnm04.ecommerce.dto.request.filter.CustomerFilterRequest;
import com.khahnm04.ecommerce.dto.request.CustomerUpdateRequest;
import com.khahnm04.ecommerce.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.entity.Customer;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreationRequest request);

    Customer getCustomerById(Long id);

    CustomerResponse getCustomerDetail(Long id, boolean deleted);

    PageResponse<CustomerResponse> getAllCustomers(CustomerFilterRequest filter);

    PageResponse<CustomerResponse> getAllDeletedCustomers(CustomerFilterRequest filter);

    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request);

    void softDeleteCustomer(Long id);

    void restoreCustomer(Long id);

    void handleBulkAction(BulkActionRequest request);

}
