package com.ecsteam.firehose.nozzle.annotation.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ecsteam.firehose.nozzle.annotation.EnableFirehoseNozzle;

@EnableFirehoseNozzle
public class ApplicationTest1 {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationTest1.class, args);
	}
	
}
