package com.decision.engines.exporter.service;

import com.decision.engines.exporter.model.Address;
import com.decision.engines.exporter.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Page<Address> getAllListedAddress(Pageable pageable) {
        return addressRepository.findAll(pageable);
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
