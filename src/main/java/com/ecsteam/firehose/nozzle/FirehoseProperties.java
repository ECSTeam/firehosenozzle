package com.ecsteam.firehose.nozzle;


import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

import com.ecsteam.firehose.nozzle.annotation.EnableFirehoseNozzle;
import com.ecsteam.firehose.nozzle.serviceinfo.FirehoseConnectorServiceInfo;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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
    	
    	log.debug("************** In the Firehose Properties Constructor **************");
    	
        String[] names = context.getBeanNamesForAnnotation(EnableFirehoseNozzle.class);
        if (names.length == 1) {
        	FirehoseConnectorServiceInfo serviceInfo = getFirehoseConnectorServiceInfo();
        	if (serviceInfo == null) {
        		log.info("***********************************************************");
        		log.info("Constructing FirehoseProperties using annotation attributes or defaults");
		        EnableFirehoseNozzle efh = context.findAnnotationOnBean(names[0],EnableFirehoseNozzle.class);
		        this.apiEndpoint = efh.apiEndpoint();
		        this.username = efh.username();
		        this.password = efh.password();
		        this.skipSslValidation = efh.skipSslValidation();
        	}
        	else {
        		log.info("***********************************************************");
        		log.info("Constructing FirehoseProperties using bound service credentials");
        		this.apiEndpoint = serviceInfo.getApiEndpoint();
        		this.username = serviceInfo.getUsername();
        		this.password = serviceInfo.getPassword();
        		this.skipSslValidation = serviceInfo.isSkipSslValidation();
        	}
	
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
    
    private Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            return cloudFactory.getCloud();
        } catch (org.springframework.cloud.CloudException ce) {
            return null;
        }
    }
    
    private FirehoseConnectorServiceInfo getFirehoseConnectorServiceInfo() {

    	
    	Cloud cloud = getCloud();
    	if (cloud != null) {
    		List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
    		for (ServiceInfo serviceInfo : serviceInfos) {
    			if (serviceInfo instanceof FirehoseConnectorServiceInfo) {
    				return (FirehoseConnectorServiceInfo)serviceInfo;
    			}
    		}
    	}
    	return null;
    }
    
    private void setNoValues () {
    	this.apiEndpoint = NO_ENDPOINT_USED;
        this.username = NO_USERNAME_USED;
        this.password = NO_PASSWORD_USED;
        this.skipSslValidation = NO_SKIP_SSL_USED;
        
        validConfiguration = false;
    }
}
