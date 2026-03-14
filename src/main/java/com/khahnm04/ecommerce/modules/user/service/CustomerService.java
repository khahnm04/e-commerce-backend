package com.khahnm04.ecommerce.modules.user.service;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerCreationRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerFilterRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerUpdateRequest;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.modules.user.entity.Customer;
import java.util.Set;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerCreationRequest request);

    Customer getCustomerById(Long id);

    CustomerResponse getCustomerDetail(Long id, boolean deleted);

    PageResponse<CustomerResponse> getAllCustomers(CustomerFilterRequest filter);

    PageResponse<CustomerResponse> getAllDeletedCustomers(CustomerFilterRequest filter);

    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request);

    void updateCustomerStatus(Long id, UserStatus status);

    void softDeleteCustomer(Long id);

    void restoreCustomer(Long id);

    void handleBulkAction(Set<Long> customers, String action);

}
