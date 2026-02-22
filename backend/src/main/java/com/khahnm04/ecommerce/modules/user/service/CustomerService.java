package com.khahnm04.ecommerce.modules.user.service;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.modules.user.dto.request.*;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.modules.user.entity.Customer;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreationRequest request);

    PageResponse<CustomerResponse> getAllCustomers(CustomerFilterRequest filter);

    PageResponse<CustomerResponse> getAllDeletedCustomers(CustomerFilterRequest filter);

    Customer getCustomerById(Long id);

    CustomerResponse getCustomerDetailById(Long id);

    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request);

    void updateCustomerStatus(Long id, CustomerUpdateStatusRequest status);

    void softDeleteCustomer(Long id);

    void restoreCustomer(Long id);

    void handleBulkAction(CustomerBulkActionRequest request);

}
