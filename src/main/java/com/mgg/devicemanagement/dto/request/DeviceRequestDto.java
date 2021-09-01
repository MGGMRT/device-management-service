package com.mgg.devicemanagement.dto.request;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeviceRequestDto {
    @NotNull
    private String deviceName;
    private String brandName;
}
