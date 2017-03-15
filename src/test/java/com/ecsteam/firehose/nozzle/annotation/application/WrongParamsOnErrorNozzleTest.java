package com.ecsteam.firehose.nozzle.annotation.application;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEventError;

@FirehoseNozzle(subscriptionId = "wrongparamsonerror")
public class WrongParamsOnErrorNozzleTest {

	@OnFirehoseEventError
	public void onFirehoseEventError(String s) {
		
	}
	
}
