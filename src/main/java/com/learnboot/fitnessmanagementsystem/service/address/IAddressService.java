package com.learnboot.fitnessmanagementsystem.service.address;

import com.learnboot.fitnessmanagementsystem.domains.Address;
import com.learnboot.fitnessmanagementsystem.dto.AddressDto;

import java.util.List;

public interface IAddressService {
    AddressDto createAddress(AddressDto addressDto, long userId);

    List<AddressDto> getAddressesForUser(long userId);

    AddressDto getAddress(long addressId);

    AddressDto updateAddress(AddressDto addressDto, long addressId);

    void deleteAddress(long addressId);
}
