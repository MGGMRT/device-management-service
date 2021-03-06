package com.mgg.devicemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.dto.response.DeviceResponseDto;
import com.mgg.devicemanagement.model.Device;
import com.mgg.devicemanagement.repository.DeviceRepository;
import com.mgg.devicemanagement.util.FakeObjects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;

import static com.mgg.devicemanagement.constants.RestApiUrlConstants.BASE;
import static com.mgg.devicemanagement.constants.RestApiUrlConstants.DEVICES;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
public class DeviceControllerTest extends AbstractIntegrationTest {

  @Autowired private ObjectMapper objectMapper;

  @Autowired private DeviceRepository deviceRepository;

  @Container
  private static PostgreSQLContainer postgresqlContainer =
      new PostgreSQLContainer("postgres:11.1")
          .withDatabaseName("devicemng")
          .withUsername("postgre")
          .withPassword("postgre");

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    registry.add("spring.datasource.username", postgresqlContainer::getUsername);
  }

  @AfterEach
  public void setUp() {
    deviceRepository.deleteAll();
  }

  @Test
  public void createDevice_shouldReturn201CreatedDeviceWhenRequestBodyIsDefineCorrectly()
      throws Exception {
    // Given
    DeviceRequestDto deviceRequestDto = new DeviceRequestDto("Laptop", "Microsoft");

    // When Then
    mockMvc
        .perform(
            post(BASE + DEVICES)
                .content(objectMapper.writeValueAsString(deviceRequestDto))
                .header("Content-Type", "application/json"))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("Laptop")))
        .andExpect(jsonPath("$.brand", is("Microsoft")))
        .andExpect(jsonPath("$.deviceKey", isA(String.class)));
  }

  @Test
  public void createDevice_shouldReturn400CreatedDeviceWhenRequestBodyIsNotDefineCorrectly()
      throws Exception {
    // Given
    DeviceRequestDto deviceRequestDto = new DeviceRequestDto("La", "Mi");

    // When Then
    mockMvc
        .perform(
            post(BASE + DEVICES)
                .content(objectMapper.writeValueAsString(deviceRequestDto))
                .header("Content-Type", "application/json"))
        .andExpect(status().isBadRequest());
  }

  @ParameterizedTest
  @CsvSource({"Laptop, Microsoft", "Mouse, Microsoft", "Mouse, Apple"})
  public void getDevice_shouldReturnOKWhenProperDeviceKeyIsDefined(
      String deviceName, String brandName) throws Exception {
    // Given
    Device deviceWithBrandAndNameOnDb = createDeviceWithBrandAndNameOnDb(brandName, deviceName);
    Device savedDevice = deviceRepository.save(deviceWithBrandAndNameOnDb);
    String deviceKey = savedDevice.getDeviceKey();

    // When Then
    mockMvc
        .perform(get(BASE + "/devices/" + deviceKey).header("Content-Type", "application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(deviceName)))
        .andExpect(jsonPath("$.brand", is(brandName)))
        .andExpect(jsonPath("$.deviceKey", is(deviceKey)));
  }

  @Test
  public void getDevice_shouldReturnNotFoundWhenValidDeviceKeyIsNotDefined() throws Exception {
    // Given
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";

    // When Then
    mockMvc
        .perform(get(BASE + "/devices/" + deviceKey).header("Content-Type", "application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getAllDevice_shouldReturnOKWhenCallAll() throws Exception {
    // Given
    createDevicesOnDb();

    // When Then
    mockMvc
        .perform(get(BASE + "/devices/").header("Content-Type", "application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].brand", is("Apple")))
        .andExpect(jsonPath("$[0].name", is("Keyboard")));
  }

  @Test
  public void getAllDevice_shouldReturnOKWhenSearchParameterIsUsed() throws Exception {
    // Given
    createDevicesOnDb();

    // When Then
    mockMvc
        .perform(
            get(BASE + "/devices/")
                .param("brandName", "Dell")
                .header("Content-Type", "application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].brand", is("Dell")))
        .andExpect(jsonPath("$[0].name", is("Keyboard")));
  }

  @Test
  public void deleteDevice_shouldReturnOKWhenValidDeviceKeyIsDefined() throws Exception {
    // Given
    Device device = createDeviceOnDb();
    String deviceKey = device.getDeviceKey();

    // When Then
    mockMvc
        .perform(delete(BASE + "/devices/" + deviceKey).header("Content-Type", "application/json"))
        .andExpect(status().isOk());
  }

  @Test
  public void deleteDevice_shouldReturnNotFoundWhenInValidDeviceKeyIsDefined() throws Exception {
    // Given
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";

    // When Then
    mockMvc
        .perform(delete(BASE + "/devices/" + deviceKey).header("Content-Type", "application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateDeviceByDeviceKey_shouldReturnOKWhenValidDeviceKeyAndValidRequestObject()
      throws Exception {
    // Given
    Device device = createDeviceOnDb();
    String deviceKey = device.getDeviceKey();
    DeviceRequestDto deviceRequestDto = FakeObjects.getDeviceRequestDto();

    // When Then
    mockMvc
        .perform(
            put(BASE + "/devices/" + deviceKey)
                .content(objectMapper.writeValueAsString(deviceRequestDto))
                .header("Content-Type", "application/json"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("Laptop")))
        .andExpect(jsonPath("$.brand", is("Microsoft")))
        .andExpect(jsonPath("$.deviceKey", is(deviceKey)));
  }

  @Test
  public void updateDeviceByDeviceKey_shouldReturn404NotFoundWhenInValidDeviceKeyIsDefined()
      throws Exception {
    // Given
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";
    DeviceRequestDto deviceRequestDto = FakeObjects.getDeviceRequestDto();

    // When Then
    mockMvc
        .perform(
            put(BASE + "/devices/" + deviceKey)
                .content(objectMapper.writeValueAsString(deviceRequestDto))
                .header("Content-Type", "application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updatePartlyDeviceByDeviceKey_shouldReturnOkWhenValidDeviceKeyAndPatchRequest()
      throws Exception {
    // Given
    Device device = createDeviceOnDb();
    String deviceKey = device.getDeviceKey();
    String patchRequest = "{ \"path\": \"brand\", \"value\": \"apple\" }";

    // When
    ResultActions resultActions =
        mockMvc
            .perform(
                patch(BASE + "/devices/" + deviceKey)
                    .content(patchRequest)
                    .header("Content-Type", "application/json-patch+json"))
            .andExpect(status().isOk());
    // Then
    String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
    DeviceResponseDto deviceResponseDto =
        objectMapper.readValue(contentAsString, DeviceResponseDto.class);
    assertEquals("apple", deviceResponseDto.getBrand());
    assertNotEquals(device.getBrand(), deviceResponseDto.getBrand());
    assertEquals(device.getName(), deviceResponseDto.getName());
    assertEquals(device.getDeviceKey(), deviceResponseDto.getDeviceKey());
  }

  @Test
  public void updatePartlyDeviceByDeviceKey_shouldReturn404NotFoundWhenInValidDeviceKeyIsDefined()
      throws Exception {
    // Given
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";
    String pathRequest = "{ \"path\": \"brand\", \"value\": \"apple\" }";

    // When Then
    mockMvc
        .perform(
            patch(BASE + "/devices/" + deviceKey)
                .content(pathRequest)
                .header("Content-Type", "application/json-patch+json"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void
      updatePartlyDeviceByDeviceKey_shouldReturn400BadRequestWhenInValidRequestJsonObjectPathValue()
          throws Exception {
    // Given
    Device device = createDeviceOnDb();
    String deviceKey = device.getDeviceKey();
    String pathRequest = "{ \"path\": \"deviceKey\", \"value\": \"apple\" }";

    // When Then
    mockMvc
        .perform(
            patch(BASE + "/devices/" + deviceKey)
                .content(pathRequest)
                .header("Content-Type", "application/json-patch+json"))
        .andExpect(status().isBadRequest());
  }

  private Device createDeviceWithBrandAndNameOnDb(String brand, String name) {
    Device device = FakeObjects.getDeviceWithBrandAndName(brand, name);
    deviceRepository.save(device);
    return device;
  }

  private Device createDeviceOnDb() {
    Device device = FakeObjects.getDevice();
    deviceRepository.save(device);
    return device;
  }

  private void createDevicesOnDb() {
    List<Device> deviceList = FakeObjects.getDeviceList();
    deviceList.forEach(item -> deviceRepository.save(item));
  }
}
