package com.khahnm04.ecommerce.service.user;

import com.khahnm04.ecommerce.dto.request.user.AddressRequest;
import com.khahnm04.ecommerce.dto.response.user.AddressResponse;
import com.khahnm04.ecommerce.entity.user.Address;
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
