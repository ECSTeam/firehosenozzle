package com.ecsteam.firehose.nozzle;


import com.ecsteam.firehose.nozzle.annotation.EnableFirehoseNozzle;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Calendar;

@ToString
public class FirehoseProperties {

    private String apiEndpoint;
    private String username;
    private String password;
    private boolean skipSslValidation;

    @Autowired
    public FirehoseProperties(ApplicationContext context) {
        String[] names = context.getBeanNamesForAnnotation(EnableFirehoseNozzle.class);
        EnableFirehoseNozzle efh = context.findAnnotationOnBean(names[0],EnableFirehoseNozzle.class);
        this.apiEndpoint = efh.apiEndpoint();
        this.username = efh.username();
        this.password = efh.password();
        this.skipSslValidation = efh.skipSslValidation();

        System.out.println("************ FirehoseProperties CONSTRUCTED! " + Calendar.getInstance().getTimeInMillis() + " **************");
        System.out.println("************ " + this.toString());
    }
}
