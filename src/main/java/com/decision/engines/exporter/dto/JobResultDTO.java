package com.decision.engines.exporter.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Job Result DTO holds the following:
 * 1. No of records in the request
 * 2. No of records successfully processed
 * 3. No of records that failed to be processed with the error message
 * 4. Status of the job.
 */

public class JobResultDTO implements Serializable {
    private Integer totalNoOfObjects;
    private Integer numberOfObjectsProcessed;
    private Integer numberOfObjectsFailed;
    private List<ErrorObject> failedObjectList;

    public JobResultDTO() {
    }

    public JobResultDTO(Integer totalNoOfObjects, Integer numberOfObjectsProcessed, Integer numberOfObjectsFailed, List<ErrorObject> failedObjectList) {
        this.totalNoOfObjects = totalNoOfObjects;
        this.numberOfObjectsProcessed = numberOfObjectsProcessed;
        this.numberOfObjectsFailed = numberOfObjectsFailed;
        this.failedObjectList = failedObjectList;
    }

    public Integer getTotalNoOfObjects() {
        return totalNoOfObjects;
    }

    public void setTotalNoOfObjects(Integer totalNoOfObjects) {
        this.totalNoOfObjects = totalNoOfObjects;
    }

    public Integer getNumberOfObjectsProcessed() {
        return numberOfObjectsProcessed;
    }

    public void setNumberOfObjectsProcessed(Integer numberOfObjectsProcessed) {
        this.numberOfObjectsProcessed = numberOfObjectsProcessed;
    }

    public Integer getNumberOfObjectsFailed() {
        return numberOfObjectsFailed;
    }

    public void setNumberOfObjectsFailed(Integer numberOfObjectsFailed) {
        this.numberOfObjectsFailed = numberOfObjectsFailed;
    }

    public List<ErrorObject> getFailedObjectList() {
        return failedObjectList;
    }

    public void setFailedObjectList(List<ErrorObject> failedObjectList) {
        this.failedObjectList = failedObjectList;
    }
}
