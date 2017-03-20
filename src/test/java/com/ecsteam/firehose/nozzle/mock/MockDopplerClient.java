package com.ecsteam.firehose.nozzle.mock;

import org.cloudfoundry.doppler.ContainerMetricsRequest;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.EventType;
import org.cloudfoundry.doppler.FirehoseRequest;
import org.cloudfoundry.doppler.RecentLogsRequest;
import org.cloudfoundry.doppler.StreamRequest;
import org.cloudfoundry.doppler.ValueMetric;

import reactor.core.publisher.Flux;

public class MockDopplerClient implements DopplerClient {

	@Override
	public Flux<Envelope> containerMetrics(ContainerMetricsRequest arg0) {
		return null;
	}

	@Override
	public Flux<Envelope> firehose(FirehoseRequest arg0) {
		
		Envelope envelopeCtr = Envelope.builder()
					.origin("TEST")
					.eventType(EventType.COUNTER_EVENT)
					.build();
		
		Envelope envelopeValue = Envelope.builder()
					.origin("TEST")
					.eventType(EventType.VALUE_METRIC)
					.valueMetric(ValueMetric.builder().name("testValMetric").unit("unit").value(new Double("0.00")).build())
					.build();

		Flux<Envelope> retVal = Flux.just(envelopeCtr, envelopeValue);
		
		return retVal;
	}

	@Override
	public Flux<Envelope> recentLogs(RecentLogsRequest arg0) {
		return null;
	}

	@Override
	public Flux<Envelope> stream(StreamRequest arg0) {
		return null;
	}

}
