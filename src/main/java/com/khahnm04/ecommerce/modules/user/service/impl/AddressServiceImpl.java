package com.khahnm04.ecommerce.modules.user.service.impl;

import com.khahnm04.ecommerce.common.exception.AppException;
import com.khahnm04.ecommerce.common.exception.ErrorCode;
import com.khahnm04.ecommerce.modules.user.dto.request.AddressRequest;
import com.khahnm04.ecommerce.modules.user.dto.response.AddressResponse;
import com.khahnm04.ecommerce.modules.user.entity.Address;
import com.khahnm04.ecommerce.modules.user.entity.Customer;
import com.khahnm04.ecommerce.modules.user.mapper.AddressMapper;
import com.khahnm04.ecommerce.modules.user.repository.AddressRepository;
import com.khahnm04.ecommerce.modules.user.service.AddressService;
import com.khahnm04.ecommerce.modules.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerService customerService;

    @Override
    @Transactional
    public AddressResponse createAddress(Long customerId, AddressRequest request) {
        Customer customer = customerService.getCustomerById(customerId);

        Address address = addressMapper.toAddress(request);
        address.setCustomer(customer);

        // Set as default if it's the first address or isDefault is true
        if (Boolean.TRUE.equals(request.isDefault()) || addressRepository.countByCustomerId(customerId) == 0) {
            addressRepository.clearDefaultAddressByCustomerId(customerId);
            address.setIsDefault(true);
        }

        Address savedAddress = addressRepository.save(address);
        log.info("Created new address {} for customer {}", savedAddress.getId(), customerId);
        return addressMapper.toResponse(savedAddress);
    }

    @Override
    public Address getAddressById(Long customerId, Long addressId) {
        return addressRepository.findByIdAndCustomerId(addressId, customerId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
    }

    @Override
    public List<AddressResponse> getAllAddressesByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId);

        List<Address> addresses = addressRepository.findAllByCustomerId(customerId);
        return addressMapper.toResponseList(addresses);
    }

    @Override
    public AddressResponse getAddressDetailById(Long customerId, Long addressId) {
        return addressMapper.toResponse(this.getAddressById(customerId, addressId));
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Long customerId, Long addressId, AddressRequest request) {
        Address address = this.getAddressById(customerId, addressId);

        addressMapper.updateAddress(address, request);
        Address updatedAddress = addressRepository.save(address);

        log.info("Updated address {} for customer {}", addressId, customerId);
        return addressMapper.toResponse(updatedAddress);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long customerId, Long addressId) {
        Address address = this.getAddressById(customerId, addressId);

        addressRepository.clearDefaultAddressByCustomerId(customerId);

        address.setIsDefault(true);
        addressRepository.save(address);
        log.info("Set address {} as default for customer {}", addressId, customerId);
    }

    @Override
    @Transactional
    public void deleteAddress(Long customerId, Long addressId) {
        Address address = this.getAddressById(customerId, addressId);

        boolean wasDefault = Boolean.TRUE.equals(address.getIsDefault());

        addressRepository.delete(address);
        log.info("Deleted address {} for customer {}", addressId, customerId);

        // If the deleted address was the default, set the first remaining address as
        // default
        if (wasDefault) {
            List<Address> remainingAddresses = addressRepository
                    .findAllByCustomerId(customerId);
            if (!remainingAddresses.isEmpty()) {
                Address firstAddress = remainingAddresses.get(0);
                firstAddress.setIsDefault(true);
                addressRepository.save(firstAddress);
                log.info("Set address {} as new default for customer {}", firstAddress.getId(), customerId);
            }
        }
    }

}
