package com.mgg.devicemanagement.mapper;

import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeviceMapper {

  Device deviceFromDeviceRequestDto(DeviceRequestDto deviceRequestDto);
}
