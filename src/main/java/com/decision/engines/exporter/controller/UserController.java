package com.decision.engines.exporter.controller;

import com.decision.engines.exporter.api.APIName;
import com.decision.engines.exporter.api.APIResponse;
import com.decision.engines.exporter.api.ResponseUtil;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(APIName.API_VERSION)
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private ResponseUtil responseUtil;
    private UserService userService;

    @Autowired
    public UserController(ResponseUtil responseUtil, UserService userService) {
        this.responseUtil = responseUtil;
        this.userService = userService;
    }

    @RequestMapping(path = APIName.USER, method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getUser(Pageable pageable) {
        return responseUtil.successResponse(userService.getAllUsers(pageable));
    }

    @RequestMapping(path = APIName.USER, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postUser(@RequestBody User user) {
        try {
            return responseUtil.successResponse(userService.save(user));
        } catch (Exception err) {
            return responseUtil.unprocessableEntityResponse("Unable to parse message : " + err.getLocalizedMessage());
        }
    }

    @RequestMapping(path = APIName.USER_BULK, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postUsers(@RequestBody List<User> users) {
        LOG.info("Users : " + users);
        try {
            return responseUtil.successResponse(users);
        } catch (Exception err) {
            return responseUtil.unprocessableEntityResponse("Unable to parse message : " + err.getLocalizedMessage());
        }
    }

}