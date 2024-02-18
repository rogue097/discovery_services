package com.rogue.services.dto;

import com.rogue.services.constants.Services;
import lombok.Data;

@Data
public class ServiceDto {
  private String serviceName;
  private Services serviceType;
}
