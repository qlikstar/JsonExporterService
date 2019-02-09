package com.decision.engines.exporter.exception;


public class ServiceException extends Exception {
    public ServiceException(String errorMessage) {
        super(errorMessage);
    }
}