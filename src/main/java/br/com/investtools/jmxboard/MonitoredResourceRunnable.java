package br.com.investtools.jmxboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for querying the JMX attribute periodically.
 * 
 * @author tuler
 * 
 */
public class MonitoredResourceRunnable implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(MonitoredResource.class);

	private Controller controller;

	private MonitoredResource resource;

	public MonitoredResourceRunnable(Controller controller,
			MonitoredResource resource) {
		this.controller = controller;
		this.resource = resource;
	}

	@Override
	public void run() {
		String serviceURL = resource.getServiceURL();
		String object = resource.getObject();
		String attribute = resource.getAttribute();
		String key = resource.getCompositeDataKey();
		try {
			// do the JMX query to get the value
			Object value = controller.getValue(serviceURL, object, attribute, key);

			resource.setValue(value);

			logger.debug("Querying " + resource.getTitle() + ": "
					+ resource.getValue());

		} catch (Exception e) {
			// some error, set the error message
			resource.setError(e.getMessage());
			logger.error("Error querying " + resource.getTitle() + ", "
					+ resource.getError());
		}

		// liga/desliga alarme do resource
		this.controller.setAlarm(resource, resource.isAlarmOn());
	}

}
