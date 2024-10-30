package com.learnboot.fitnessmanagementsystem.service.user;

import com.learnboot.fitnessmanagementsystem.Utils.UserCreation;
import com.learnboot.fitnessmanagementsystem.domains.Address;
import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.AddressDto;
import com.learnboot.fitnessmanagementsystem.dto.UserDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.repository.AddressRepository;
import com.learnboot.fitnessmanagementsystem.repository.UserRepository;
import com.learnboot.fitnessmanagementsystem.request.UserCreationRequest;
import com.learnboot.fitnessmanagementsystem.request.UserUpdateRequest;
import com.learnboot.fitnessmanagementsystem.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserCreation userCreation;
    private final ModelMapper modelMapper;
    private final AddressService addressService;
    private final AddressRepository addressRepository;

    @Override
    public UserDto createUser(UserCreationRequest request) {
        log.info("UserService :: createUser");
        User user =  userCreation.createUser(request);
        AddressDto addressDto = addressService.createAddress(request.getAddress(),user.getId());
        UserDto userDto =  modelMapper.map(user, UserDto.class);
        userDto.setAddress(addressService.getAddressesForUser(user.getId()));
        return userDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("UserService :: getUserById");
        return userRepository.findById(userId)
                .map(user -> {
                    UserDto userDto = modelMapper.map(user, UserDto.class);
                    userDto.setAddress(addressService.getAddressesForUser(userId));
                    return userDto;
                })
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public UserDto updateUser(UserUpdateRequest request, long userId) {
        log.info("UserService :: updateUser");
        return userRepository.findById(userId)
                .map(user->{
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setAge(request.getAge());
                    user.setBirthDate(request.getBirthDate());
                    user.setGender(request.getGender());
                    user.setPhoneNumber(request.getPhoneNumber());
                    user.setSpecialization(request.getSpecialization());
                    User updatedUser =  userRepository.save(user);
                    return modelMapper.map(updatedUser, UserDto.class);
                })
                .orElseThrow(()-> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        log.info("UserService :: deleteUser");
        userRepository.findById(userId)
                .ifPresentOrElse(user->{
                    Optional<List<Address>> addressList = addressRepository.findByUserId(userId);
                    addressList.ifPresent(addresses -> addresses
                                    .forEach(address -> {addressService.deleteAddress(address.getId());})
                            );
                    userRepository.delete(user);
                }, () -> {
                    throw new ResourceNotFoundException("User Not Found");
                });
        log.info("User Deleted Successfully");
    }

    @Override
    public List<UserDto> getAllUsers(){
        log.info("UserService :: getAllUsers");
        return userRepository.findAll()
                .stream().map(user->modelMapper.map(user,UserDto.class)).toList();
    }
}
