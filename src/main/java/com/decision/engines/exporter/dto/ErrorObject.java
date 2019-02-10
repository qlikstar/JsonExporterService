package com.decision.engines.exporter.dto;

import com.decision.engines.exporter.model.User;

import java.io.Serializable;

public class ErrorObject implements Serializable {
    private User user;
    private String error;

    public ErrorObject(User user, String error) {
        this.user = user;
        this.error = error;
    }

    public ErrorObject() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
