package com.decision.engines.exporter.service;

import com.decision.engines.exporter.model.Address;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private AddressService addressService;

    @Autowired
    public UserService(UserRepository userRepository, AddressService addressService) {
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
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
