package com.decision.engines.exporter.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ResponseUtil {

    public ResponseEntity<APIResponse> buildResponse(APIStatus apiStatus, Object data, HttpStatus httpStatus) {
        return new ResponseEntity(new APIResponse(apiStatus, data), httpStatus);
    }

    public ResponseEntity<APIResponse> successResponse(Object data) {
        return buildResponse(APIStatus.OK, data, HttpStatus.OK);
    }

    public ResponseEntity<APIResponse> badRequestResponse(List<ParamErrors> errors) {
        Map<String, String> errMap = null;
        if (errors != null) {
            errMap = new HashMap<>();
            for (ParamErrors error : errors) {
                errMap.put(error.getName(), error.getDesc());
            }
        }
        return buildResponse(APIStatus.ERR_BAD_REQUEST, errMap, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<APIResponse> badRequestResponse(String errorMessage) {
        return buildResponse(APIStatus.ERR_BAD_REQUEST, errorMessage, HttpStatus.BAD_REQUEST);
    }
}
