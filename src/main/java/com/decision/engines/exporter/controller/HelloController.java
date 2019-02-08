package com.decision.engines.exporter.controller;

import com.decision.engines.exporter.api.APIName;
import com.decision.engines.exporter.api.APIResponse;
import com.decision.engines.exporter.api.ResponseUtil;
import com.decision.engines.exporter.model.Address;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.service.AddressService;
import com.decision.engines.exporter.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIName.VERSION)
public class HelloController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    public ResponseUtil responseUtil;

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(path = "/address", method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postAddress(@RequestBody Address address) {
        LOG.info("Address : " + address);
        return responseUtil.successResponse(addressService.save(address));
    }

    @RequestMapping(path = "/address", method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getAddress() {
        return responseUtil.successResponse(addressService.getAllListedAddress());
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postUser(@RequestBody User user) {
        LOG.info("User : " + user);
        return responseUtil.successResponse(userService.save(user));
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getUser() {
        return responseUtil.successResponse(userService.getAllUsers());
    }

}