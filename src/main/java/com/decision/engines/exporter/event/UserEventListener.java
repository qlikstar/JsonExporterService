package com.decision.engines.exporter.event;

import com.decision.engines.exporter.dto.ErrorObject;
import com.decision.engines.exporter.dto.JobResultDTO;
import com.decision.engines.exporter.enums.JobStatus;
import com.decision.engines.exporter.model.Bulk;
import com.decision.engines.exporter.service.BulkDataService;
import com.decision.engines.exporter.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserEventListener {

    private UserService userService;
    private BulkDataService bulkDataService;

    @Autowired
    public UserEventListener(UserService userService, BulkDataService bulkDataService) {
        this.userService = userService;
        this.bulkDataService = bulkDataService;
    }

    /**
     * Handle an application event.
     * @param event the event to respond to
     */
    @Async
    @EventListener
    public void handleUserEvent(UserEvent event) {

        boolean failed = false;
        ErrorObject errorObject = new ErrorObject();
        try {
            userService.save(event.getUser());
        } catch (Exception ex) {
            failed = true;
            errorObject = new ErrorObject(event.getUser(), ex.getLocalizedMessage());
        } finally {
            /**
             * Synchronized block is needed here as multiple thread try to access this block at the same time.
             */
            synchronized (this) {
                Bulk bulk = bulkDataService.findBulkByUUID(event.getId());
                JobResultDTO jobResult = new Gson().fromJson(bulk.getResults(), JobResultDTO.class);
                if (failed) {
                    // If any exception occurs, this block will report the exception to the use
                    jobResult.setNumberOfObjectsFailed(jobResult.getNumberOfObjectsFailed() + 1);
                    List<ErrorObject> errorObjects = jobResult.getFailedObjectList();
                    errorObjects.add(errorObject);
                    jobResult.setFailedObjectList(errorObjects);
                } else {
                    // If everything goes well
                    jobResult.setNumberOfObjectsProcessed(jobResult.getNumberOfObjectsProcessed() + 1);
                }
                bulk.setResults(new Gson().toJson(jobResult));
                bulk.setJobStatus(JobStatus.PROCESSED);
                System.out.println(bulk.getId());
                bulkDataService.save(bulk);
            }
        }
    }

}
