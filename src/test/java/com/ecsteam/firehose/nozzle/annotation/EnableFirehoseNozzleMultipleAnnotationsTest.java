package com.ecsteam.firehose.nozzle.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ecsteam.firehose.nozzle.FirehoseProperties;
import com.ecsteam.firehose.nozzle.annotation.application.ApplicationTest1;
import com.ecsteam.firehose.nozzle.annotation.application.ApplicationTest2;
import com.ecsteam.firehose.nozzle.annotation.application.NozzleTest1;
import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzlePropertiesConfiguration;
import com.ecsteam.firehose.nozzle.mock.MockDopplerClientProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationTest1.class, ApplicationTest2.class, NozzleTest1.class})
public class EnableFirehoseNozzleMultipleAnnotationsTest {

	@Autowired
	ApplicationContext context;
	
	@Test
	public void testMultipleAnnotations() {
		
		assertNotNull(context);
		FirehoseNozzlePropertiesConfiguration config = (FirehoseNozzlePropertiesConfiguration)context.getBean(FirehoseNozzlePropertiesConfiguration.class);
		
		assertNotNull(config);
		
		FirehoseProperties props = config.firehoseProperties(context);
		
		assertNotNull(props);
		
		assertEquals(props.getApiEndpoint(), FirehoseProperties.NO_ENDPOINT_USED);
		assertEquals(props.getUsername(), FirehoseProperties.NO_USERNAME_USED);
		assertEquals(props.getPassword(), FirehoseProperties.NO_PASSWORD_USED);
		assertEquals(props.isSkipSslValidation(), FirehoseProperties.NO_SKIP_SSL_USED);
		
		
		assertFalse(props.isValidConfiguration());
		
	}
	
}
