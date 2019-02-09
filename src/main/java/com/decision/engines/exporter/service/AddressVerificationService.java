package com.decision.engines.exporter.service;

import com.decision.engines.exporter.model.Address;

public interface AddressVerificationService {
    /**
     * @param address
     * @return true if the address is verified correctly
     */
    boolean verifyAddress(Address address);

    /**
     * @param address
     * @return the corrected address by calling the verification service
     */
    Address getCorrectedAddress(Address address);
}
