package com.ecsteam.firehose.nozzle;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;

@ToString
public class FirehoseReader {

    private FirehoseProperties props;
    private String subscriptionId;

    @Autowired
    public FirehoseReader(FirehoseProperties props, ApplicationContext context) {
        this.props = props;

        String[] names = context.getBeanNamesForAnnotation(FirehoseNozzle.class);
        FirehoseNozzle fn = context.findAnnotationOnBean(names[0],FirehoseNozzle.class);
        this.subscriptionId = fn.subscriptionId();

        System.out.println("************ FirehoseReader CONSTRUCTED! " + Calendar.getInstance().getTimeInMillis() + " **************");
        System.out.println("************ " + this.toString());
    }
}
