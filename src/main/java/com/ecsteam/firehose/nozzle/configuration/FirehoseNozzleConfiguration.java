package com.ecsteam.firehose.nozzle.configuration;

import com.ecsteam.firehose.nozzle.FirehoseProperties;
import com.ecsteam.firehose.nozzle.FirehoseReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

//@Configuration
public class FirehoseNozzleConfiguration {

//    @Bean
//    @Autowired
    public FirehoseReader firehoseReader(FirehoseProperties props, ApplicationContext context) {
        return new FirehoseReader(props);
    }

}
