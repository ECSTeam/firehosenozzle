package com.ecsteam.firehose.nozzle.annotation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ecsteam.firehose.nozzle.FirehoseProperties;
import com.ecsteam.firehose.nozzle.annotation.application.ApplicationTest2;
import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzlePropertiesConfiguration;
import com.ecsteam.firehose.nozzle.mock.MockDopplerClientProvider;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationTest2.class, MockDopplerClientProvider.class})
public class EnableFirehoseNozzleParameterizedConfigTest {
	
	@Autowired
	ApplicationContext context;
	
	@Test
	public void testParameterizedAnnotation() {
		
		assertNotNull(context);
		FirehoseNozzlePropertiesConfiguration config = (FirehoseNozzlePropertiesConfiguration)context.getBean(FirehoseNozzlePropertiesConfiguration.class);
		
		assertNotNull(config);
		
		FirehoseProperties props = config.firehoseProperties(context);
		
		assertNotNull(props);

		assertEquals(props.getApiEndpoint(), "https://home.api");
		assertEquals(props.getUsername(), "scott");
		assertEquals(props.getPassword(), "tiger");
		assertEquals(props.isSkipSslValidation(), false);
		
	}
}
