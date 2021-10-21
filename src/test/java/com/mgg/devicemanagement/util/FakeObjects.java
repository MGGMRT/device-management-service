package com.mgg.devicemanagement.util;

import com.mgg.devicemanagement.dto.request.DevicePatch;
import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.model.Device;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FakeObjects {

  public static Device getDevice() {
    Device device = new Device();
    device.setDeviceKey(UUID.randomUUID().toString());
    device.setBrand("Apple");
    device.setName("Keyboard");
    return device;
  }

  public static Device getDeviceWithBrandAndName(String brand, String name) {
    Device device = new Device();
    device.setDeviceKey(UUID.randomUUID().toString());
    device.setBrand(brand);
    device.setName(name);
    device.setCreatedDate(LocalDateTime.now());
    device.setModifiedDate(LocalDateTime.now());
    return device;
  }

  public static DeviceRequestDto getDeviceRequestDto() {
    return new DeviceRequestDto("Laptop", "Microsoft");
  }

  public static List<Device> getDeviceList() {
    Device firstDevice = getDevice();
    Device secondDevice = new Device();
    secondDevice.setDeviceKey(UUID.randomUUID().toString());
    secondDevice.setBrand("Dell");
    secondDevice.setName("Keyboard");
    secondDevice.setCreatedDate(LocalDateTime.now());
    secondDevice.setModifiedDate(LocalDateTime.now());
    return Arrays.asList(firstDevice, secondDevice);
  }

  public static DevicePatch buildDevicePatch() {
    return DevicePatch.builder()
            .path("brand")
            .value("apple")
            .build();
  }
}
