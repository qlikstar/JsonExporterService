package com.decision.engines.exporter.service;

import com.decision.engines.exporter.model.Address;
import com.decision.engines.exporter.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Set<Address> getAllListedAddress() {
        return new HashSet<>(addressRepository.findAll());
    }

    private Optional<Address> findOneObject(Address newAddress) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "created_at", "updated_at");
        Example<Address> searchContact = Example.of(newAddress, matcher);
        return addressRepository.findOne(searchContact);
    }

    public Address save(Address newAddress) {
        return findOneObject(newAddress).orElseGet(() -> addressRepository.save(newAddress));
    }

}
