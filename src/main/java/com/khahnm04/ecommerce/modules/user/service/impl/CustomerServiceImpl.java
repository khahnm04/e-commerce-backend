package com.khahnm04.ecommerce.modules.user.service.impl;

import com.khahnm04.ecommerce.common.base.dto.PageResponse;
import com.khahnm04.ecommerce.common.base.entity.BaseEntity;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerCreationRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerFilterRequest;
import com.khahnm04.ecommerce.modules.user.dto.request.CustomerUpdateRequest;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.service.UserService;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.modules.user.entity.Customer;
import com.khahnm04.ecommerce.modules.user.repository.specification.CustomerSpecification;
import com.khahnm04.ecommerce.modules.user.mapper.CustomerMapper;
import com.khahnm04.ecommerce.modules.user.repository.CustomerRepository;
import com.khahnm04.ecommerce.modules.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserService userService;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerCreationRequest request) {
        if (!Objects.equals(request.password(), request.confirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        User user = userService.createUser(customerMapper.toUserCreationRequest(request));

        Customer newCustomer = customerMapper.toCustomer(request);
        newCustomer.setUser(user);
        Customer savedCustomer = customerRepository.save(newCustomer);

        log.info("Created new customer with id: {}", savedCustomer.getId());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Override
    public CustomerResponse getCustomerDetail(Long id, boolean deleted) {
        Customer customer = deleted
                ? getDeletedCustomerById(id)
                : getCustomerById(id);
        return customerMapper.toResponse(customer);
    }

    @Override
    public PageResponse<CustomerResponse> getAllCustomers(CustomerFilterRequest request) {
        return getAllWithFilterAndSearch(request, false);
    }

    @Override
    public PageResponse<CustomerResponse> getAllDeletedCustomers(CustomerFilterRequest request) {
        return getAllWithFilterAndSearch(request, true);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = this.getCustomerById(id);

        userService.updateUser(customer.getUser().getId(), customerMapper.toUserUpdateRequest(request));

        customerMapper.updateCustomer(customer, request);
        customerRepository.save(customer);

        log.info("Updated customer with id: {}", id);
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional
    public void updateCustomerStatus(Long id, UserStatus status) {
        Customer customer = this.getCustomerById(id);
        userService.updateUserStatus(customer.getUser().getId(), status);
        log.info("Updated status for customer {} to {}", customer.getId(), status);
    }

    @Override
    @Transactional
    public void softDeleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        if (customer.getDeletedAt() != null) {
            log.warn("Customer with id {} is already soft deleted", id);
            return;
        }
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
        log.info("Soft-deleted customer with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreCustomer(Long id) {
        Customer customer = getDeletedCustomerById(id);
        if (customer.getDeletedAt() == null) {
            log.warn("Customer with id {} is not in a deleted state", id);
            return;
        }
        customer.setDeletedAt(null);
        customerRepository.save(customer);
        log.info("Customer restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(Set<Long> customerIds, String actionInput) {
        if (customerIds == null || customerIds.isEmpty()) {
            throw new AppException(ErrorCode.CUSTOMER_BULK_ACTION_REQUIRED_IDS);
        }

        String action = actionInput.toUpperCase();

        Set<Customer> existingCustomers = new HashSet<>(
                action.equals("RESTORE")
                        ? customerRepository.findAllDeletedByIds(customerIds)
                        : customerRepository.findAllById(customerIds));

        Set<Long> existingCustomerIds = existingCustomers.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (customerIds.size() != existingCustomerIds.size()) {
            Set<Long> missingIds = new HashSet<>(customerIds);
            missingIds.removeAll(existingCustomerIds);
            log.error("Bulk action customers failed. Missing Ids: {}", missingIds);
            throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
        }

        Set<Long> existingUserIds = existingCustomers.stream()
                .map(customer -> customer.getUser().getId())
                .collect(Collectors.toSet());

        switch (action) {
            case "ACTIVE", "INACTIVE", "BANNED", "LOCKED" -> {
                userService.bulkUpdateStatus(existingUserIds, UserStatus.valueOf(action));
                log.info("Bulk update status {} customers ids: {}", action, existingCustomerIds);
            }
            case "DELETE" -> {
                customerRepository.softDeleteByIds(LocalDateTime.now(), existingCustomerIds);
                log.info("Bulk delete customers ids: {}", existingCustomerIds);
            }
            case "RESTORE" -> {
                customerRepository.restoreDeletedByIds(existingCustomerIds);
                log.info("Bulk restore customers ids: {}", existingCustomerIds);
            }
            default -> throw new AppException(ErrorCode.CUSTOMER_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} customers", action, existingCustomerIds.size());
    }

    // ======================== Private methods ========================

    private Customer getDeletedCustomerById(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    private PageResponse<CustomerResponse> getAllWithFilterAndSearch(CustomerFilterRequest request, boolean isDeleted) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.size(),
                CustomerSpecification.getSort(request.sort()));

        Specification<Customer> spec = CustomerSpecification.buildSpecification(request, isDeleted);
        Page<Customer> page = customerRepository.findAll(spec, pageable);

        List<CustomerResponse> content = page.getContent().stream()
                .map(customerMapper::toResponse)
                .collect(Collectors.toList());

        Page<CustomerResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
