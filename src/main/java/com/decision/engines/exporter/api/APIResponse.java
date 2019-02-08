package com.decision.engines.exporter.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T extends Object> implements Serializable {

    private int status;
    private String message;
    private T data;

    public APIResponse(APIStatus apiStatus, T data) {

        if (apiStatus == null) {
            throw new IllegalArgumentException("APIStatus must not be null");
        }

        this.status = apiStatus.getCode();
        this.message = apiStatus.getDescription();
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
