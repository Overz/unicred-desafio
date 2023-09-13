package com.example.associado.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "custom-app-properties")
public class CustomAppProperties implements Serializable {
}
