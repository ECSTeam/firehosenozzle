package com.ecsteam.firehose.nozzle;


import com.ecsteam.firehose.nozzle.annotation.EnableFirehoseNozzle;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;

@ToString
@Getter
@Slf4j
public class FirehoseProperties {

    private String apiEndpoint;
    private String username;
    private String password;
    private boolean skipSslValidation;
    
    private boolean validConfiguration = true;
    
    public static final String NO_ENDPOINT_USED = "http://none";
    public static final String NO_USERNAME_USED = "NO_USERNAME";
    public static final String NO_PASSWORD_USED = "NO_PASSWORD";
    public static final boolean NO_SKIP_SSL_USED = false;

    @Autowired
    public FirehoseProperties(ApplicationContext context) {
    	
    	
        String[] names = context.getBeanNamesForAnnotation(EnableFirehoseNozzle.class);
        if (names.length == 1) {
	        EnableFirehoseNozzle efh = context.findAnnotationOnBean(names[0],EnableFirehoseNozzle.class);
	        this.apiEndpoint = efh.apiEndpoint();
	        this.username = efh.username();
	        this.password = efh.password();
	        this.skipSslValidation = efh.skipSslValidation();
	
	        log.info("************ FirehoseProperties CONSTRUCTED! " + Calendar.getInstance().getTimeInMillis() + " **************");
	        log.info("************ " + this.toString());
        }
        else if (names.length > 1) {
        	log.info("Expecting exactly one bean with EnableFirehoseNozzle annotation, found " + names.length);
        	log.info("Bean names:");
        	for (String name : names) {
        		log.info(name);
        	}
        	setNoValues();
        }
        else {
        	log.info("EnableFirehoseNozzle library on path yet no beans found with EnableFirehoseNozzle annotation");
        	setNoValues();
        }
    }
    
    private void setNoValues () {
    	this.apiEndpoint = NO_ENDPOINT_USED;
        this.username = NO_USERNAME_USED;
        this.password = NO_PASSWORD_USED;
        this.skipSslValidation = NO_SKIP_SSL_USED;
        
        validConfiguration = false;
    }
}
