package com.ecsteam.firehose.nozzle.annotation;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Properties;

import org.cloudfoundry.doppler.Envelope;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ecsteam.firehose.nozzle.FirehoseReader;
import com.ecsteam.firehose.nozzle.annotation.application.ApplicationTest1;
import com.ecsteam.firehose.nozzle.annotation.application.NozzleTest1;
import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzleConfiguration;
import com.ecsteam.firehose.nozzle.mock.MockDopplerClientProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationTest1.class, NozzleTest1.class, MockDopplerClientProvider.class})
public class FirehoseNozzleHappyPathTest {

	@Autowired
	ApplicationContext context;

	
	@Test
	public void testDefaultAnnotation() {
		
		assertNotNull(context);
		FirehoseNozzleConfiguration config = (FirehoseNozzleConfiguration)context.getBean(FirehoseNozzleConfiguration.class);
		
		assertNotNull(config);
		
		FirehoseReader reader = (FirehoseReader)context.getBean(FirehoseReader.class);
		
		assertNotNull(reader);		
		
		Object nozzleBean = reader.getBean();
		assertNotNull(nozzleBean);
		
		assertTrue(nozzleBean instanceof NozzleTest1);
		
		Method eventMethod = reader.getOnEventMethod(); 
		assertNotNull(eventMethod);
		assertTrue(eventMethod.getName().equalsIgnoreCase("onEvent"));
		assertTrue(eventMethod.getParameterCount() == 1);
		Class[] params = eventMethod.getParameterTypes();
		assertTrue(params[0] == Envelope.class);
		
		Method errorMethod = reader.getOnEventErrorMethod();
		assertNotNull(errorMethod);
		assertTrue(errorMethod.getName().equalsIgnoreCase("onEventError"));
		assertTrue(errorMethod.getParameterCount() == 1);
		Class[] errorParams = errorMethod.getParameterTypes();
		assertTrue(errorParams[0] == Throwable.class);
		
		NozzleTest1 nozzleTest = (NozzleTest1)nozzleBean;
		assertNotNull(nozzleTest);
		
		assertTrue(nozzleTest.didGetCounterEvent());
		assertFalse(nozzleTest.didGetValueMetric());
		
	}
	
}


