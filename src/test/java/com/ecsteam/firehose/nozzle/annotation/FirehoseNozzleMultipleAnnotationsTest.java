package com.ecsteam.firehose.nozzle.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ecsteam.firehose.nozzle.FirehoseProperties;
import com.ecsteam.firehose.nozzle.FirehoseReader;
import com.ecsteam.firehose.nozzle.annotation.application.ApplicationTest1;
import com.ecsteam.firehose.nozzle.annotation.application.NozzleTest1;
import com.ecsteam.firehose.nozzle.annotation.application.NozzleTest2;
import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzlePropertiesConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationTest1.class, NozzleTest1.class, NozzleTest2.class})
public class FirehoseNozzleMultipleAnnotationsTest {
	
	@Autowired
	ApplicationContext context;
	
	@Test
	public void testMultipleAnnotations() {
		assertNotNull(context);
		FirehoseNozzlePropertiesConfiguration config = (FirehoseNozzlePropertiesConfiguration)context.getBean(FirehoseNozzlePropertiesConfiguration.class);
		
		assertNotNull(config);
		
		FirehoseProperties props = config.firehoseProperties(context);
		
		assertNotNull(props);

		assertEquals(props.getApiEndpoint(), EnableFirehoseNozzle.DEFAULT_API_ENDPOINT);
		assertEquals(props.getUsername(), EnableFirehoseNozzle.DEFAULT_USERNAME);
		assertEquals(props.getPassword(), EnableFirehoseNozzle.DEFAULT_PASSWORD);
		assertEquals(props.isSkipSslValidation(), EnableFirehoseNozzle.DEFAULT_SKIP_SSL_VALIDATION);
		
		assertTrue(props.isValidConfiguration());

		FirehoseReader reader = (FirehoseReader)context.getBean(FirehoseReader.class);
		
		assertNull(reader);
		
	}
	
}
