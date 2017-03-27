package com.ecsteam.firehose.nozzle.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Properties;

import static org.mockito.Mockito.mock;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ecsteam.firehose.nozzle.FirehoseProperties;
import com.ecsteam.firehose.nozzle.FirehoseReader;
import com.ecsteam.firehose.nozzle.annotation.application.ApplicationTest4;
import com.ecsteam.firehose.nozzle.configuration.FirehoseNozzlePropertiesConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ApplicationTest4.class})
public class EnableFirehoseNozzlePropertySubstitutionTest {
	@Autowired
	ApplicationContext context;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	// this will get the class to load the application-test.properties file
	// in the test/resources directory
	
	@BeforeClass
	public static void setSystemProperty() {
		Properties properties = System.getProperties();
		properties.setProperty("spring.profiles.active", "test");

	}
	
	@Test
	public void testAnnotationPropertySubstitutions() {
		
		assertNotNull(context);
		FirehoseNozzlePropertiesConfiguration config = (FirehoseNozzlePropertiesConfiguration)context.getBean(FirehoseNozzlePropertiesConfiguration.class);
		
		assertNotNull(config);
		
		FirehoseProperties props = config.firehoseProperties(context);
		
		assertNotNull(props);

		assertEquals(props.getApiEndpoint(), "http://prop.endpoint");
		assertEquals(props.getUsername(), "prop_username");
		assertEquals(props.getPassword(), "prop_password");
		assertEquals(props.isSkipSslValidation(), true);
		
		assertTrue(props.isValidConfiguration());
		
		thrown.expect(NoSuchBeanDefinitionException.class);
		FirehoseReader reader = (FirehoseReader)context.getBean(FirehoseReader.class);
		
	}
}
