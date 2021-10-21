package com.mgg.devicemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DeviceManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(DeviceManagementApplication.class, args);
  }
}
