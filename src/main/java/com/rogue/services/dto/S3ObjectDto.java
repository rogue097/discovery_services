package com.rogue.services.dto;

import lombok.Data;

@Data
public class S3ObjectDto {
  private String bucketName;
  private String key;
  private Long size;
}
