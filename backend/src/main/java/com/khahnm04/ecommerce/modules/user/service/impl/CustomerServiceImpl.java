package com.khahnm04.ecommerce.modules.user.service.impl;

import com.khahnm04.ecommerce.common.dto.PageResponse;
import com.khahnm04.ecommerce.common.entity.BaseEntity;
import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.common.util.SortUtils;
import com.khahnm04.ecommerce.modules.auth.entity.User;
import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.service.UserService;
import com.khahnm04.ecommerce.modules.user.dto.request.*;
import com.khahnm04.ecommerce.modules.user.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.modules.user.entity.Customer;
import com.khahnm04.ecommerce.modules.user.enums.CustomerSortField;
import com.khahnm04.ecommerce.modules.user.mapper.CustomerMapper;
import com.khahnm04.ecommerce.modules.user.mapper.CustomerMyBatisMapper;
import com.khahnm04.ecommerce.modules.user.repository.CustomerRepository;
import com.khahnm04.ecommerce.modules.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
    private final CustomerMyBatisMapper customerMyBatisMapper;
    private final UserService userService;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerCreationRequest request) {
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
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
    public PageResponse<CustomerResponse> getAllCustomers(CustomerFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<CustomerResponse> page = this.getAllWithFilterAndSearch(request, pageable, false);
        return PageResponse.fromPage(page);
    }

    @Override
    public PageResponse<CustomerResponse> getAllDeletedCustomers(CustomerFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(),
                SortUtils.parseSort(request.getSort()));

        Page<CustomerResponse> page = this.getAllWithFilterAndSearch(request, pageable, true);
        return PageResponse.fromPage(page);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    @Override
    public CustomerResponse getCustomerDetailById(Long id) {
        return customerMapper.toResponse(this.getCustomerById(id));
    }

    @Override
    public CustomerResponse getDeletedCustomerDetailById(Long id) {
        Customer customer = customerRepository.findDeletedCustomerById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));
        return customerMapper.toResponse(customer);
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
    public void updateCustomerStatus(Long id, CustomerUpdateStatusRequest request) {
        Customer customer = this.getCustomerById(id);
        userService.updateUserStatus(customer.getUser().getId(), request.getStatus());
        log.info("Updated status for customer {} to {}", customer.getId(), request.getStatus());
    }

    @Override
    @Transactional
    public void softDeleteCustomer(Long id) {
        Customer customer = this.getCustomerById(id);
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);

        userService.softDeleteUser(customer.getUser().getId());
        log.info("Soft deleted customer with id: {}", id);
    }

    @Override
    @Transactional
    public void restoreCustomer(Long id) {
        customerRepository.findDeletedCustomerById(id)
                .ifPresentOrElse(customer -> {
                    if (customer.getDeletedAt() == null) {
                        log.warn("Customer with id {} is not deleted, no restoration needed", id);
                        return;
                    }
                    customer.setDeletedAt(null);
                    customerRepository.save(customer);

                    userService.restoreUser(customer.getUser().getId());
                    log.info("Restored customer id {}", id);
                }, () -> {
                    throw new AppException(ErrorCode.CUSTOMER_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public void handleBulkAction(CustomerBulkActionRequest request) {
        Set<Long> customerIds = request.getCustomers();
        if (customerIds == null || customerIds.isEmpty()) {
            throw new AppException(ErrorCode.CUSTOMER_BULK_ACTION_REQUIRED_IDS);
        }

        String action = request.getAction().toUpperCase();

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

    private Page<CustomerResponse> getAllWithFilterAndSearch(CustomerFilterRequest request, Pageable pageable,
            boolean isDeleted) {
        String keyword = StringUtils.hasText(request.getKeyword())
                ? request.getKeyword().trim()
                : null;

        // Parse sort
        String[] sortParams = SortUtils.parseSortForMyBatis(request.getSort(), CustomerSortField.class);
        String sortColumn = sortParams[0];
        String sortDirection = sortParams[1];

        List<CustomerResponse> content = customerMyBatisMapper.findAllWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getGender(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted,
                sortColumn,
                sortDirection,
                pageable.getOffset(),
                pageable.getPageSize());

        long total = customerMyBatisMapper.countWithFilterAndSearch(
                keyword,
                request.getStatus(),
                request.getGender(),
                request.getStartDate(),
                request.getEndDate(),
                isDeleted);

        return new PageImpl<>(content, pageable, total);
    }

}
