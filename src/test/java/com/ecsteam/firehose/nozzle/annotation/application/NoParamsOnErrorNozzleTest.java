package com.ecsteam.firehose.nozzle.annotation.application;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEventError;

@FirehoseNozzle(subscriptionId = "noparamsonerror")
public class NoParamsOnErrorNozzleTest {

	@OnFirehoseEventError
	public void onFirehoseEventError() {
		
	}
	
	
}
