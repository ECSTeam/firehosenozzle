package com.ecsteam.firehose.nozzle.configuration;

import com.ecsteam.firehose.nozzle.FirehoseProperties;
import com.ecsteam.firehose.nozzle.FirehoseReader;
import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;

import lombok.extern.slf4j.Slf4j;

import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.ClientCredentialsGrantTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import java.net.MalformedURLException;
import java.net.URL;

@Configuration
@Slf4j
public class FirehoseNozzleConfiguration {

    private DefaultConnectionContext connectionContext(String apiHost, Boolean skipSslValidation) {
        return DefaultConnectionContext.builder()
                .apiHost(apiHost)
                .skipSslValidation(skipSslValidation)
                .build();
    }

    private TokenProvider tokenProvider(String clientId, String clientSecret) {
        return ClientCredentialsGrantTokenProvider.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    private DopplerClient dopplerClient(FirehoseProperties properties) {
        return ReactorDopplerClient.builder()
                .connectionContext(connectionContext(getApiHost(properties), properties.isSkipSslValidation()))
                .tokenProvider(tokenProvider(properties.getUsername(), properties.getPassword()))
                .build();
    }
    

    private String getApiHost(FirehoseProperties properties) {
        String apiHost = properties.getApiEndpoint();

        // in a tile context, this may get passed as a full URL, but we just need the hostname
        try {
            URL url = new URL(apiHost);
            apiHost = url.getHost();
        } catch (MalformedURLException e) {
            // this will happen if passed directly as "api.{SYSTEM_DOMAIN}"
        } finally {
            return apiHost;
        }
    }
    
    @Bean
    @Autowired
    public FirehoseReader firehoseReader(FirehoseProperties props, ApplicationContext context) {
    	
    	log.info("in the firehose reader bean method");
    	String[] names = context.getBeanNamesForAnnotation(FirehoseNozzle.class);
		if (names.length == 1) {
			return new FirehoseReader(props,context, dopplerClient(props));
		}
		else if (names.length > 1) {
			log.error("****************************************");
			log.error("Cannot instantiate FirehoseReader class as there are multiple beans with the FirehoseNozzle annotation");
			for (String name : names) {
				log.error(name);
			}
			
			return null;
		}
		else {
			log.error("****************************************");
			log.error("Cannot instantiate FirehoseReader class as there are no beans with the FirehoseNozzle annotation");
			
			return null;
		}
    }


}
