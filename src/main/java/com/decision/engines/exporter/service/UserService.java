package com.decision.engines.exporter.service;

import com.decision.engines.exporter.model.Address;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private AddressService addressService;

    @Autowired
    public UserService(UserRepository userRepository, AddressService addressService) {
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    public Set<User> getAllUsers() {
        return new HashSet<>(userRepository.findAll());
    }

    private Optional<User> findOneObject(User newUser) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "created_at", "updated_at");
        Example<User> searchUser = Example.of(newUser, matcher);
        return userRepository.findOne(searchUser);
    }

    @Transactional
    public User save(User newUser) {
        Address address = addressService.save(newUser.getAddress());
        newUser.setAddress(address);
        return findOneObject(newUser).orElseGet(() -> userRepository.save(newUser));
    }

}
