package com.mgg.devicemanagement.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;

import static com.mgg.devicemanagement.constants.DeviceManagementServiceConstants.MAX_SIZE_OF_DEVICE_NAME;
import static com.mgg.devicemanagement.constants.DeviceManagementServiceConstants.MIN_SIZE_OF_DEVICE_NAME;

@AllArgsConstructor
@Data
public class DeviceRequestDto {
  @NotNull
  @Size(min = MIN_SIZE_OF_DEVICE_NAME, max = MAX_SIZE_OF_DEVICE_NAME)
  private String deviceName;

  @Size(min = MIN_SIZE_OF_DEVICE_NAME, max = MAX_SIZE_OF_DEVICE_NAME)
  private String brandName;
}
