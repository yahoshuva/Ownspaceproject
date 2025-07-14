package com.fwitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.fwitter.config.RsaKeyProperties;



@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication(scanBasePackages = "com.fwitter")
public class VersionApplication {

	public static void main(String[] args) {
		SpringApplication.run(VersionApplication.class, args);
	}

}
