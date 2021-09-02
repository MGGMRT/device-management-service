package com.mgg.devicemanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.dto.response.DeviceResponseDto;
import com.mgg.devicemanagement.exception.NotFoundDeviceException;
import com.mgg.devicemanagement.model.Device;
import com.mgg.devicemanagement.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.json.JsonPatch;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceService {

  private final DeviceRepository deviceRepository;
  private final ObjectMapper objectMapper;

  public void createDevice(DeviceRequestDto deviceRequestDto) {
    log.info("creating device");
    Device device = getDevice(deviceRequestDto);
    deviceRepository.save(device);
  }

  private Device getDevice(DeviceRequestDto deviceRequestDto) {
    Device device = new Device();
    device.setName(deviceRequestDto.getDeviceName());
    device.setBrand(deviceRequestDto.getBrandName());
    device.setDeviceKey(UUID.randomUUID().toString());
    return device;
  }

  public DeviceResponseDto getDevice(String deviceKey) {
    Device device = getDeviceByDeviceKey(deviceKey);
    return DeviceResponseDto.from(device.getName(), device.getBrand());
  }

  private Device getDeviceByDeviceKey(String deviceKey) {
    return deviceRepository
        .findByDeviceKey(deviceKey)
        .orElseThrow(
            () -> new NotFoundDeviceException("The device is not found by deviceKey=" + deviceKey));
  }

  public List<DeviceResponseDto> getDeviceAll() {
    List<Device> deviceList = deviceRepository.findAll();
    return transformToDeviceResponseDtoList(deviceList);
  }

  private List<DeviceResponseDto> transformToDeviceResponseDtoList(List<Device> deviceList) {
    return deviceList.stream()
        .map(item -> DeviceResponseDto.from(item.getName(), item.getBrand()))
        .collect(Collectors.toList());
  }

  public void deleteDevice(String deviceKey) {
    Device device = getDeviceByDeviceKey(deviceKey);
    deviceRepository.delete(device);
  }

  public DeviceResponseDto updateDevice(String deviceKey, DeviceRequestDto deviceRequestDto) {
    Device device = getDeviceByDeviceKey(deviceKey);
    device.setName(deviceRequestDto.getDeviceName());
    device.setBrand(deviceRequestDto.getBrandName());
    deviceRepository.save(device);
    return DeviceResponseDto.from(device.getName(), device.getBrand());
  }

  public List<DeviceResponseDto> searchDeviceByBrandName(String brandName) {
    List<Device> deviceList = deviceRepository.findByBrand(brandName);
    return transformToDeviceResponseDtoList(deviceList);
  }

  public DeviceResponseDto partlyUpdateDevice(String deviceKey, JsonPatch patch) {
    Device device = getDeviceByDeviceKey(deviceKey);
    Device modifiedDevice = getModifiedDevice(patch, device);
    Device updatedDevice = deviceRepository.save(modifiedDevice);
    return DeviceResponseDto.from(updatedDevice.getName(), updatedDevice.getBrand());
  }

  private Device getModifiedDevice(JsonPatch patch, Device device) {
    JsonStructure target = objectMapper.convertValue(device, JsonStructure.class);
    JsonValue devicePatched = patch.apply(target);
    Device modifiedDevice = objectMapper.convertValue(devicePatched, Device.class);
    log.debug("modified device {}", modifiedDevice);
    return modifiedDevice;
  }
}
