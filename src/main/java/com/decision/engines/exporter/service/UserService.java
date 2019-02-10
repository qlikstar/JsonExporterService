package com.decision.engines.exporter.service;

import com.decision.engines.exporter.dto.UserDTO;
import com.decision.engines.exporter.model.Address;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private AddressService addressService;

    @Autowired
    public UserService(ModelMapper modelMapper,
                       UserRepository userRepository,
                       AddressService addressService) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    /**
     * This method returns the list of pageable users to the API caller
     *
     * @param pageable
     * @return pages of users
     */
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        List<User> userList = userRepository.findAll(pageable).getContent();
        List<UserDTO> userDTOList = userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(userDTOList, pageable, userDTOList.size());
    }

    /**
     * @param newUser
     * @return if an user exists, then returns it
     */
    private Optional<User> findOneObject(User newUser) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "address", "created_at", "updated_at");
        Example<User> searchUser = Example.of(newUser, matcher);
        return userRepository.findOne(searchUser);
    }

    /**
     * This method first checks if there is an address in the table. If yes, then associates the address with the new user
     * It also checks if the user exists. If yes, then updates the existing user with the new address.
     *
     * @param newUser
     * @return newly created user
     */
    @Transactional
    public User save(User newUser) {
        Address address = addressService.save(newUser.getAddress());
        newUser.setAddress(address);
        Optional<User> optionalUser = findOneObject(newUser);
        if (optionalUser.isPresent()) {
            User updateUser = optionalUser.get();
            updateUser.setAddress(address);
            return userRepository.save(updateUser);
        } else {
            return userRepository.save(newUser);
        }
    }
}
