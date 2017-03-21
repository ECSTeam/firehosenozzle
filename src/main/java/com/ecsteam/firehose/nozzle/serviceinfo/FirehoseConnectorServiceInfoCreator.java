package com.ecsteam.firehose.nozzle.serviceinfo;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FirehoseConnectorServiceInfoCreator extends CloudFoundryServiceInfoCreator<FirehoseConnectorServiceInfo> {

	public FirehoseConnectorServiceInfoCreator() {
		super(new Tags(), new String[1]);
	}
	
	public FirehoseConnectorServiceInfoCreator(Tags tags, String[] uriSchemes) {
		super(tags, uriSchemes);
	}

	@Override
	public FirehoseConnectorServiceInfo createServiceInfo(Map<String, Object> config) {

		Map<String, Object> configParams = getCredentials(config);

		String name = (String) configParams.get("name");
		String serviceType = (String) configParams.get("serviceType");
		String subscriptionId = (String) configParams.get("subscriptionId");
		String username = (String) configParams.get("username");
		String password = (String) configParams.get("password");
		String apiEndpoint = (String) configParams.get("apiEndpoint");
		Boolean skipSslValidation = (Boolean) configParams.get("skipSslValidation");

		FirehoseConnectorServiceInfo serviceInfo = new FirehoseConnectorServiceInfo(name, serviceType, subscriptionId,
				username, password, apiEndpoint, skipSslValidation);

		log.debug("**************************");
		log.debug("in the create method of FirehoseConnectorServiceInfoCreator");
		log.debug("returning a serviceInfo that looks like:" + serviceInfo.toString());
		
		return serviceInfo;
	}

	@Override
	public boolean accept(Map<String, Object> config) {

		Map<String, Object> configParams = getCredentials(config);

		boolean retVal = false;

		log.debug("**************************");
		log.debug("in the accept method of FirehoseConnectorServiceInfoCreator");

		if (configParams.containsKey("serviceType")) {
			String serviceType = (String) configParams.get("serviceType");
			retVal = serviceType.contains("firehose");
		}

		log.debug("retVal = " + retVal);

		return retVal;

	}

}
