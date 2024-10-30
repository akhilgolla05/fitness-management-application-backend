package com.learnboot.fitnessmanagementsystem.service.address;

import com.learnboot.fitnessmanagementsystem.domains.Address;
import com.learnboot.fitnessmanagementsystem.dto.AddressDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.repository.AddressRepository;
import com.learnboot.fitnessmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public AddressDto createAddress(AddressDto addressDto, long userId) {
        return userRepository.findById(userId)
                .map(user->{
                    Address address = new Address();
                    address.setStreet(addressDto.getStreet());
                    address.setCity(addressDto.getCity());
                    address.setState(addressDto.getState());
                    address.setZip(addressDto.getZip());
                    address.setCountry(addressDto.getCountry());
                    address.setUser(user);
                    Address dbAddress =  addressRepository.save(address);
                    return modelMapper.map(dbAddress, AddressDto.class);
                }).orElseThrow(()-> new ResourceNotFoundException("User Not Found!"));
    };

    @Override
    public List<AddressDto> getAddressesForUser(long userId) {
       List<Address> addressList= addressRepository.findByUserId(userId)
               .orElseThrow(()->new ResourceNotFoundException("User Not Found!"));
       List<AddressDto> addressDtos = addressList.stream()
               .map(address->modelMapper.map(address,AddressDto.class)).toList();
       return addressDtos;

    }

    @Override
    public AddressDto getAddress(long addressId) {
        return addressRepository.findById(addressId)
                .map(address->modelMapper.map(address,AddressDto.class))
                .orElseThrow(()-> new ResourceNotFoundException("Address Not Found!"));
    }

    @Override
    public AddressDto updateAddress(AddressDto addressDto, long addressId) {
        return addressRepository.findById(addressId)
                .map(address->{
                    address.setStreet(addressDto.getStreet());
                    address.setCity(addressDto.getCity());
                    address.setState(addressDto.getState());
                    address.setZip(addressDto.getZip());
                    address.setCountry(addressDto.getCountry());
                    Address updatedAddress =  addressRepository.save(address);
                    return modelMapper.map(updatedAddress, AddressDto.class);
                }).orElseThrow(()-> new ResourceNotFoundException("Address Not Found!"));
    }

    @Override
    public void deleteAddress(long addressId) {
        addressRepository.findById(addressId)
                .ifPresentOrElse(addressRepository::delete, ()->{
                    throw new ResourceNotFoundException("Address Not Found!");
                });
    }


}
