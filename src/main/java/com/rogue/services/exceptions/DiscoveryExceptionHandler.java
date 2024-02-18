package com.rogue.services.exceptions;

import com.rogue.services.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class DiscoveryExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {DiscoveryException.class})
  protected ResponseEntity<Object> handleDiscoveryException(DiscoveryException ex) {
    Response<Object> build = Response.builder()
            .message("error")
            .errors(List.of(ex.getMessage())).build();

    return buildResponseEntity(build, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<Object> buildResponseEntity(Response<Object> payload, HttpStatus status) {
    return new ResponseEntity<>(payload, status);
  }

}
