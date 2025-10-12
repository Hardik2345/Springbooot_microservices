package com.ncu.collage.configurationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigurationserverApplication {

	public static void main(String[] args) {
		String mode=System.getenv(name:"CONFIG_MODE")
		SpringApplication application=new SpringApplication(ConfigurationserverApplication.class);
		if("native".equalsIgnoreCase(mode)) {
			System.setAdditionalProfiles("native");
		} else {
			System.setAdditionalProfiles("git");
		}
		application.run(args);
	}

}
