package com.mgg.devicemanagement.mapper;

import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface DeviceMapper {

  @Mapping(source="deviceName",target = "name")
  @Mapping(source="brandName",target = "brand")
  Device deviceFromDeviceRequestDto(DeviceRequestDto deviceRequestDto);
}
