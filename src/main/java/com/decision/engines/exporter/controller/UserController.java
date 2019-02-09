package com.decision.engines.exporter.controller;

import com.decision.engines.exporter.api.APIName;
import com.decision.engines.exporter.api.APIResponse;
import com.decision.engines.exporter.api.ResponseUtil;
import com.decision.engines.exporter.dto.BulkDataDTO;
import com.decision.engines.exporter.exception.ServiceException;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.service.BulkDataService;
import com.decision.engines.exporter.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(APIName.API_VERSION)
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private ResponseUtil responseUtil;
    private UserService userService;
    private BulkDataService bulkDataService;

    @Autowired
    public UserController(ResponseUtil responseUtil,
                          UserService userService,
                          BulkDataService bulkDataService,
                          ApplicationEventPublisher applicationEventPublisher) {
        this.responseUtil = responseUtil;
        this.userService = userService;
        this.bulkDataService = bulkDataService;
    }

    @RequestMapping(path = APIName.USER, method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getUser(Pageable pageable) {
        return responseUtil.successResponse(userService.getAllUsers(pageable));
    }

    @RequestMapping(path = APIName.USER, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postUser(@RequestBody User user) {
        return responseUtil.successResponse(userService.save(user));
    }

    @RequestMapping(path = APIName.USER_BULK, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postBulkUsers(@RequestBody List<User> users) {
        BulkDataDTO bulkDataResponse = bulkDataService.saveAndProcess(users);
        return responseUtil.acceptedResponse(bulkDataResponse);
    }

    @RequestMapping(path = APIName.USER_BULK + "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getJobById(@RequestParam("uuid") String uuid) {
        try {
            return responseUtil.successResponse(bulkDataService.findBulkByRequestedUUID(UUID.fromString(uuid)));
        } catch (ServiceException e) {
            e.printStackTrace();
            return responseUtil.badRequestResponse(e.getMessage());
        }

    }


}