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
    private AddressVerificationService addressVerificationService;

    @Autowired
    public AddressService(AddressRepository addressRepository,
                          AddressVerificationService addressVerificationService) {
        this.addressRepository = addressRepository;
        this.addressVerificationService = addressVerificationService;
    }

    /**
     * @param pageable
     * @return all the pageable data from the repository
     */
    public Page<Address> getAllListedAddress(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    /**
     * @param newAddress
     * @return an option of the address. Could be an address or it could be null.
     */
    private Optional<Address> findOneObject(Address newAddress) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "created_at", "updated_at");
        Example<Address> searchContact = Example.of(newAddress, matcher);
        return addressRepository.findOne(searchContact);
    }

    public Address save(Address newAddress) {
        return findOneObject(newAddress).orElseGet(() -> {
            /**
             * Makes a remote call to the address verification service. In this case USPS
             */
            if (addressVerificationService.verifyAddress(newAddress))
                return addressRepository.save(newAddress);
            else {
                /**
                 * If the service wasn't able to verify the address, make another call to get the Corrected address
                 */
                Address verifiedAddress = addressVerificationService.getCorrectedAddress(newAddress);
                return addressRepository.save(verifiedAddress);
            }
        });
    }

}
