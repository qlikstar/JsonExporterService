package com.decision.engines.exporter.model;

import com.decision.engines.exporter.enums.DataFormat;
import com.decision.engines.exporter.enums.JobStatus;
import com.decision.engines.exporter.enums.UploadType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "bulk")
public class Bulk extends DateAudit {

    private static final long serialVersionUID = 1993497570522025038L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    private DataFormat dataFormat;

    @Column(columnDefinition = "TEXT")
    private String data;

    @Column
    @Enumerated(EnumType.STRING)
    private UploadType uploadType;

    @Column
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @Column
    private String results;

    public Bulk(DataFormat dataFormat, String data, UploadType uploadType, JobStatus jobStatus, String results) {
        this.dataFormat = dataFormat;
        this.data = data;
        this.uploadType = uploadType;
        this.jobStatus = jobStatus;
        this.results = results;
    }

    public Bulk() {
    }

    public UUID getId() {
        return id;
    }

    public DataFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(DataFormat dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public UploadType getUploadType() {
        return uploadType;
    }

    public void setUploadType(UploadType uploadType) {
        this.uploadType = uploadType;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
