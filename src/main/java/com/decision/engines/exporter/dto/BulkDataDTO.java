package com.decision.engines.exporter.dto;

import com.decision.engines.exporter.enums.JobStatus;

import java.io.Serializable;
import java.util.UUID;

public class BulkDataDTO implements Serializable {
    private UUID id;

    private JobStatus jobStatus;

    private JobResultDTO result;

    public BulkDataDTO() {
    }

    public BulkDataDTO(UUID id, JobStatus jobStatus, JobResultDTO result) {
        this.id = id;
        this.jobStatus = jobStatus;
        this.result = result;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public JobResultDTO getResult() {
        return result;
    }

    public void setResult(JobResultDTO result) {
        this.result = result;
    }
}
