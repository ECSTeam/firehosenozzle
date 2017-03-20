package com.ecsteam.firehose.nozzle.mock;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.ecsteam.firehose.nozzle.FirehoseProperties;
import com.ecsteam.firehose.nozzle.FirehoseReader;

@Configuration
public class MockDopplerClientProvider {

  @Primary
  @Bean
  public FirehoseReader firehoseReader(FirehoseProperties props, ApplicationContext context) {
      return new FirehoseReader(props,context, new MockDopplerClient());
  }

}
