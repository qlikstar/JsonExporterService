package com.decision.engines.exporter.service;

import com.decision.engines.exporter.dto.BulkDataDTO;
import com.decision.engines.exporter.dto.JobResultDTO;
import com.decision.engines.exporter.dto.UserDTO;
import com.decision.engines.exporter.enums.DataFormat;
import com.decision.engines.exporter.enums.JobStatus;
import com.decision.engines.exporter.enums.UploadType;
import com.decision.engines.exporter.event.UserEvent;
import com.decision.engines.exporter.exception.ServiceException;
import com.decision.engines.exporter.model.Bulk;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.repository.BulkRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BulkDataService {

    private ApplicationEventPublisher applicationEventPublisher;
    private BulkRepository bulkRepository;
    private ModelMapper modelMapper;

    @Autowired
    public BulkDataService(ApplicationEventPublisher applicationEventPublisher,
                           BulkRepository bulkRepository,
                           ModelMapper modelMapper) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.bulkRepository = bulkRepository;
        this.modelMapper = modelMapper;
    }

    private static List<UserDTO> readFileDataFromJson(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        List<UserDTO> userDTOlist = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
            ObjectMapper mapper = new ObjectMapper();
            userDTOlist = mapper.readValue(contentBuilder.toString(), new TypeReference<List<UserDTO>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userDTOlist;
    }

    public Bulk findBulkByUUID(UUID id) {
        return bulkRepository.findById(id);
    }

    public Bulk save(Bulk bulk) {
        return bulkRepository.save(bulk);
    }

    public BulkDataDTO findBulkByRequestedUUID(UUID id) throws ServiceException {
        Bulk bulk = bulkRepository.findById(id);
        if (bulk != null) {
            return new BulkDataDTO(bulk.getId(), bulk.getJobStatus(), new Gson().fromJson(bulk.getResults(), JobResultDTO.class));
        } else {
            throw new ServiceException("Requested Job UUID not found: " + id);
        }
    }

    public BulkDataDTO saveAndProcess(List<UserDTO> userDTOs, DataFormat dataFormat, UploadType uploadType) {

        // Gets the list of users from UserDTO
        List<User> users = userDTOs.stream()
                .map(userDTO -> modelMapper.map(userDTO, User.class))
                .collect(Collectors.toList());

        // Writes the entry to the DB table
        Gson gson = new Gson();
        Type type = new TypeToken<List<User>>() {
        }.getType();
        String jsonString = gson.toJson(users, type);
        JobResultDTO jobResultDTO = new JobResultDTO(users.size(), 0, 0, new ArrayList<>());
        Bulk bulkdata = new Bulk(dataFormat, jsonString, uploadType, JobStatus.ACCEPTED, new Gson().toJson(jobResultDTO));
        Bulk bulk = bulkRepository.save(bulkdata);

        // Makes an async event call to the EventPublisher
        for (User user : users) {
            UserEvent userEvent = new UserEvent(this, user, bulk.getId());
            applicationEventPublisher.publishEvent(userEvent);
        }
        return new BulkDataDTO(bulk.getId(), bulk.getJobStatus(), new Gson().fromJson(bulk.getResults(), JobResultDTO.class));
    }

    public BulkDataDTO saveAndProcessFile(String filePath, DataFormat dataFormat) throws ServiceException {

        List<UserDTO> users = readDataFromFile(filePath, dataFormat);
        return saveAndProcess(users, DataFormat.JSON, UploadType.FILE);
    }

    private List<UserDTO> readDataFromFile(String filePath, DataFormat dataFormat) throws ServiceException {
        List<UserDTO> userDTOList;
        if (dataFormat.equals(DataFormat.JSON))
            userDTOList = readFileDataFromJson(filePath);
        else {
            throw new ServiceException("File type not supported: " + dataFormat);
        }
        return userDTOList;
    }

}
