package com.mgg.devicemanagement.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgg.devicemanagement.dto.request.DevicePatch;
import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.dto.response.DeviceResponseDto;
import com.mgg.devicemanagement.exception.NotFoundDeviceException;
import com.mgg.devicemanagement.exception.UnRecognizedDevicePatchException;
import com.mgg.devicemanagement.mapper.DeviceMapperImpl;
import com.mgg.devicemanagement.model.Device;
import com.mgg.devicemanagement.repository.DeviceRepository;
import com.mgg.devicemanagement.util.FakeObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

  @InjectMocks private DeviceService deviceService;
  @Mock private DeviceRepository deviceRepository;

  @Mock() private ObjectMapper objectMapper;

  @Mock private DeviceMapperImpl deviceMapper;

  @Test
  public void createDevice_shouldReturnDeviceResponseDtoWhenValidDeviceRequestDto() {
    // Given
    DeviceRequestDto deviceRequestDto = FakeObjects.getDeviceRequestDto();
    Device device = FakeObjects.getDevice();
    when(deviceRepository.save(any(Device.class))).thenReturn(device);
    when(deviceMapper.deviceFromDeviceRequestDto(any(DeviceRequestDto.class))).thenReturn(device);

    // When
    DeviceResponseDto deviceResponseDto = deviceService.createDevice(deviceRequestDto);

    // Then
    verify(deviceRepository, times(1)).save(any());
    assertTrue(nonNull(deviceResponseDto));
    assertEquals(device.getDeviceKey(), deviceResponseDto.getDeviceKey());
    assertEquals(device.getBrand(), deviceResponseDto.getBrand());
    assertEquals(device.getName(), deviceResponseDto.getName());
  }

  @Test
  public void getDevice_shouldReturnDeviceResponseDtoWhenValidDeviceKey() {
    // Given
    Device device = FakeObjects.getDevice();
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.of(device));

    // When
    DeviceResponseDto deviceResponseDto = deviceService.getDevice(anyString());

    // Then
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
    assertTrue(nonNull(deviceResponseDto));
    assertEquals(device.getDeviceKey(), deviceResponseDto.getDeviceKey());
    assertEquals(device.getBrand(), deviceResponseDto.getBrand());
    assertEquals(device.getName(), deviceResponseDto.getName());
  }

  @Test
  public void getDevice_shouldThrowExceptionWhenInValidDeviceKey() {
    // Given
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.empty());

    // When
    assertThrows(NotFoundDeviceException.class, () -> deviceService.getDevice(anyString()));

    // Then
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
  }

  @Test
  public void getDeviceAll_shouldReturnDeviceResponseDtoListWhenIsCall() {
    // Given
    List<Device> deviceList = FakeObjects.getDeviceList();
    when(deviceRepository.findAll()).thenReturn(deviceList);

    // When
    List<DeviceResponseDto> deviceResponseDtoList = deviceService.getDeviceAll();

    // Then
    verify(deviceRepository, times(1)).findAll();
    assertTrue(nonNull(deviceResponseDtoList));
    assertEquals(deviceList.size(), deviceResponseDtoList.size());
    assertEquals(deviceList.get(0).getDeviceKey(), deviceResponseDtoList.get(0).getDeviceKey());
    assertEquals(deviceList.get(0).getBrand(), deviceResponseDtoList.get(0).getBrand());
    assertEquals(deviceList.get(0).getName(), deviceResponseDtoList.get(0).getName());
  }

  @Test
  public void deleteDevice_shouldReturnVoidWhenDeleteDeviceByValidDeviceKey() {
    // Given
    Device device = FakeObjects.getDevice();
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.of(device));

    // When
    deviceService.deleteDevice(anyString());

    // Then
    verify(deviceRepository, times(1)).delete(device);
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
  }

  @Test
  public void deleteDevice_shouldReturnNotFoundExceptionWhenInValidKey() {
    // Given
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.empty());

    // When
    assertThrows(NotFoundDeviceException.class, () -> deviceService.deleteDevice(anyString()));

    // Then
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
  }

  @Test
  public void updateDevice_shouldReturnDeviceResposeDtoWhenValidDeviceKeyAndDeviceRequestDto() {

    // Given
    Device device = FakeObjects.getDevice();
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.of(device));
    when(deviceRepository.save(any(Device.class))).thenReturn(device);
    DeviceRequestDto deviceRequestDto = FakeObjects.getDeviceRequestDto();

    // When
    DeviceResponseDto deviceResponseDto = deviceService.updateDevice(anyString(), deviceRequestDto);

    // Then
    assertTrue(nonNull(deviceResponseDto));
    assertEquals(device.getDeviceKey(), deviceResponseDto.getDeviceKey());
    assertEquals(device.getBrand(), deviceResponseDto.getBrand());
    assertEquals(device.getName(), deviceResponseDto.getName());
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
    verify(deviceRepository, times(1)).save(any(Device.class));
  }

  @Test
  public void updateDevice_shouldReturnNotFoundExceptionWhenInValidDeviceKeyAndDeviceRequestDto() {
    // Given
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.empty());
    DeviceRequestDto deviceRequestDto = FakeObjects.getDeviceRequestDto();

    // When
    assertThrows(
        NotFoundDeviceException.class,
        () -> deviceService.updateDevice(anyString(), deviceRequestDto));

    // Then
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
  }

  @Test
  public void searchDeviceByBrandName_returnDeviceResponseDtoListWhenEnterBrandName() {
    // Given
    List<Device> deviceList = FakeObjects.getDeviceList();
    when(deviceRepository.findByBrandLikeIgnoreCase(anyString())).thenReturn(deviceList);

    // When
    List<DeviceResponseDto> deviceResponseDtoList =
        deviceService.searchDeviceByBrandName(anyString());

    // Then
    verify(deviceRepository, times(1)).findByBrandLikeIgnoreCase(anyString());
    assertTrue(nonNull(deviceResponseDtoList));
    assertEquals(deviceList.size(), deviceResponseDtoList.size());
    assertEquals(deviceList.get(0).getDeviceKey(), deviceResponseDtoList.get(0).getDeviceKey());
    assertEquals(deviceList.get(0).getBrand(), deviceResponseDtoList.get(0).getBrand());
    assertEquals(deviceList.get(0).getName(), deviceResponseDtoList.get(0).getName());
  }

  @Test
  public void partlyUpdateDevice_shouldReturnDeviceResponseDtoWhenValidKeyAndValidJsonPatch()
      throws JsonProcessingException {
    // Given
    Device device = FakeObjects.getDevice();
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.of(device));
    when(deviceRepository.save(any(Device.class))).thenReturn(device);
    DevicePatch devicePatch = FakeObjects.buildDevicePatch();
    String pathRequest = "{ \"path\": \"brand\", \"value\": \"apple\" }";
    when(objectMapper.readValue(anyString(), eq(DevicePatch.class))).thenReturn(devicePatch);

    // When
    DeviceResponseDto deviceResponseDto =
        deviceService.partlyUpdateDevice(device.getDeviceKey(), pathRequest);

    // Then
    assertTrue(nonNull(deviceResponseDto));
    assertEquals(device.getDeviceKey(), deviceResponseDto.getDeviceKey());
    assertEquals(device.getBrand(), deviceResponseDto.getBrand());
    assertEquals(device.getName(), deviceResponseDto.getName());
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
    verify(deviceRepository, times(1)).save(any(Device.class));
  }

  @Test
  public void partlyUpdateDevice_shouldReturnNotFoundExceptionWhenInValidKeyAndJsonPatch() {
    // Given
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.empty());

    // When
    assertThrows(
        NotFoundDeviceException.class,
        () -> deviceService.partlyUpdateDevice(deviceKey, any(String.class)));

    // Then
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
  }

  @Test
  public void partlyUpdateDevice_shouldReturnBadRequestWhenValidKeyAndInValidJsonPatch()
      throws JsonProcessingException {
    // Given
    Device device = FakeObjects.getDevice();
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.of(device));
    DevicePatch devicePatch = FakeObjects.buildDevicePatch();
    devicePatch.setPath("deviceKey");
    String pathRequest = "{ \"path\": \"deviceKey\", \"value\": \"apple\" }";
    when(objectMapper.readValue(anyString(), eq(DevicePatch.class))).thenReturn(devicePatch);

    // When
    assertThrows(
        UnRecognizedDevicePatchException.class,
        () -> deviceService.partlyUpdateDevice(device.getDeviceKey(), pathRequest));

    // Then
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
  }

  @Test
  public void partlyUpdateDevice_shouldReturnBadRequestWhenValidKeyAndInValidJsonPatchField()
      throws JsonProcessingException {
    // Given
    Device device = FakeObjects.getDevice();
    when(deviceRepository.findByDeviceKey(anyString())).thenReturn(Optional.of(device));
    DevicePatch devicePatch = FakeObjects.buildDevicePatch();
    devicePatch.setPath("deviceKey");
    String pathRequest = "{ \"pathx\": \"deviceKey\", \"value\": \"apple\" }";
    when(objectMapper.readValue(anyString(), eq(DevicePatch.class))).thenReturn(devicePatch);

    // When
    assertThrows(
        UnRecognizedDevicePatchException.class,
        () -> deviceService.partlyUpdateDevice(device.getDeviceKey(), pathRequest));

    // Then
    verify(deviceRepository, times(1)).findByDeviceKey(anyString());
  }
}
