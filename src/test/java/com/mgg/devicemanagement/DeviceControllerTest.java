package com.mgg.devicemanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgg.devicemanagement.dto.request.DeviceRequestDto;
import com.mgg.devicemanagement.model.Device;
import com.mgg.devicemanagement.repository.DeviceRepository;
import com.mgg.devicemanagement.util.FakeObjects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import java.util.Map;
import static com.mgg.devicemanagement.constants.RestApiUrlConstants.BASE;
import static com.mgg.devicemanagement.constants.RestApiUrlConstants.DEVICES;
import static org.hamcrest.Matchers.*;
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
    DeviceRequestDto deviceRequestDto = new DeviceRequestDto("Laptop", "Microsoft");

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
    DeviceRequestDto deviceRequestDto = new DeviceRequestDto("La", "Mi");

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

    Device deviceWithBrandAndNameOnDb = createDeviceWithBrandAndNameOnDb(brandName, deviceName);
    Device savedDevice = deviceRepository.save(deviceWithBrandAndNameOnDb);

    String deviceKey = savedDevice.getDeviceKey();
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
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";
    mockMvc
        .perform(get(BASE + "/devices/" + deviceKey).header("Content-Type", "application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void getAllDevice_shouldReturnOKWhenCallAll() throws Exception {
    createDevicesOnDb();
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
    createDevicesOnDb();
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
    Device device = createDeviceOnDb();
    String deviceKey = device.getDeviceKey();
    mockMvc
        .perform(delete(BASE + "/devices/" + deviceKey).header("Content-Type", "application/json"))
        .andExpect(status().isOk());
  }

  @Test
  public void deleteDevice_shouldReturnNotFoundWhenInValidDeviceKeyIsDefined() throws Exception {
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";
    mockMvc
        .perform(delete(BASE + "/devices/" + deviceKey).header("Content-Type", "application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateDeviceByDeviceKey_shouldReturnOKWhenValidDeviceKeyAndValidRequestObject()
      throws Exception {
    Device device = createDeviceOnDb();
    String deviceKey = device.getDeviceKey();
    DeviceRequestDto deviceRequestDto = FakeObjects.getDeviceRequestDto();
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
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";
    DeviceRequestDto deviceRequestDto = FakeObjects.getDeviceRequestDto();

    mockMvc
        .perform(
            put(BASE + "/devices/" + deviceKey)
                .content(objectMapper.writeValueAsString(deviceRequestDto))
                .header("Content-Type", "application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updatePartlyDeviceByDeviceKey_shouldReturn404NotFoundWhenInValidDeviceKeyIsDefined()
      throws Exception {
    String deviceKey = "7937cd0d-92d0-45b9-b7df-75986a9f1a1f";
    String pathRequest = "[ { \"op\": \"replace\", \"path\": \"/brand\", \"value\": \"apple\" } ]";
    mockMvc
        .perform(
            patch(BASE + "/devices/" + deviceKey)
                .content(pathRequest)
                .header("Content-Type", "application/json-patch+json"))
        .andExpect(status().isNotFound());
  }

  @Disabled
  @Test
  public void updatePartlyDeviceByDeviceKey_shouldReturnOkWhenValidDeviceKeyAndPatchRequest()
      throws Exception {
    Device device = createDeviceOnDb();
    String deviceKey = device.getDeviceKey();
    String patchRequest = "[ { \"op\": \"replace\", \"path\": \"/brand\", \"value\": \"apple\" } ]";
    mockMvc
        .perform(
            patch(BASE + "/devices/" + deviceKey)
                .content(patchRequest)
                .header("Content-Type", "application/json-patch+json"))
        .andExpect(status().isNotFound());
  }

  private Device createDeviceWithBrandAndNameOnDb(String brand, String name) {
    Device device = FakeObjects.getDeviceWithBrandAndName(brand,name);
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
