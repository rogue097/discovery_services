package com.rogue.services.constants;

import lombok.Getter;

@Getter
public enum Region {
  AP_SOUTH_1("ap-south-1");

  private final String region;

  Region(String region) {
    this.region = region;
  }

  public static Region fromName(String region) {
    for (Region r : Region.values()) {
      if (r.getRegion().equals(region)) {
        return r;
      }
    }
    return null;
  }
}
