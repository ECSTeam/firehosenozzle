package com.ecsteam.firehose.nozzle;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.FirehoseRequest;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@ToString
@Service
@Slf4j
public class FirehoseReader implements SmartLifecycle {

    private final FirehoseProperties props;
    private final ReactorDopplerClient dopplerClient;
    private final String subscriptionId;
    private final Object bean;

    private boolean running = false;


    @Autowired
    public FirehoseReader(FirehoseProperties props, ApplicationContext context, ReactorDopplerClient dopplerClient) {
        this.props = props;
        this.dopplerClient = dopplerClient;

        String[] names = context.getBeanNamesForAnnotation(FirehoseNozzle.class);
        FirehoseNozzle fn = context.findAnnotationOnBean(names[0],FirehoseNozzle.class);
        this.bean = context.getBean(names[0]);
        this.subscriptionId = fn.subscriptionId();

        log.info("************ FirehoseReader CONSTRUCTED! (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        log.info("************ " + this.toString());
    }

    @Override
    public boolean isAutoStartup() {
        log.info("************ FirehoseReader isAutoStartup() (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        log.info("************ FirehoseReader stop(Runnable) (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        callback.run();
        stop();
    }

    @Override
    public void start() {
        log.info("************ FirehoseReader start() (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        running = true;

        log.info("Connecting to the Firehose");
        FirehoseRequest request = FirehoseRequest.builder()
                .subscriptionId(this.subscriptionId).build();

        dopplerClient.firehose(request)
                .doOnError(this::receiveError)
                .retry()
                .subscribe(this::receiveEvent, this::receiveError);


    }

    @Override
    public void stop() {
        log.info("************ FirehoseReader stop() (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        running = false;
    }

    @Override
    public boolean isRunning() {
        log.info("************ FirehoseReader isRunning() (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        return running;
    }

    @Override
    public int getPhase() {
        log.info("************ FirehoseReader getPhase() (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        return 0;
    }


    private void receiveEvent(Envelope envelope) {
        log.info("************ FirehoseReader receiveEvent() (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        log.info(envelope.toString());
        log.info("********************************************************************************");


        /*
        switch (envelope.getEventType()) {
            case COUNTER_EVENT:
            case VALUE_METRIC:
                break;
            default:
        }
        */
    }

    private void receiveError(Throwable error) {
        log.error("Error in receiving Firehose event: {}", error.getMessage());
        if (log.isDebugEnabled()) {
            error.printStackTrace();
        }
    }
}
