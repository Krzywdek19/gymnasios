package com.krzywdek19.api_gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.dev-auth")
public class DevAuthProperties {
    private boolean enabled;
    private String email;
}
