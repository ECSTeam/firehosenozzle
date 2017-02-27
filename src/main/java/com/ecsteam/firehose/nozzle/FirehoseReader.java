package com.ecsteam.firehose.nozzle;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@ToString
@Service
public class FirehoseReader {

    private FirehoseProperties props;
    private String subscriptionId;

    @Autowired
    public FirehoseReader(FirehoseProperties props) {
        this.props = props;
    }

    public void initialize(Object bean) {
        FirehoseNozzle fn[] = bean.getClass().getAnnotationsByType(FirehoseNozzle.class);
        this.subscriptionId = fn[0].subscriptionId();

        System.out.println("************ FirehoseReader CONSTRUCTED! (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        System.out.println("************ " + this.toString());
    }

}
