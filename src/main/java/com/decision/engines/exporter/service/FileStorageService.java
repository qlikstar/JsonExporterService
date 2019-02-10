package com.decision.engines.exporter.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * This method stores the file in the File system
     * @param file
     * @return the filename as string
     */
    public String storeFile(MultipartFile file);

    /**
     * This method gets the file as resource
     * @param fileName
     * @return
     */
    public Resource loadFileAsResource(String fileName);
}
