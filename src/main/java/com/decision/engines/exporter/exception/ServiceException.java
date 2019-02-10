package com.decision.engines.exporter.exception;

/**
 * Generic exception class for the service
 */
public class ServiceException extends Exception {
    public ServiceException(String errorMessage) {
        super(errorMessage);
    }
}