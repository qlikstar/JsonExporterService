package com.decision.engines.exporter.controller;

import com.decision.engines.exporter.api.APIName;
import com.decision.engines.exporter.api.APIResponse;
import com.decision.engines.exporter.api.ResponseUtil;
import com.decision.engines.exporter.dto.BulkDataDTO;
import com.decision.engines.exporter.dto.UserDTO;
import com.decision.engines.exporter.enums.DataFormat;
import com.decision.engines.exporter.enums.UploadType;
import com.decision.engines.exporter.exception.ServiceException;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.service.impl.BulkDataService;
import com.decision.engines.exporter.service.impl.LocalFileStorageService;
import com.decision.engines.exporter.service.impl.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

/**
 * This is primarily the main controller file of the project. It hosts the key API endpoints
 * <p>
 * 1. Create user: Creates the user only if there isn't one existing already. If the address has changed,
 * then it creates a new address record and updates the existing user record with the new address id.
 * <p>
 * 2. '/user/bulk' endpoint takes in an array of users and writes the data on to the 'bulk' table,
 * makes an asynchronous call to process the data received and returns back an "201: ACCEPTED" status
 * to the API caller, with the UUID of the bulk record.
 * <p>
 * 3. As the async job runs, it processes the data in the background, and updates the result table, with
 * the success and error count, and with the error details, if there are any.
 * <p>
 * 4. Now, this data can be queried with the '/user/bulk/{uuid}' endpoints, to see the status.
 * <p>
 * 5. We also expose an endpoint '/user/bulk/import' which takes in a JSON file and processes the same way as
 * the 'bulk' endpoint. The status of the job can also be queried at a later point by using the  'bulk'
 * endpoint.
 * <p>
 * 6. This file also exposes the future support to import CSV / XML data through the '/import' endpoints.
 */
@RestController
@RequestMapping(APIName.API_VERSION)
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private ResponseUtil responseUtil;
    private ModelMapper modelMapper;
    private UserService userService;
    private BulkDataService bulkDataService;
    private LocalFileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public UserController(ResponseUtil responseUtil,
                          ModelMapper modelMapper,
                          UserService userService,
                          BulkDataService bulkDataService,
                          LocalFileStorageService fileStorageService) {
        this.responseUtil = responseUtil;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.bulkDataService = bulkDataService;
        this.fileStorageService = fileStorageService;
    }

    /**
     * @param pageable
     * @return a pageable list of users
     */
    @RequestMapping(path = APIName.USER, method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getUser(Pageable pageable) {

        return responseUtil.successResponse(userService.getAllUsers(pageable));
    }

    /**
     * @param userDTO
     * @return the saved user with the ID and the created and modified timestamps
     */
    @RequestMapping(path = APIName.USER, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postUser(@RequestBody UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        UserDTO userDTOResponse = modelMapper.map(userService.save(user), UserDTO.class);
        return responseUtil.createdResponse(userDTOResponse);
    }

    /**
     * This endpoint basically takes a list of users to be uploaded. It then makes an async call to process the
     * data and saves the list to the 'bulk' table in the Database with the initial results.
     *
     * @param userDTOs
     * @return the Bulk data response that holds the UUID of the accepted request and the status.
     */
    @RequestMapping(path = APIName.USER_BULK, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postBulkUsers(@RequestBody List<UserDTO> userDTOs) {

        BulkDataDTO bulkDataResponse = bulkDataService.saveAndProcess(userDTOs, DataFormat.JSON, UploadType.DATABLOB);
        return responseUtil.acceptedResponse(bulkDataResponse);
    }

    /**
     * This endpoiint fetches the status of the bulk data UUID. It consists of the following:
     * 1. No of records in the request
     * 2. No of records successfully processed
     * 3. No of records that failed to be processed with the error message
     * 4. Status of the job.
     *
     * @param uuid
     * @return the BulkDataDTO for the requested UUID
     */
    @RequestMapping(path = APIName.USER_BULK + "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getJobById(@PathVariable("uuid") String uuid) {
        try {
            return responseUtil.successResponse(bulkDataService.findBulkByRequestedUUID(UUID.fromString(uuid)));
        } catch (ServiceException e) {
            e.printStackTrace();
            return responseUtil.badRequestResponse(e.getMessage());
        }
    }

    /**
     * This endpoint basically does the same, except that it takes in a JSON file to be imported.
     * This controller can be further extended to process XML / CSV files.
     *
     * @param file
     * @return the Bulk data response that holds the UUID of the accepted request and the status.
     */
    @RequestMapping(path = APIName.USER_BULK_IMPORT, method = RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        String fileExtension = getFileExtension(file);
        String filePath = uploadDir + "/" + fileName;

        BulkDataDTO bulkDataResponse;
        try {
            if (fileExtension.equals(".json")) {
                bulkDataResponse = bulkDataService.saveAndProcessFile(filePath, DataFormat.JSON);
            } else if (fileExtension.equals(".csv")) {
                bulkDataResponse = bulkDataService.saveAndProcessFile(filePath, DataFormat.CSV);
            } else if (fileExtension.equals(".xml")) {
                bulkDataResponse = bulkDataService.saveAndProcessFile(filePath, DataFormat.XML);
            } else {
                return responseUtil.badRequestResponse("Invalid file type uploaded: " + fileExtension);
            }
        } catch (ServiceException serEx) {
            return responseUtil.badRequestResponse(serEx.getMessage());
        }
        return responseUtil.successResponse(bulkDataResponse);
    }

    private String getFileExtension(MultipartFile inFile) {
        return inFile.getOriginalFilename().substring(inFile.getOriginalFilename().lastIndexOf('.'));
    }


}