package com.ecsteam.firehose.nozzle.annotation.application;

import org.cloudfoundry.doppler.Envelope;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEvent;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEventError;

@FirehoseNozzle(subscriptionId = "MyDriver")
public class NozzleTest1 {
	
	@OnFirehoseEvent
	public void onEvent(Envelope e) {
		
	}
	
	@OnFirehoseEventError
	public void onEventError(Throwable t) {
		
	}

}
