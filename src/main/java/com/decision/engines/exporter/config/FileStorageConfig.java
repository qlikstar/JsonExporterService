package com.decision.engines.exporter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * File Storage configuration file
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageConfig {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
