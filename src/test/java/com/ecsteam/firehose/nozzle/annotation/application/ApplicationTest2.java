package com.ecsteam.firehose.nozzle.annotation.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ecsteam.firehose.nozzle.annotation.EnableFirehoseNozzle;

@SpringBootApplication
@EnableFirehoseNozzle(apiEndpoint = "https://home.api", username = "scott", password = "tiger", skipSslValidation = false)
public class ApplicationTest2 {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationTest2.class, args);
	}

}
