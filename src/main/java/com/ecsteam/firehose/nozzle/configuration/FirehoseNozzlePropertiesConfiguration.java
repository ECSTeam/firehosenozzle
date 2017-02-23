package com.ecsteam.firehose.nozzle.configuration;

import com.ecsteam.firehose.nozzle.FirehoseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirehoseNozzlePropertiesConfiguration {

    @Bean
    @Autowired
    public FirehoseProperties firehoseProperties(ApplicationContext context) {
        return new FirehoseProperties(context);
    }

}
