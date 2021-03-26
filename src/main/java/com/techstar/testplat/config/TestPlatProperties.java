package com.techstar.testplat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "testplat")
public class TestPlatProperties {
	private String loginHost;
//	private String swaggerUrl;
	private String jmeterAgentUrl;
}
