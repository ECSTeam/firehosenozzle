package com.ecsteam.firehose.nozzle.annotation;


import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzlePropertiesConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FirehoseNozzlePropertiesConfiguration.class)
@Documented
public @interface EnableFirehoseNozzle {
	
	public static final String DEFAULT_API_ENDPOINT = "https://api.bosh-lite.com";
	public static final String DEFAULT_USERNAME = "admin";
	public static final String DEFAULT_PASSWORD = "admin";
	public static final boolean DEFAULT_SKIP_SSL_VALIDATION = true;
	
    String apiEndpoint() default DEFAULT_API_ENDPOINT;
    String username() default DEFAULT_USERNAME;
    String password() default DEFAULT_PASSWORD;
    boolean skipSslValidation() default DEFAULT_SKIP_SSL_VALIDATION;
}