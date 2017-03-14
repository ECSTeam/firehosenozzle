package com.ecsteam.firehose.nozzle.annotation.application;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEvent;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEventError;

@FirehoseNozzle(subscriptionId = "noparams")
public class NoParamsNozzleTest {

	@OnFirehoseEvent
	public void onEvent() {
		
	}
	
}
