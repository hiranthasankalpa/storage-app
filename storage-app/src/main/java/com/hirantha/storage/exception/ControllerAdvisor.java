package com.hirantha.storage.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Object> handleApiException(ApiException ex) {

    log.error("ApiException occurred due to: {}", ex.toString());
    return new ResponseEntity<>(ex.getMessage(), ex.getHttpStatus());

  }

}
