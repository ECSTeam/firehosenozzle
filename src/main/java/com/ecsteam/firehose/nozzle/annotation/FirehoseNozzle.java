package com.ecsteam.firehose.nozzle.annotation;


import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzleConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FirehoseNozzleConfiguration.class)
@Documented
public @interface FirehoseNozzle {
    String subscriptionId();
}
