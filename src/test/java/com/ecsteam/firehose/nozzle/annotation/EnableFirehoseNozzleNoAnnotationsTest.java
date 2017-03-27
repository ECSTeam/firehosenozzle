package com.ecsteam.firehose.nozzle.annotation;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ecsteam.firehose.nozzle.annotation.application.ApplicationTest3;
import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzlePropertiesConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationTest3.class})

public class EnableFirehoseNozzleNoAnnotationsTest {

	@Autowired
	ApplicationContext context;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testNoAnnotations() {
		
		assertNotNull(context);
		
		thrown.expect(NoSuchBeanDefinitionException.class);
		FirehoseNozzlePropertiesConfiguration config = (FirehoseNozzlePropertiesConfiguration)context.getBean(FirehoseNozzlePropertiesConfiguration.class);
		
		
	}
}
