package com.ecsteam.firehose.nozzle.serviceinfo;

import org.springframework.cloud.service.BaseServiceInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirehoseConnectorServiceInfo extends BaseServiceInfo {
	
	private String serviceType;
	private String subscriptionId;
	private String username;
	private String password;
	private String apiEndpoint;
	private boolean skipSslValidation;

	public FirehoseConnectorServiceInfo(String name, String serviceType, String subscriptionId, String username, String password, String apiEndpoint, boolean skipSslValidation) {
		super(name);
		this.serviceType = serviceType;
		this.subscriptionId = subscriptionId;
		this.username = username;
		this.password = password;
		this.apiEndpoint = apiEndpoint;
		this.skipSslValidation = skipSslValidation;
	}

}