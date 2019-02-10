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
import com.decision.engines.exporter.service.BulkDataService;
import com.decision.engines.exporter.service.FileStorageService;
import com.decision.engines.exporter.service.UserService;
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

@RestController
@RequestMapping(APIName.API_VERSION)
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private ResponseUtil responseUtil;
    private ModelMapper modelMapper;
    private UserService userService;
    private BulkDataService bulkDataService;
    private FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public UserController(ResponseUtil responseUtil,
                          ModelMapper modelMapper,
                          UserService userService,
                          BulkDataService bulkDataService,
                          FileStorageService fileStorageService) {
        this.responseUtil = responseUtil;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.bulkDataService = bulkDataService;
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(path = APIName.USER, method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getUser(Pageable pageable) {

        return responseUtil.successResponse(userService.getAllUsers(pageable));
    }

    @RequestMapping(path = APIName.USER, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postUser(@RequestBody UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        UserDTO userDTOResponse = modelMapper.map(userService.save(user), UserDTO.class);
        return responseUtil.successResponse(userDTOResponse);
    }

    @RequestMapping(path = APIName.USER_BULK, method = RequestMethod.POST)
    public ResponseEntity<APIResponse> postBulkUsers(@RequestBody List<UserDTO> userDTOs) {

        BulkDataDTO bulkDataResponse = bulkDataService.saveAndProcess(userDTOs, DataFormat.JSON, UploadType.DATABLOB);
        return responseUtil.acceptedResponse(bulkDataResponse);
    }

    @RequestMapping(path = APIName.USER_BULK + "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<APIResponse> getJobById(@PathVariable("uuid") String uuid) {
        try {
            return responseUtil.successResponse(bulkDataService.findBulkByRequestedUUID(UUID.fromString(uuid)));
        } catch (ServiceException e) {
            e.printStackTrace();
            return responseUtil.badRequestResponse(e.getMessage());
        }
    }

    @RequestMapping(path = APIName.USER_BULK + "/import", method = RequestMethod.POST)
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


//    public ResponseEntity<APIResponse> uploadFile(@RequestParam("file") MultipartFile file) {
//        String fileExtension = getFileExtension(file);
//        String filename = "File_" + new Timestamp(System.currentTimeMillis()).getTime();
//        File targetFile = getTargetFile(fileExtension, filename);
//        String filePath = targetFile.getAbsolutePath();
//
//        LOG.info("File downloaded : " + filePath);
//
//
//
//    }

//    private File getTargetFile(String fileExtn, String fileName) {
//        File targetFile = new File(uploadDir + fileName + fileExtn);
//        return targetFile;
//    }

    private String getFileExtension(MultipartFile inFile) {
        String fileExtention = inFile.getOriginalFilename().substring(inFile.getOriginalFilename().lastIndexOf('.'));
        return fileExtention;
    }


}