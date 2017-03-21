package com.ecsteam.firehose.nozzle;

import com.ecsteam.firehose.nozzle.annotation.FirehoseNozzle;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEvent;
import com.ecsteam.firehose.nozzle.annotation.OnFirehoseEventError;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.EventType;
import org.cloudfoundry.doppler.FirehoseRequest;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@ToString
@Service
@Slf4j
@Getter

public class FirehoseReader implements SmartLifecycle {

	public static final String INCORRECT_PARAMS_EVENT_METHOD_MESSAGE = "Incorrect number of parameters passed to OnFirehoseEvent method";
	public static final String INCORRECT_PARAMS_ERROR_EVENT_METHOD_MESSAGE = "Incorrect number of parameters passed to OnFirehoseErrorEvent method";
	public static final String UNMATCHED_PARAMS_EVENT_METHOD_MESSAGE = "Parameter for OnFirehoseEvent method needs to be of type Envelope";
	public static final String UNMATCHED_PARAMS_ERROR_EVENT_METHOD_MESSAGE = "Parameters for OnFirehoseErrorEvent method needs to be of type Throwable";

	private final FirehoseProperties props;
	private final DopplerClient dopplerClient;
	private final String subscriptionId;
	private final Object bean;
	private Method onEventMethod = null;
	private Method onEventErrorMethod = null;
	private HashMap<String, EventType> eventTypes;

	private boolean running = false;
	private boolean isValid = false;

	@Autowired
	public FirehoseReader(FirehoseProperties props, ApplicationContext context, DopplerClient dopplerClient) {
		
		log.debug("************ FirehoseReader CONSTRUCTED! (" + this.hashCode() + ") "
				+ Calendar.getInstance().getTimeInMillis() + " **************");
		log.debug("************ " + this.toString());
		
		this.props = props;
		this.dopplerClient = dopplerClient;

		if (props != null && props.isValidConfiguration()) {
			String[] names = context.getBeanNamesForAnnotation(FirehoseNozzle.class);
			if (names.length == 1) {
				FirehoseNozzle fn = context.findAnnotationOnBean(names[0], FirehoseNozzle.class);
				this.bean = context.getBean(names[0]);
				this.subscriptionId = fn.subscriptionId();
	
				log.debug("************ FirehoseReader CONSTRUCTED! (" + this.hashCode() + ") "
						+ Calendar.getInstance().getTimeInMillis() + " **************");
				log.debug("************ " + this.toString());
	
				eventTypes = new HashMap<String, EventType>();
				
				isValid = true;
			}
			else if (names.length > 1) {
				log.error("****************************************");
				log.error("Cannot instantiate FirehoseReader class as there are multiple beans with the FirehoseNozzle annotation");
				for (String name : names) {
					log.error(name);
				}
				bean = null;
				subscriptionId = "";
			}
			else {
				log.error("****************************************");
				log.error("Cannot instantiate FirehoseReader class as there are no beans with the FirehoseNozzle annotation");
				
				bean = null;
				subscriptionId = "";
			}
		}
		else {
			log.error("****************************************");
			log.error("Cannot instantiate FirehoseReader class as current property set is invalid.  Do you have a class with the EnableFirehose annotation?");
			bean = null;
			subscriptionId = "";
		}

	}

	@Override
	public boolean isAutoStartup() {

		log.debug("************ FirehoseReader isAutoStartup() (" + this.hashCode() + ") "
				+ Calendar.getInstance().getTimeInMillis() + " **************");

		return true;
	}

	@Override
	public void stop(Runnable callback) {

		log.debug("************ FirehoseReader stop(Runnable) (" + this.hashCode() + ") "
				+ Calendar.getInstance().getTimeInMillis() + " **************");

		callback.run();
		stop();
	}

	private void checkForEventMethod(Method method) {

		if (method.isAnnotationPresent(OnFirehoseEvent.class)) {
			OnFirehoseEvent annotationInstance = method.getAnnotation(OnFirehoseEvent.class);

			Class[] methodParams = method.getParameterTypes();
			if (methodParams.length != 1) {
				log.error(
						"*********** incorrect number of parameters declared for onFirehoseEvent annotated method ****");
				throw new BeanCreationException(INCORRECT_PARAMS_EVENT_METHOD_MESSAGE);
			} else {
				if (methodParams[0] == Envelope.class) {
					onEventMethod = method;
					log.debug("************ FirehoseReader onEvent discovered! "
							+ Calendar.getInstance().getTimeInMillis() + " **************");
					log.debug("************ " + onEventMethod.toString());
					EventType[] annotatedTypes = annotationInstance.eventTypes();
					eventTypes = new HashMap<String, EventType>();
					for (EventType type : annotatedTypes) {
						log.debug("****** filtering on type " + type.toString() + "*********");
						eventTypes.put(type.toString(), type);
					}

				} else {
					log.error("*********** single parameter for onFirehoseEvent annotated method is of class "
							+ methodParams[0].toGenericString() + " and needs to be of type Envelope  ****");
					throw new BeanCreationException(UNMATCHED_PARAMS_EVENT_METHOD_MESSAGE);
				}

			}
		}
	}

	private void checkForErrorMethod(Method method) {

		if (method.isAnnotationPresent(OnFirehoseEventError.class)) {

			Class[] methodParams = method.getParameterTypes();
			if (methodParams.length != 1) {
				log.error(
						"*********** incorrect number of parameters declared for onFirehoseEventError annotated method ****");
				throw new BeanCreationException(INCORRECT_PARAMS_ERROR_EVENT_METHOD_MESSAGE);
			} else {
				if (methodParams[0] == Throwable.class) {
					onEventErrorMethod = method;
					log.debug("************ FirehoseReader onEventError discovered! "
							+ Calendar.getInstance().getTimeInMillis() + " **************");
					log.debug("************ " + onEventErrorMethod.toString());
				} else {
					log.error("*********** single parameter for onFirehoseEventError annotated method is of class "
							+ methodParams[0].toGenericString() + " and needs to be of type Throwable  ****");
					throw new BeanCreationException(UNMATCHED_PARAMS_ERROR_EVENT_METHOD_MESSAGE);
				}

			}
		}
	}

	@Override
	public void start() {
		log.debug("************ FirehoseReader start() (" + this.hashCode() + ") "
				+ Calendar.getInstance().getTimeInMillis() + " **************");
		running = true;

		if (bean != null && isValid) {
			Method[] allMethods = bean.getClass().getMethods();
			for (Method method : allMethods) {
				if (onEventMethod == null) {
					checkForEventMethod(method);
				}
				if (onEventErrorMethod == null) {
					checkForErrorMethod(method);
				}
			}

			log.debug("Building a Firehose Request object");
			FirehoseRequest request = FirehoseRequest.builder().subscriptionId(this.subscriptionId).build();

			log.debug("Connecting to the Firehose");
			if (dopplerClient != null) {
				dopplerClient.firehose(request).doOnError(this::receiveConnectionError).retry()
						.subscribe(this::receiveEvent, this::receiveError);
				log.info("Connected to the Firehose");
			}
		}

	}

	@Override
	public void stop() {
		log.debug("************ FirehoseReader stop() (" + this.hashCode() + ") "
				+ Calendar.getInstance().getTimeInMillis() + " **************");
		running = false;
	}

	@Override
	public boolean isRunning() {
		log.debug("************ FirehoseReader isRunning() (" + this.hashCode() + ") "
				+ Calendar.getInstance().getTimeInMillis() + " **************");
		return running;
	}

	@Override
	public int getPhase() {
		log.debug("************ FirehoseReader getPhase() (" + this.hashCode() + ") "
				+ Calendar.getInstance().getTimeInMillis() + " **************");
		return 0;
	}

	private void receiveEvent(Envelope envelope) {

		log.debug("************ FirehoseReader receiveEvent() (" + this.hashCode() + ") "
				+ Calendar.getInstance().getTimeInMillis() + " **************");
		log.debug(envelope.toString());
		log.debug("********************************************************************************");

		if ((eventTypes.containsKey(envelope.getEventType().toString())) && (onEventMethod != null)) {
			try {
				onEventMethod.invoke(bean, envelope);
			} catch (IllegalAccessException e) {
				if (bean != null) {
					log.error("Exception invoking event method on bean " + bean.getClass().getName());
				} else {
					log.error("Exception invoking event method on bean, bean is null");
				}
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				if (bean != null) {
					log.error("Exception passing envelope to event method on bean " + bean.getClass().getName());
				} else {
					log.error("Exception passing envelope to event method on bean, bean is null");
				}
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				if (bean != null) {
					log.error("Exception invoking event method on bean " + bean.getClass().getName());
				} else {
					log.error("Exception invoking event method on bean, bean is null");
				}
				e.printStackTrace();
			}
		}

	}

	private void receiveConnectionError(Throwable error) {
		log.error("Error in connecting to Firehose : {}", error.getMessage());
		if (log.isDebugEnabled()) {
			error.printStackTrace();
		}
	}

	private void receiveError(Throwable error) {

		log.error("Error in receiving Firehose event: {}", error.getMessage());
		if (log.isDebugEnabled()) {
			error.printStackTrace();
		}

		if (onEventErrorMethod != null) {
			try {
				onEventErrorMethod.invoke(bean, error);
			} catch (IllegalAccessException e) {
				if (bean != null) {
					log.error("Exception invoking error method on bean " + bean.getClass().getName());
				} else {
					log.error("Exception invoking error method on bean, bean is null");
				}
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				if (bean != null) {
					log.error("Exception passing envelope to error method on bean " + bean.getClass().getName());
				} else {
					log.error("Exception passing envelope to error method on bean, bean is null");
				}
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				if (bean != null) {
					log.error("Exception invoking error method on bean " + bean.getClass().getName());
				} else {
					log.error("Exception invoking error method on bean, bean is null");
				}
				e.printStackTrace();
			}
		}
	}

}
