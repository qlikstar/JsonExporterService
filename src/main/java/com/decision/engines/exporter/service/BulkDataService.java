package com.decision.engines.exporter.service;

import com.decision.engines.exporter.dto.BulkDataDTO;
import com.decision.engines.exporter.dto.JobResultDTO;
import com.decision.engines.exporter.enums.DataFormat;
import com.decision.engines.exporter.enums.JobStatus;
import com.decision.engines.exporter.enums.UploadType;
import com.decision.engines.exporter.event.UserEvent;
import com.decision.engines.exporter.exception.ServiceException;
import com.decision.engines.exporter.model.Bulk;
import com.decision.engines.exporter.model.User;
import com.decision.engines.exporter.repository.BulkRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BulkDataService {

    private ApplicationEventPublisher applicationEventPublisher;
    private BulkRepository bulkRepository;

    @Autowired
    public BulkDataService(ApplicationEventPublisher applicationEventPublisher,
                           BulkRepository bulkRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.bulkRepository = bulkRepository;
    }

    public Bulk findBulkByUUID(UUID id) {
        return bulkRepository.findById(id);
    }

    public BulkDataDTO findBulkByRequestedUUID(UUID id) throws ServiceException {
        Bulk bulk = bulkRepository.findById(id);
        if (bulk != null) {
            return new BulkDataDTO(bulk.getId(), bulk.getJobStatus(), new Gson().fromJson(bulk.getResults(), JobResultDTO.class));
        } else {
            throw new ServiceException("Requested Job UUID not found: " + id);
        }
    }

    public BulkDataDTO saveAndProcess(List<User> users) {

        Gson gson = new Gson();
        Type type = new TypeToken<List<User>>() {
        }.getType();
        String jsonString = gson.toJson(users, type);
        JobResultDTO jobResultDTO = new JobResultDTO(users.size(), 0, 0, new ArrayList<>());
        Bulk bulkdata = new Bulk(DataFormat.JSON, jsonString, UploadType.DATABLOB, JobStatus.ACCEPTED, new Gson().toJson(jobResultDTO));
        Bulk bulk = bulkRepository.save(bulkdata);
        for (User user : users) {
            UserEvent userEvent = new UserEvent(this, user, bulk.getId());
            applicationEventPublisher.publishEvent(userEvent);
        }
        return new BulkDataDTO(bulk.getId(), bulk.getJobStatus(), new Gson().fromJson(bulk.getResults(), JobResultDTO.class));
    }

    public Bulk save(Bulk bulk) {
        return bulkRepository.save(bulk);
    }
}
