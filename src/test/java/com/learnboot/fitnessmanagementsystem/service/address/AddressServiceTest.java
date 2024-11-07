package com.learnboot.fitnessmanagementsystem.service.address;

import com.learnboot.fitnessmanagementsystem.domains.Address;
import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.AddressDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.repository.AddressRepository;
import com.learnboot.fitnessmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AddressService addressService;

    private User user;
    private Address address;
    private AddressDto addressDto;
    @BeforeEach
    public void beforeEach() {
         user = User.builder()
                .id(1L)
                .firstName("jack").lastName("joe")
                .email("jack@gmail.com")
                .userType("STUDENT").build();

        address = Address.builder()
                .id(1L)
                .user(user)
                .city("wichita").street("21st N")
                .state("KS").country("USA").zip("78654")
                .build();

         addressDto = AddressDto.builder()
                 .id(1L)
                .city("wichita").street("21st N")
                .state("KS").country("USA").zip("78654")
                .build();

    }

    @Test
    public void givenAddressObject_WhenCreate_returnAddress() {

        Mockito.when(userRepository.findById(ArgumentMatchers.any(Long.class)))
                .thenReturn(Optional.of(user));
        BDDMockito.given(addressRepository.save(ArgumentMatchers.any(Address.class)))
                .willReturn(address);
        Mockito.when(modelMapper.map(address, AddressDto.class))
                .thenReturn(addressDto);

        AddressDto dbAddress = addressService.createAddress(addressDto,user.getId());
        assertThat(dbAddress).isNotNull();
        assertThat(dbAddress).isEqualTo(addressDto);
        assertThat(dbAddress.getCity()).isEqualTo(addressDto.getCity());

    }

    @Test
    public void givenUserId_whenFindAllAddresses_returnAllAddress() {
        // Arrange
        Address address1 = Address.builder()
                .id(2L).user(user)
                .street("22nd st").city("wichita")
                .country("USA").zip("78654").build();

        AddressDto addressDto1 = AddressDto.builder()
                .id(2L)
                .street("22nd st").city("wichita")
                .country("USA").zip("78654").build();

        ArrayList<Address> arrayList = new ArrayList<>();
        arrayList.add(address1);
        arrayList.add(address);

        ArrayList<AddressDto> arrayDtoList = new ArrayList<>();
        arrayDtoList.add(addressDto1);
        arrayDtoList.add(addressDto);

        BDDMockito.given(addressRepository.findByUserId(ArgumentMatchers.any(Long.class)))
                .willReturn(Optional.of(arrayList));

        // Dynamically map each Address to its corresponding AddressDto
        Mockito.doAnswer(invocation -> {
            Address addressArg = invocation.getArgument(0);
            if (addressArg.getId() == 1L) return addressDto;
            else if (addressArg.getId() == 2L) return addressDto1;
            return null;
        }).when(modelMapper).map(ArgumentMatchers.any(Address.class), ArgumentMatchers.eq(AddressDto.class));

        // Act
        List<AddressDto> addressDtos = addressService.getAddressesForUser(user.getId());

        // Assert
        assertThat(addressDtos.size()).isEqualTo(2);
        assertThat(addressDtos).containsExactlyInAnyOrder(addressDto, addressDto1);
    }

    @Test
    public void givenUserId_whenFindAllAddresses_returnAllAddresses() {

        Address address1 = Address.builder()
                .id(2L)
                .user(user)
                .street("22nd st")
                .city("wichita")
                .country("USA")
                .zip("78654")
                .build();

        AddressDto addressDto1 = AddressDto.builder()
                .id(2L)
                .street("22nd st")
                .city("wichita")
                .country("USA")
                .zip("78654")
                .build();

        ArrayList<Address> arrayList = new ArrayList<>();
        arrayList.add(address1);
        arrayList.add(address);

        ArrayList<AddressDto> arrayDtoList = new ArrayList<>();
        arrayDtoList.add(addressDto1);
        arrayDtoList.add(addressDto);

        BDDMockito.given(addressRepository.findByUserId(ArgumentMatchers.any(Long.class)))
                .willReturn(Optional.of(arrayList));

        // Use lenient stubbing to handle any Address instance
        lenient().when(modelMapper.map(ArgumentMatchers.any(Address.class), ArgumentMatchers.eq(AddressDto.class)))
                .thenAnswer(invocation -> {
                    Address addr = invocation.getArgument(0);
                    return AddressDto.builder()
                            .id(addr.getId())
                            .street(addr.getStreet())
                            .city(addr.getCity())
                            .country(addr.getCountry())
                            .zip(addr.getZip())
                            .build();
                });

        List<AddressDto> addressDtos = addressService.getAddressesForUser(user.getId());

        assertThat(addressDtos.size()).isEqualTo(2);
        assertThat(addressDtos.get(0).getId()).isEqualTo(2L);
        assertThat(addressDtos.get(1).getId()).isEqualTo(1L);
    }

    @Test
    public void givenAddressObject_whenFindById_returnAddressObject(){

        BDDMockito.given(addressRepository.findById(ArgumentMatchers.any(Long.class)))
                .willReturn(Optional.of(address));
        BDDMockito.given(modelMapper.map(address, AddressDto.class))
                .willReturn(addressDto);
       AddressDto addressDto1 = addressService.getAddress(1L);
       assertThat(addressDto1).isNotNull();
    }

    @Test
    public void givenAddressObject_whenFindById_returnError(){
        assertThrows(ResourceNotFoundException.class,
                () -> addressService.getAddress(1L));
    }

    @Test
    public void givenUpdatedAddress_whenUpdate_returnUpdatedAddress(){

        Address updatedAddress = Address.builder()
                        .id(1L)
                        .city("fairmont").street("21st N")
                        .state("KS").country("USA").zip("78654")
                        .build();

         addressDto = AddressDto.builder()
                .id(1L)
                .city("fairmont").street("21st N")
                .state("KS").country("USA").zip("78654")
                .build();

        BDDMockito.given(addressRepository.findById(ArgumentMatchers.any(Long.class)))
                .willReturn(Optional.of(address));
        BDDMockito.given(addressRepository.save(ArgumentMatchers.any(Address.class)))
                .willReturn(updatedAddress);
        BDDMockito.given(modelMapper.map(updatedAddress, AddressDto.class))
                .willReturn(addressDto);

       AddressDto addressDto1 =  addressService.updateAddress(addressDto,user.getId());
       assertThat(addressDto1).isNotNull();
       assertThat(addressDto1.getCity()).isEqualTo(updatedAddress.getCity());
       assertThat(addressDto1.getStreet()).isEqualTo(updatedAddress.getStreet());

    }

    @Test
    public void givenAddressId_whenDelete_returnNothing(){
        BDDMockito.given(addressRepository.findById(ArgumentMatchers.any(Long.class)))
                .willReturn(Optional.of(address));
        BDDMockito.willDoNothing().given(addressRepository)
                .delete(ArgumentMatchers.any(Address.class));
        addressService.deleteAddress(1L);

        verify(addressRepository,times(1))
                .delete(ArgumentMatchers.any(Address.class));

    }





}