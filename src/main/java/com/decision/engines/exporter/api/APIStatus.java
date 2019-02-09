package com.decision.engines.exporter.api;

public enum APIStatus {

    OK(200, null),
    ACCEPTED(201, "Accepted"),
    ERR_INTERNAL_SERVER(500, "Internal Error"),
    SQL_ERROR(501, "SQL Error"),
    NOT_FOUND(404, "Not Found"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    ERR_BAD_REQUEST(400, "Bad request"),
    ERR_UPLOAD_FILE(900, "Upload file error."),
    ERR_REQUEST_FORBIDDEN(403, "Forbidden request");

    private final int code;
    private final String description;

    private APIStatus(int s, String v) {
        code = s;
        description = v;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
