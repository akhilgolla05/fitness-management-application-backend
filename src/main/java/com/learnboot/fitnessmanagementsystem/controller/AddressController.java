package com.learnboot.fitnessmanagementsystem.controller;

import com.learnboot.fitnessmanagementsystem.domains.Address;
import com.learnboot.fitnessmanagementsystem.dto.AddressDto;
import com.learnboot.fitnessmanagementsystem.response.ApiResponse;
import com.learnboot.fitnessmanagementsystem.service.address.IAddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @PostMapping("/create-address/{userId}")
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse> addAddressForUser(@RequestBody AddressDto addressDto,
                                                         @PathVariable long userId) {
        AddressDto addressDto1 = addressService.createAddress(addressDto,userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Address Saved Successfully", addressDto1));
    }

    @GetMapping("/get-address-for-user/{userId}")
    public ResponseEntity<ApiResponse> getAddressForUser(@PathVariable long userId) {
        List<AddressDto> addressList = addressService.getAddressesForUser(userId);
        return ResponseEntity.ok(new ApiResponse("Address Found!",addressList));
    }

    @GetMapping("/get-address-by-id/{addressId}")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable long addressId) {
        AddressDto address = addressService.getAddress(addressId);
        return ResponseEntity.ok(new ApiResponse("Address Found!",address));
    }

    @PutMapping("/update-address/{addressId}")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable long addressId,
                                                      @RequestBody AddressDto addressDto) {
        AddressDto address = addressService.updateAddress(addressDto,addressId);
        return ResponseEntity.ok(new ApiResponse("Address Updated!",address));
    }

    @DeleteMapping("/delete-address/{addressId}")
    public ResponseEntity<ApiResponse> deleteAddressById(@PathVariable long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(new ApiResponse("Address Deleted!",null));
    }
}
