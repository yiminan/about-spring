package com.example.aboutspring.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfiguration {

  @ConfigurationProperties(prefix = "example")
  @Bean
  public Properties properties() {
    return new Properties();
  }
}
