package com.rogue.services.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateServiceRequestDto {
  private List<String> services;
}
