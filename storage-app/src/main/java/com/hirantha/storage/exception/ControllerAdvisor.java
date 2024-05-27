package com.hirantha.storage.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Object> handleApiException(ApiException exception) {

    log.error("ApiException occurred due to: {}", exception.toString());
    log.debug(ExceptionUtils.getStackTrace(exception));
    return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());

  }

}
