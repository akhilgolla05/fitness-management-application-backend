package com.learnboot.fitnessmanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnboot.fitnessmanagementsystem.domains.Address;
import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.AddressDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.service.address.AddressService;
import com.learnboot.fitnessmanagementsystem.service.address.IAddressService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AddressController.class)
@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AddressService addressService;

    @Autowired
    private ObjectMapper objectMapper;

    private Address address;
    private AddressDto addressDto;
    private User user;
    @BeforeEach
    public void beforeEach() {
         user = User.builder()
                .id(1L)
                .userType("TRAINER").firstName("munna")
                .lastName("kamal").email("munna@gmail.com")
                .build();
        address = Address.builder()
                .id(1L).street("21st N")
                .user(user)
                .city("wichita").state("KS")
                .country("USA").zip("503187").build();

        addressDto = AddressDto.builder()
                .id(1L).street("21st N")
                .city("wichita").state("KS")
                .country("USA").zip("503187").build();
    }

    @Test
    public void givenAddress_whenCreate_returnAddressDto() throws Exception {

        // Set up the mock behavior to return this addressDto when called
        BDDMockito
                .given(addressService.createAddress(ArgumentMatchers.any(AddressDto.class),
                        ArgumentMatchers.anyLong()))
                .willReturn(addressDto);

        // Perform the mock request
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/address/create-address/{userId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto))
        );
        // Verify the response
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Address Saved Successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.street").value("21st N"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.city").value(address.getCity()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.state").value("KS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.zip").value("503187"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.country").value("USA"));
    }

    @Test
    public void givenUserId_whenFind_returnListOfAddressDto() throws Exception {
       Address address2 = Address.builder()
                .id(2L).street("22st N")
                .user(user)
                .city("fairmont").state("KS")
                .country("USA").zip("55555").build();

        AddressDto addressDto2 = AddressDto.builder()
                .id(2L).street("22st N")
                .city("fairmont").state("KS")
                .country("USA").zip("55555").build();

        ArrayList<AddressDto> addressDtos = new ArrayList<>();
        addressDtos.add(addressDto2);
        addressDtos.add(addressDto);

        BDDMockito.given(addressService.getAddressesForUser(ArgumentMatchers.anyLong()))
                .willReturn(addressDtos);

        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/address/get-address-for-user/{userId}",1L));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].street").value("22st N"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].city").value("fairmont"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.size()").value(CoreMatchers.is(addressDtos.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void givenUserId_whenFind_returnError() throws Exception {
        BDDMockito.given(addressService.getAddressesForUser(3L))
                .willThrow(ResourceNotFoundException.class);
        ResultActions response = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/address/get-address-for-user/{userId}",3L));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void givenUserId_whenDelete_returnNothing() throws Exception {

        BDDMockito.willDoNothing().given(addressService).deleteAddress(ArgumentMatchers.anyLong());

       ResultActions resultActions =
               mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/address/delete-address/{addressId}",1L));

       resultActions.andExpect(MockMvcResultMatchers.status().isOk())
               .andDo(MockMvcResultHandlers.print());

    }
}

