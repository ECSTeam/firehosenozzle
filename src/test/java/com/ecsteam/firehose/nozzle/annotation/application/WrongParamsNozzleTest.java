package com.ecsteam.firehose.nozzle.annotation.application;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEvent;

@FirehoseNozzle(subscriptionId = "wrongparams")
public class WrongParamsNozzleTest {

	@OnFirehoseEvent
	public void onEvent(String s) {
		
	}
	
}
