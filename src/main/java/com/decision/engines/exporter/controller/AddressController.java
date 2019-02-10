package com.decision.engines.exporter.controller;

import com.decision.engines.exporter.api.APIName;
import com.decision.engines.exporter.api.APIResponse;
import com.decision.engines.exporter.api.ResponseUtil;
import com.decision.engines.exporter.model.Address;
import com.decision.engines.exporter.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a REST controller for Address.
 * Currently, there is no provision to delete an entity.
 * However, all the save operations guarantee that there is always 1 unique record for the address.
 */
@RestController
@RequestMapping(APIName.API_VERSION)
public class AddressController {

    private static final Logger LOG = LoggerFactory.getLogger(AddressController.class);

    private ResponseUtil responseUtil;
    private AddressService addressService;

    @Autowired
    public AddressController(ResponseUtil responseUtil, AddressService addressService) {
        this.responseUtil = responseUtil;
        this.addressService = addressService;
    }

    /**
     * @param pageable
     * @return a list of pageable addresses
     */
    @RequestMapping(path = APIName.ADDRESS, method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getAddress(Pageable pageable) {
        return responseUtil.successResponse(addressService.getAllListedAddress(pageable));
    }

    /**
     * @param address
     * @return the saved address with the relavant info like the id and the created and modified dates
     */
    @RequestMapping(path = APIName.ADDRESS, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postAddress(@RequestBody Address address) {
        LOG.info("Address : " + address);
        return responseUtil.successResponse(addressService.save(address));
    }
}