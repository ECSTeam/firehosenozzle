package com.ecsteam.firehose.nozzle.annotation;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OnEvent {
    String eventType() default "all";
}
