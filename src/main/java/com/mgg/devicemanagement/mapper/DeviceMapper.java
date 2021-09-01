package com.mgg.devicemanagement.mapper;

import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.model.Device;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface DeviceMapper {

    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    Device deviceFromDeviceRequestDto(DeviceRequestDto deviceRequestDto);
}
