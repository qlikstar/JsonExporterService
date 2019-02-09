package com.decision.engines.exporter.dto;

import java.util.List;

public class JobResultDTO {
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
