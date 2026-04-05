package com.khahnm04.ecommerce.service;

import com.khahnm04.ecommerce.dto.request.AddressRequest;
import com.khahnm04.ecommerce.dto.response.AddressResponse;
import com.khahnm04.ecommerce.entity.Address;
import java.util.List;

public interface AddressService {

    AddressResponse createAddress(Long customerId, AddressRequest request);

    Address getAddressById(Long customerId, Long addressId);

    List<AddressResponse> getAllAddressesByCustomerId(Long customerId);

    AddressResponse getAddressDetailById(Long customerId, Long addressId);

    AddressResponse updateAddress(Long customerId, Long addressId, AddressRequest request);

    void setDefaultAddress(Long customerId, Long addressId);

    void deleteAddress(Long customerId, Long addressId);

}
