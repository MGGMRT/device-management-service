package com.mgg.devicemanagement.service;

import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.dto.response.DeviceResponseDto;
import com.mgg.devicemanagement.exception.NotFoundDeviceException;
import com.mgg.devicemanagement.mapper.DeviceMapper;
import com.mgg.devicemanagement.model.Device;
import com.mgg.devicemanagement.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public void createDevice(DeviceRequestDto deviceRequestDto) {
        log.info("creating device");
        Device device = getDevice(deviceRequestDto);
        deviceRepository.save(device);
    }

    private Device getDevice(DeviceRequestDto deviceRequestDto) {
        Device device = new Device();
        device.setName(deviceRequestDto.getBrandName());
        device.setBrand(deviceRequestDto.getDeviceName());
        device.setDeviceKey(UUID.randomUUID().toString());
        return device;
    }

    public DeviceResponseDto getDevice(String deviceKey) {
        Device device = getDeviceByDeviceKey(deviceKey);
        return DeviceResponseDto.from(device.getName(),device.getBrand());
    }

    private Device getDeviceByDeviceKey(String deviceKey) {
        return deviceRepository.findByDeviceKey(deviceKey)
                .orElseThrow(() -> new NotFoundDeviceException("The device is not found by id {{deviceUuid}}"));
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
        return DeviceResponseDto.from(device.getName(),device.getBrand());
    }

    public DeviceResponseDto updatePartlyDevice(String deviceKey, DeviceRequestDto deviceRequestDto) {
        Device device = getDeviceByDeviceKey(deviceKey);
        device.setBrand(deviceRequestDto.getBrandName());
        deviceRepository.save(device);
        return DeviceResponseDto.from(device.getName(),device.getBrand());
    }

    public List<DeviceResponseDto> searchDeviceByBrandName(String brandName) {
        List<Device> deviceList = deviceRepository.findByBrand(brandName);
        return transformToDeviceResponseDtoList(deviceList);
    }
}
