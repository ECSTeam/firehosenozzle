package com.ecsteam.firehose.nozzle;


import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class FirehoseNozzleCreator implements ApplicationListener<ApplicationReadyEvent> {

    private final static String BEAN_SUFFIX = "_FirehoseNozzle";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("************ FirehoseNozzleCreator onApplicationEvent! (" + this.hashCode() + ") " + Calendar.getInstance().getTimeInMillis() + " **************");
        ApplicationContext context = event.getApplicationContext();
        String[] names = context.getBeanNamesForAnnotation(FirehoseNozzle.class);
        for (String name : names) {
            Object bean = context.getBean(name);
            FirehoseReader rdr = (FirehoseReader)context.getAutowireCapableBeanFactory().createBean(FirehoseReader.class, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR,true);
            rdr.initialize(bean);
            context.getAutowireCapableBeanFactory().initializeBean(rdr,name + BEAN_SUFFIX);
        }

    }
}
