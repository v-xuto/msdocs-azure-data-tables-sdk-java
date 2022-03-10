package com.data.tables.handler;

import com.data.tables.common.Constants;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ExceptionHandler(Exception.class)
    public String handlerException(Exception exception) {
        logger.error("{} exception occurred, message: {}", exception.getClass(), exception.getMessage());
        return new JSONObject(new HashMap<String, Object>(){{
            put("code", Constants.FAILED_CODE);
            put("exception",exception.getClass().getSimpleName());
            put("msg", exception.getMessage());
        }}).toString();
    }
}
