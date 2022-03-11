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
        return null;
    }
}
