package com.ecsteam.firehose.nozzle.annotation;

import java.util.Properties;

import org.hamcrest.CustomMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ecsteam.firehose.nozzle.FirehoseReader;
import com.ecsteam.firehose.nozzle.annotation.application.ApplicationTest1;
import com.ecsteam.firehose.nozzle.annotation.application.NoParamsNozzleTest;
import com.ecsteam.firehose.nozzle.annotation.application.NoParamsOnErrorNozzleTest;
import com.ecsteam.firehose.nozzle.annotation.application.WrongParamsNozzleTest;
import com.ecsteam.firehose.nozzle.annotation.application.WrongParamsOnErrorNozzleTest;
import com.ecsteam.firehose.nozzle.mock.MockDopplerClientProvider;

@RunWith(SpringJUnit4ClassRunner.class)
// @SpringBootTest(classes = {ApplicationTest1.class, NoParamsNozzleTest.class})
public class FirehoseNozzleNoParamsTest {

	// @Autowired
	ApplicationContext context;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setSystemProperty() {
		Properties properties = System.getProperties();
		properties.setProperty("spring.profiles.active", "test");

	}

	@Test
	public void testNoParamsOnEvent() {

		Object[] sources = new Object[3];
		sources[0] = ApplicationTest1.class;
		sources[1] = NoParamsNozzleTest.class;
		sources[2] = MockDopplerClientProvider.class;

		String[] args = new String[1];
		args[0] = "";

		thrown.expect(ApplicationContextException.class);
		thrown.expectCause(
				new CauseMatcher(BeanCreationException.class, FirehoseReader.INCORRECT_PARAMS_EVENT_METHOD_MESSAGE));
		context = SpringApplication.run(sources, args);

	}
	
	@Test
	public void testWrongParamsOnEvent() {

		Object[] sources = new Object[3];
		sources[0] = ApplicationTest1.class;
		sources[1] = WrongParamsNozzleTest.class;
		sources[2] = MockDopplerClientProvider.class;

		String[] args = new String[1];
		args[0] = "";

		thrown.expect(ApplicationContextException.class);
		thrown.expectCause(
				new CauseMatcher(BeanCreationException.class, FirehoseReader.UNMATCHED_PARAMS_EVENT_METHOD_MESSAGE));
		context = SpringApplication.run(sources, args);

	}

	@Test
	public void testNoParamsOnErrorEvent() {

		Object[] sources = new Object[3];
		sources[0] = ApplicationTest1.class;
		sources[1] = NoParamsOnErrorNozzleTest.class;
		sources[2] = MockDopplerClientProvider.class;

		String[] args = new String[1];
		args[0] = "";

		thrown.expect(ApplicationContextException.class);
		thrown.expectCause(new CauseMatcher(BeanCreationException.class,
				FirehoseReader.INCORRECT_PARAMS_ERROR_EVENT_METHOD_MESSAGE));
		context = SpringApplication.run(sources, args);

	}
	
	@Test
	public void testWrongParamsOnErrorEvent() {

		Object[] sources = new Object[3];
		sources[0] = ApplicationTest1.class;
		sources[1] = WrongParamsOnErrorNozzleTest.class;
		sources[2] = MockDopplerClientProvider.class;

		String[] args = new String[1];
		args[0] = "";

		thrown.expect(ApplicationContextException.class);
		thrown.expectCause(new CauseMatcher(BeanCreationException.class,
				FirehoseReader.UNMATCHED_PARAMS_ERROR_EVENT_METHOD_MESSAGE));
		context = SpringApplication.run(sources, args);

	}

	private static class CauseMatcher extends TypeSafeMatcher<Throwable> {

		private final Class<? extends Throwable> type;

		private final String expectedMessage;

		public CauseMatcher(Class<? extends Throwable> type, String expectedMessage) {
			this.type = type;
			this.expectedMessage = expectedMessage;
		}

		@Override

		protected boolean matchesSafely(Throwable item) {
			return item.getClass().isAssignableFrom(type)
					&& item.getMessage().contains(expectedMessage);
		}

		@Override

		public void describeTo(Description description) {
			description.appendText("expects type ")
					.appendValue(type)
					.appendText(" and a message ")
					.appendValue(expectedMessage);
		}
	}

}
