package com.mgg.devicemanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.dto.response.DeviceResponseDto;
import com.mgg.devicemanagement.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.mgg.devicemanagement.constants.RestApiUrlConstants.*;
import static java.util.Objects.isNull;

@CrossOrigin
@Slf4j
@Validated
@AllArgsConstructor
@RestController
@RequestMapping(BASE)
@Tag(name = "Device Management Service")
public class DeviceController {

  private final DeviceService deviceService;

  @Operation(summary = "Create a device based on the request object", method = "POST")
  @ApiResponse(responseCode = "201", description = " Returns http status code ")
  @ApiResponse(
      responseCode = "400",
      description = "Returned when input is not within the allowed range.")
  @PostMapping(
      value = DEVICES,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DeviceResponseDto> createDevice(
      @Valid @RequestBody DeviceRequestDto deviceRequestDto) {
    log.info(
        "Creating a device with name={}, brand={}",
        deviceRequestDto.getDeviceName(),
        deviceRequestDto.getBrandName());
    DeviceResponseDto deviceResponseDto = deviceService.createDevice(deviceRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(deviceResponseDto);
  }

  @Operation(
      summary = "Returned the device when device is found for given device key.",
      method = "GET")
  @ApiResponse(
      responseCode = "200",
      description = "The Device object returned.",
      content = @Content(schema = @Schema(implementation = DeviceResponseDto.class)))
  @ApiResponse(
      responseCode = "404",
      description = "Returned when no device is found for given deviceKey.")
  @GetMapping(value = DEVICES_PATH_VARIABLE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DeviceResponseDto> getDevice(
      @PathVariable(name = "deviceKey") String deviceKey) {
    log.info("Getting a device with deviceKey={}", deviceKey);
    DeviceResponseDto deviceResponseDto = deviceService.getDevice(deviceKey);
    return ResponseEntity.ok(deviceResponseDto);
  }

  @Operation(
      summary = "Return all devices or all devices searched for given device name.",
      method = "GET")
  @ApiResponse(
      responseCode = "200",
      description =
          "Devices list object return based on search request parameter if it was passed, or returned all devices.",
      content = @Content(schema = @Schema(implementation = DeviceResponseDto.class)))
  @GetMapping(value = DEVICES, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DeviceResponseDto>> getAllDevice(
      @RequestParam(name = "brandName", required = false) String brandName) {
    List<DeviceResponseDto> deviceResponseDtoList;
    if (isNull(brandName)) {
      log.info("Getting all device");
      deviceResponseDtoList = deviceService.getDeviceAll();
    } else {
      log.info("Searching all device with brand={}", brandName);
      deviceResponseDtoList = deviceService.searchDeviceByBrandName(brandName);
    }
    return ResponseEntity.ok(deviceResponseDtoList);
  }

  @Operation(
      summary = "Delete the device when device is found for given device key.",
      method = "DELETE")
  @ApiResponse(responseCode = "200", description = "The Device was deleted.")
  @ApiResponse(
      responseCode = "404",
      description = "Returned when no device is found for given device key.")
  @DeleteMapping(value = DEVICES_PATH_VARIABLE)
  public ResponseEntity<Void> deleteDevice(@PathVariable(name = "deviceKey") String deviceKey) {
    log.info("Deleting a device with deviceKey={}", deviceKey);
    deviceService.deleteDevice(deviceKey);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(
      summary = "Updated the device when device is found for given device key.",
      method = "PUT")
  @ApiResponse(responseCode = "200", description = "The Device object was updated.")
  @ApiResponse(
      responseCode = "404",
      description = "Returned when no device is found for given device key.")
  @PutMapping(value = DEVICES_PATH_VARIABLE)
  public ResponseEntity<DeviceResponseDto> updateDeviceByDeviceKey(
      @PathVariable(name = "deviceKey") String deviceKey,
      @Valid @RequestBody DeviceRequestDto deviceRequestDto) {
    log.info("Updating a device with deviceKey={}", deviceKey);
    DeviceResponseDto deviceResponseDto = deviceService.updateDevice(deviceKey, deviceRequestDto);
    return ResponseEntity.ok(deviceResponseDto);
  }

  @Operation(
      summary = "Partly update the device when device is found for given device key.",
      method = "PATCH")
  @ApiResponse(responseCode = "200", description = "The Device object was partly updated.")
  @ApiResponse(
      responseCode = "404",
      description = "Returned when no device is found for given device key.")
  @PatchMapping(value = DEVICES_PATH_VARIABLE, consumes = "application/json-patch+json")
  public ResponseEntity<DeviceResponseDto> updatePartlyDeviceByDeviceKey(
      @PathVariable(name = "deviceKey") String deviceKey, @RequestBody String patch)
      throws JsonProcessingException {
    log.info("Partly updating a device with deviceKey={}", deviceKey);
    DeviceResponseDto deviceResponseDto = deviceService.partlyUpdateDevice(deviceKey, patch);
    return ResponseEntity.ok(deviceResponseDto);
  }
}
