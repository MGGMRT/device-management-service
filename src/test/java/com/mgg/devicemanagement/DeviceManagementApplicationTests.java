package com.mgg.devicemanagement;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class DeviceManagementApplicationTests {

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

  @Test
  void contextLoads() {
    assertTrue(postgresqlContainer.isRunning());
  }
}
