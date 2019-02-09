package com.decision.engines.exporter.dto;

import com.decision.engines.exporter.model.User;

public class ErrorObject {
    private User user;
    private String error;

    public ErrorObject(User user, String error) {
        this.user = user;
        this.error = error;
    }

    public ErrorObject() {
    }
}
