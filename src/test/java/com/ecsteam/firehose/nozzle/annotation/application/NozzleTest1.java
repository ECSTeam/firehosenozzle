package com.ecsteam.firehose.nozzle.annotation.application;

import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.EventType;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEvent;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEventError;

@FirehoseNozzle(subscriptionId = "MyDriver")
public class NozzleTest1 {
	
	private boolean gotCounterEvent = false;
	private boolean gotValueMetric = false;
	
	@OnFirehoseEvent(eventTypes={EventType.COUNTER_EVENT})
	public void onEvent(Envelope e) {

		
		if (e.getEventType().compareTo(EventType.COUNTER_EVENT) == 0) {
			gotCounterEvent = true;
		}
		
		if (e.getEventType().compareTo(EventType.VALUE_METRIC) == 0) {
			gotValueMetric = true;
		}
		
	}
	
	@OnFirehoseEventError
	public void onEventError(Throwable t) {
		
	}
	
	public boolean didGetCounterEvent() {
		return gotCounterEvent;
	}
	
	public boolean didGetValueMetric() {
		return gotValueMetric;
	}

}
