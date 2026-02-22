package com.khahnm04.ecommerce.modules.user.service;

import com.khahnm04.ecommerce.modules.user.dto.request.AddressRequest;
import com.khahnm04.ecommerce.modules.user.dto.response.AddressResponse;
import com.khahnm04.ecommerce.modules.user.entity.Address;
import java.util.List;

public interface AddressService {

    AddressResponse createAddress(Long customerId, AddressRequest request);

    List<AddressResponse> getAddressesByCustomerId(Long customerId);

    Address getAddressById(Long customerId, Long addressId);

    AddressResponse getAddressDetailById(Long customerId, Long addressId);

    AddressResponse updateAddress(Long customerId, Long addressId, AddressRequest request);

    void setDefaultAddress(Long customerId, Long addressId);

    void deleteAddress(Long customerId, Long addressId);

}
