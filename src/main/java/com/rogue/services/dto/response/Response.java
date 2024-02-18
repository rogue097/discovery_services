package com.rogue.services.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rogue.services.utility.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
  private final long timestamp = DateTimeUtils.now();
  private String message;
  private T data;
  private List<String> errors;

  public static class ResponseBuilder<T> {
    private T data;
    private String message = "success";

    @Override
    public String toString() {
      return "ResponseBuilder {" +
              "data=" + data +
              ", message='" + message + '\'' +
              ", errors=" + errors +
              '}';
    }

    public ResponseBuilder data(T data) {
      this.data = data;
      return this;
    }
  }

}

