package com.ecsteam.firehose.nozzle.annotation.application;

import org.springframework.boot.SpringApplication;

import com.ecsteam.firehose.nozzle.annotation.EnableFirehoseNozzle;

@EnableFirehoseNozzle(apiEndpoint = "${nozzle.api.endpoint}", 
					  username = "${nozzle.username}", 
					  password = "${nozzle.password}", 
					  skipSslValidation = "${nozzle.skipSslValidation}")
public class ApplicationTest4 {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationTest4.class, args);
	}
}
