package com.rogue.services.dto;

import lombok.Data;

@Data
public class S3ObjectCountDto {
    private String bucketName;
    private Long objectCount;
}
