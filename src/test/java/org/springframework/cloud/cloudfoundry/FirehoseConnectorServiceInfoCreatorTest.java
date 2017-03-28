package org.springframework.cloud.cloudfoundry;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.util.EnvironmentAccessor;
import org.springframework.context.ApplicationContext;

import com.ecsteam.firehose.nozzle.serviceinfo.FirehoseConnectorServiceInfo;

public class FirehoseConnectorServiceInfoCreatorTest {

	public CloudFoundryConnector testCloudConnector = new CloudFoundryConnector();
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	protected EnvironmentAccessor mockEnvironment;

	@Autowired
	ApplicationContext context;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		testCloudConnector.setCloudEnvironment(mockEnvironment);

	}

	protected static ServiceInfo getServiceInfo(List<ServiceInfo> serviceInfos, String serviceId) {
		for (ServiceInfo serviceInfo : serviceInfos) {
			if (serviceInfo.getId().equals(serviceId)) {
				return serviceInfo;
			}
		}
		return null;
	}

	protected String readTestDataFile(String fileName) {
		Scanner scanner = null;
		try {
			InputStream stream = getClass().getResourceAsStream(fileName);
			Reader fileReader = new InputStreamReader(stream);

			scanner = new Scanner(fileReader);
			return scanner.useDelimiter("\\Z").next();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	private String getServicePayload(String payloadFile, String serviceName, String name, String serviceType,
			String subscriptionId, String hostname) {
		String payload = readTestDataFile(payloadFile);

		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$name", name);
		payload = payload.replace("$serviceType", serviceType);
		payload = payload.replace("$subId", subscriptionId);
		payload = payload.replace("$hostname", hostname);

		return payload;
	}

	protected static String getServicesPayload(String... servicePayloads) {
		Map<String, List<String>> labelPayloadMap = new HashMap<String, List<String>>();

		for (String payload : servicePayloads) {
			String label = getServiceLabel(payload);

			List<String> payloadsForLabel = labelPayloadMap.get(label);
			if (payloadsForLabel == null) {
				payloadsForLabel = new ArrayList<String>();
				labelPayloadMap.put(label, payloadsForLabel);
			}
			payloadsForLabel.add(payload);
		}

		StringBuilder result = new StringBuilder("{\n");
		int labelSize = labelPayloadMap.size();
		int i = 0;

		for (Map.Entry<String, List<String>> entry : labelPayloadMap.entrySet()) {
			result.append(quote(entry.getKey())).append(":");
			result.append(getServicePayload(entry.getValue()));
			if (i++ != labelSize - 1) {
				result.append(",\n");
			}
		}
		result.append("}");

		return result.toString();

	}

	private static String getServicePayload(List<String> servicePayloads) {
		StringBuilder payload = new StringBuilder("[");

		// In Scala, this would have been servicePayloads mkString "," :-)
		for (int i = 0; i < servicePayloads.size(); i++) {
			payload.append(servicePayloads.get(i));
			if (i != servicePayloads.size() - 1) {
				payload.append(",");
			}
		}
		payload.append("]");

		return payload.toString();
	}

	@SuppressWarnings("unchecked")
	private static String getServiceLabel(String servicePayload) {
		try {
			Map<String, Object> serviceMap = objectMapper.readValue(servicePayload, Map.class);
			return serviceMap.get("label").toString();
		} catch (Exception e) {
			return null;
		}
	}

	private static String quote(String str) {
		return "\"" + str + "\"";
	}

	protected static void assertServiceFoundOfType(ServiceInfo serviceInfo, Class<? extends ServiceInfo> type) {
		assertNotNull(serviceInfo);
		assertThat(serviceInfo, instanceOf(type));
	}

	protected static void assertServiceFoundOfType(List<ServiceInfo> serviceInfos, String serviceId,
			Class<? extends ServiceInfo> type) {
		ServiceInfo serviceInfo = getServiceInfo(serviceInfos, serviceId);
		assertServiceFoundOfType(serviceInfo, type);
	}

	@Test
	public void testSimpleServiceCreation() {

		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(getServicePayload("test-firehose-info.json", "happyService",
						"testService", "firehose", "testSubId", "notARealHost")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		assertServiceFoundOfType(serviceInfos, "testService", FirehoseConnectorServiceInfo.class);

	}

	@Test
	public void testMissingParamsServiceCreation() {

		when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(getServicePayload("test-firehose-info-missing-params.json",
						"missingService", "missingService", "missing-firehose", "missingSubId", "missingARealHost")));

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();

		// this should be a BaseServiceInfo as discovered by the
		// FallbackServiceInfoCreator.
		// however, since FirehoseConnectorServiceInfo extends BaseServiceInfo
		// let's really make sure its just a BaseServiceInfo
		
		assertServiceFoundOfType(serviceInfos, "missingService", BaseServiceInfo.class);
		assertEquals(serviceInfos.size(), 1);

		ServiceInfo sInfo = serviceInfos.get(0);

		assertFalse(sInfo instanceof FirehoseConnectorServiceInfo);

	}

}
