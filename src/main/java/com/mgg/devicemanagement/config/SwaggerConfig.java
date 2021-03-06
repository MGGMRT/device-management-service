package com.mgg.devicemanagement.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  OpenAPI simulationApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Device management service")
                .description("Used to manage devices")
                .version("v1.0.0"));
  }
}
