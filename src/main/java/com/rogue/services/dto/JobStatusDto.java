package com.rogue.services.dto;

import com.rogue.services.constants.JobStatusType;
import lombok.Data;

@Data
public class JobStatusDto {
  private Long id;
  private JobStatusType status;
}
