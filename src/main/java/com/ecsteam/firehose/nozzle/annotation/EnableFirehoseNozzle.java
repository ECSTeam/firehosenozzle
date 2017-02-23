package com.ecsteam.firehose.nozzle.annotation;


import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzlePropertiesConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FirehoseNozzlePropertiesConfiguration.class)
@Documented
public @interface EnableFirehoseNozzle {
    String apiEndpoint() default "https://api.bosh-lite.com";
    String username() default "admin";
    String password() default "admin";
    boolean skipSslValidation() default true;
}