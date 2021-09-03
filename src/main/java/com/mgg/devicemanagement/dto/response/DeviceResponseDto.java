package com.mgg.devicemanagement.dto.response;

import lombok.Value;

@Value
public class DeviceResponseDto {
  public String name;
  public String brand;
  public String deviceKey;

  public static DeviceResponseDto from(String name, String brand, String deviceKey) {
    return new DeviceResponseDto(name, brand, deviceKey);
  }
}
