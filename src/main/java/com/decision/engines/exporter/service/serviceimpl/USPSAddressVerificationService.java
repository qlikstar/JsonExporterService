package com.decision.engines.exporter.service.serviceimpl;

import com.decision.engines.exporter.model.Address;
import com.decision.engines.exporter.service.AddressVerificationService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * This is a dummy Address Verification Service that validates the address from an external service
 */
@Primary
@Service
public class USPSAddressVerificationService implements AddressVerificationService {

    @Override
    public boolean verifyAddress(Address address) {
        /**
         * Returns true if the address is verified with the service.
         */
        return true;
    }

    @Override
    public Address getCorrectedAddress(Address address) {
        /**
         * Make a remote call to USPS to verify the address
         */
        return address;
    }
}
