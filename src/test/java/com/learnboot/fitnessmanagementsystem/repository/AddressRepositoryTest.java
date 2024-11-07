package com.learnboot.fitnessmanagementsystem.repository;


import com.learnboot.fitnessmanagementsystem.domains.Address;
import com.learnboot.fitnessmanagementsystem.domains.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    private  AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    private Address address;
    private User user;
    @BeforeEach
    public void beforeEachTest() {
         user = User.builder()
                .email("munna@gmail.com")
                .userType("STUDENT").firstName("munna")
                .lastName("bhai").build();

         address = Address.builder()
                .city("Plano").state("texas")
                .country("USA").zip("78025")
                .user(user).build();
    }

    @Test
    public void givenAddressObject_WhenSave_ThenReturnAddressObject() {
        Address savedAddress = addressRepository.save(address);
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isGreaterThan(0);
        assertThat(savedAddress.getCountry()).isEqualTo("USA");
        assertThat(savedAddress.getState()).isEqualTo("texas");
        assertThat(savedAddress.getZip()).isEqualTo("78025");
    }

    @Test
    public void givenAddressObject_WhenFindById_ThenReturnAddressObject() {
           Address savedAddress =  addressRepository.save(address);
           Address addressById =  addressRepository.findById(savedAddress.getId()).get();
           assertThat(addressById).isNotNull();
    }

    @Test
    public void givenAddressObject_WhenFindById_ThenThrowsError() {
        assertThrows(NoSuchElementException.class,()->addressRepository.findById(2L).get());
    }

    @Test
    public void givenAddressObject_WhenFindByUserId_ThenReturnAddressObject() {
        User savedUser = userRepository.save(user);
        Address savedAddress =  addressRepository.save(address);
        List<Address> addressById =  addressRepository.findByUserId(savedAddress.getUser().getId()).get();
        assertThat(addressById).isNotNull();
        assertThat(addressById.get(0).getUser())
                .extracting(User::getEmail).isEqualTo("munna@gmail.com");
    }

    @Test
    public void givenAddressObject_WhenFindByUserId_ThenThrowsError() {
           List<Address> addressList = addressRepository.findByUserId(2L).get();
            assertThat(addressList).isEmpty();
    }
}
