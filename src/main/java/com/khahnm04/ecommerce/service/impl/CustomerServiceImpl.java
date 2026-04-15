package com.khahnm04.ecommerce.service.impl;

import com.khahnm04.ecommerce.config.AuditorAwareConfig;
import com.khahnm04.ecommerce.dto.response.PageResponse;
import com.khahnm04.ecommerce.dto.projection.CustomerProjection;
import com.khahnm04.ecommerce.dto.request.BulkActionRequest;
import com.khahnm04.ecommerce.dto.request.CustomerCreationRequest;
import com.khahnm04.ecommerce.dto.request.filter.CustomerFilterRequest;
import com.khahnm04.ecommerce.dto.request.CustomerUpdateRequest;
import com.khahnm04.ecommerce.exception.AppException;
import com.khahnm04.ecommerce.exception.ErrorCode;
import com.khahnm04.ecommerce.entity.User;
import com.khahnm04.ecommerce.common.enums.UserStatus;
import com.khahnm04.ecommerce.service.UserService;
import com.khahnm04.ecommerce.dto.response.CustomerResponse;
import com.khahnm04.ecommerce.entity.Customer;
import com.khahnm04.ecommerce.specification.CustomerSpecification;
import com.khahnm04.ecommerce.mapper.CustomerMapper;
import com.khahnm04.ecommerce.repository.CustomerRepository;
import com.khahnm04.ecommerce.repository.OrderRepository;
import com.khahnm04.ecommerce.service.CustomerService;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CustomerMapper customerMapper;
    private final UserService userService;
    private final AuditorAwareConfig auditorAwareConfig;

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
    public void softDeleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        if (customer.getDeletedAt() != null) {
            log.warn("Customer with id {} is already soft deleted", id);
            return;
        }
        customer.setDeletedAt(LocalDateTime.now());
        customer.setDeletedBy(auditorAwareConfig.getCurrentAuditor().orElse(null));
        customerRepository.save(customer);
        log.info("Soft-deleted customer with id: {}", id);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getDeletedCustomerById(id);
        if (customer.getDeletedAt() == null) {
            log.warn("Customer with id {} is already deleted", id);
            return;
        }
        if (orderRepository.existsByCustomerId(id)) {
            throw new AppException(ErrorCode.CUSTOMER_HAS_ORDERS);
        }
        customerRepository.delete(customer);
        log.info("Permanently deleted customer with id: {}", id);
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
        customer.setDeletedBy(null);
        customerRepository.save(customer);
        log.info("Customer restored with id {}", id);
    }

    @Override
    @Transactional
    public void handleBulkAction(BulkActionRequest request) {
        Set<Long> customerIds = request.ids();
        String action = request.action().toUpperCase();

        if (customerIds == null || customerIds.isEmpty()) {
            throw new AppException(ErrorCode.CUSTOMER_BULK_ACTION_REQUIRED_IDS);
        }

        switch (action) {
            case "ACTIVE", "INACTIVE", "BANNED", "LOCKED" -> {
                Set<Long> existingUserIds = customerRepository.findAllById(customerIds)
                        .stream()
                        .map(customer -> customer.getUser().getId())
                        .collect(Collectors.toSet());
                userService.bulkUpdateStatus(existingUserIds, UserStatus.valueOf(action));
                log.info("Bulk update status {} customers ids: {}", action, customerIds);
            }
            case "SOFT_DELETE" -> {
                User currentAuditor = auditorAwareConfig.getCurrentAuditorReference();
                customerRepository.softDeleteByIds(LocalDateTime.now(), currentAuditor, customerIds);
                log.info("Bulk soft-delete customers ids: {}", customerIds);
            }
            case "DELETE" -> {
                if (orderRepository.existsByCustomerIdIn(customerIds)) {
                    throw new AppException(ErrorCode.CUSTOMER_HAS_ORDERS);
                }
                Set<Customer> customers = customerRepository.findAllDeletedByIds(customerIds);
                customerRepository.deleteAll(customers);
                log.info("Bulk permanently delete customers ids: {}", customerIds);
            }
            case "RESTORE" -> {
                customerRepository.restoreDeletedByIds(customerIds);
                log.info("Bulk restore customers ids: {}", customerIds);
            }
            default -> throw new AppException(ErrorCode.CUSTOMER_INVALID_BULK_ACTION);
        }
        log.info("Executed bulk action {} for {} customers", action, customerIds.size());
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
        Page<CustomerProjection> page = customerRepository.findBy(spec,
                q -> q.as(CustomerProjection.class).page(pageable));

        List<CustomerResponse> content = page.getContent().stream()
                .map(proj -> CustomerResponse.builder()
                        .id(proj.getId())
                        .userId(proj.getUserId())
                        .fullName(proj.getFullName())
                        .email(proj.getEmail())
                        .phoneNumber(proj.getPhoneNumber())
                        .dateOfBirth(proj.getDateOfBirth())
                        .gender(proj.getGender())
                        .avatar(proj.getAvatar())
                        .status(proj.getUserStatus())
                        .addressCount(0)
                        .createdAt(proj.getCreatedAt())
                        .createdBy(proj.getCreatedByFullName())
                        .updatedAt(proj.getUpdatedAt())
                        .updatedBy(proj.getUpdatedByFullName())
                        .deletedAt(proj.getDeletedAt())
                        .deletedBy(proj.getDeletedByFullName())
                        .build())
                .collect(Collectors.toList());

        Page<CustomerResponse> responsePage = new PageImpl<>(content, pageable, page.getTotalElements());
        return PageResponse.fromPage(responsePage);
    }

}
