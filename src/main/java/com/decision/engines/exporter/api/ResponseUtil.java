package com.decision.engines.exporter.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    private ResponseEntity<APIResponse> buildResponse(APIStatus apiStatus, Object data, HttpStatus httpStatus) {
        return new ResponseEntity(new APIResponse(apiStatus, data), httpStatus);
    }

    public ResponseEntity<APIResponse> successResponse(Object data) {
        return buildResponse(APIStatus.OK, data, HttpStatus.OK);
    }

    public ResponseEntity<APIResponse> acceptedResponse(Object data) {
        return buildResponse(APIStatus.ACCEPTED, data, HttpStatus.ACCEPTED);
    }

    public ResponseEntity<APIResponse> createdResponse(Object data) {
        return buildResponse(APIStatus.CREATED, data, HttpStatus.CREATED);
    }

    public ResponseEntity<APIResponse> badRequestResponse(String errorMessage) {
        return buildResponse(APIStatus.ERR_BAD_REQUEST, errorMessage, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<APIResponse> unprocessableEntityResponse(String errorMessage) {
        return buildResponse(APIStatus.UNPROCESSABLE_ENTITY, errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
