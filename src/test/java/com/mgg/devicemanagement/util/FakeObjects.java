package com.mgg.devicemanagement.util;

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
    device.setCreatedTime(LocalDateTime.now());
    device.setModifiedTime(LocalDateTime.now());
    return device;
  }

  public static Device getDeviceWithBrandAndName(String brand, String name) {
    Device device = new Device();
    device.setDeviceKey(UUID.randomUUID().toString());
    device.setBrand(brand);
    device.setName(name);
    device.setCreatedTime(LocalDateTime.now());
    device.setModifiedTime(LocalDateTime.now());
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
    secondDevice.setCreatedTime(LocalDateTime.now());
    secondDevice.setModifiedTime(LocalDateTime.now());
    return Arrays.asList(firstDevice, secondDevice);
  }
}
