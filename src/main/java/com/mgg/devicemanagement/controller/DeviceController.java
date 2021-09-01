package com.mgg.devicemanagement.controller;

import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.dto.response.DeviceResponseDto;
import com.mgg.devicemanagement.service.DeviceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping(value = "/devices", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createDevice(@Valid @RequestBody DeviceRequestDto deviceRequestDto) {
        log.info("Creating a device with name={}, brand={}", deviceRequestDto.getDeviceName(),
                deviceRequestDto.getBrandName());
        deviceService.createDevice(deviceRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/devices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResponseDto> getDevice(@PathVariable(name = "id") String deviceKey) {
        log.info("Getting a device with deviceKey={}", deviceKey);
        DeviceResponseDto deviceResponseDto = deviceService.getDevice(deviceKey);
        return ResponseEntity.ok(deviceResponseDto);
    }

    @GetMapping(value = "/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeviceResponseDto>> getAllDevice() {
        log.info("Getting all device");
        List<DeviceResponseDto> deviceResponseDtoList = deviceService.getDeviceAll();
        return ResponseEntity.ok(deviceResponseDtoList);
    }

    @DeleteMapping(value = "/devices/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable(name = "id") String deviceKey) {
        log.info("Deleting a device with deviceKey={}", deviceKey);
        deviceService.deleteDevice(deviceKey);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(value = "/devices/{id}")
    public ResponseEntity<DeviceResponseDto> updateDeviceByDeviceKey(@PathVariable(name = "id") String deviceKey,
           @Valid @RequestBody DeviceRequestDto deviceRequestDto) {
        log.info("Updating a device with deviceKey={}", deviceKey);
        DeviceResponseDto deviceResponseDto = deviceService.updateDevice(deviceKey, deviceRequestDto);
        return ResponseEntity.ok(deviceResponseDto);
    }

    @GetMapping(value = "/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeviceResponseDto>> searchDeviceByBrandName(@RequestParam(name = "brandName",required = true) String brandName) {
        log.info("Searching all device");
        List<DeviceResponseDto> deviceResponseDtoList = deviceService.searchDeviceByBrandName(brandName);
        return ResponseEntity.ok(deviceResponseDtoList);
    }

}
