package com.mgg.devicemanagement.dto.response;

import lombok.Data;
import lombok.Value;

@Value
public class DeviceResponseDto {
    public String name;
    public String brand;

    public static DeviceResponseDto from ( String name, String brand) {
        return new DeviceResponseDto(name, brand);
    }
}
