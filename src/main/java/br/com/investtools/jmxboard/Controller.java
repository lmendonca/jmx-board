package br.com.investtools.jmxboard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.remote.JMXConnector;

import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.StackKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {

	private static final Logger logger = LoggerFactory
			.getLogger(Controller.class);

	private transient PropertyChangeSupport pcs = new PropertyChangeSupport(
			this);

	private KeyedObjectPool connectionPool;

	private ScheduledExecutorService executorService;

	private Map<MonitoredResource, Boolean> alarms;

	private AlarmThread alarmThread;

	private boolean alarmOn;

	public Controller() {
		// JMX connector pool
		KeyedPoolableObjectFactory factory = new JMXConnectorObjectFactory();
		connectionPool = new StackKeyedObjectPool(factory);

		// executor service
		executorService = Executors.newSingleThreadScheduledExecutor();

		// alarm states per resource
		alarms = new HashMap<MonitoredResource, Boolean>();

		// alarm thread
		alarmThread = new AlarmThread();
		alarmThread.start();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
	}

	public void addMonitoredResource(final MonitoredResource resource) {
		// schedules de periodic execution of resource refresh
		final ScheduledFuture<?> future = executorService.scheduleAtFixedRate(
				new MonitoredResourceRunnable(this, resource), 0, resource
						.getPeriod(), TimeUnit.MILLISECONDS);

		// if period changes, recreate the task
		resource.addPropertyChangeListener("period",
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						future.cancel(false);
						executorService.scheduleAtFixedRate(
								new MonitoredResourceRunnable(Controller.this,
										resource), 0, resource.getPeriod(),
								TimeUnit.MILLISECONDS);

					}
				});
	}

	public Object getValue(String serviceURL, String object, String attribute,
			String key) throws Exception {
		JMXConnector jmxc = null;
		try {
			// pega conector do pool
			jmxc = (JMXConnector) connectionPool.borrowObject(serviceURL);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

			ObjectName name = new ObjectName(object);
			Object value = mbsc.getAttribute(name, attribute);
			if (value instanceof CompositeData) {
				if (key == null) {
					logger
							.warn(
									"Attribute {}:{} is CompositeData but resource did not define a 'compositeDataKey'",
									object, attribute);
					value = null;
				} else {
					CompositeData data = (CompositeData) value;
					value = data.get(key);
				}
			}
			return value;

		} catch (Exception e) {
			if (null != jmxc) {
				connectionPool.invalidateObject(serviceURL, jmxc);
				jmxc = null;
			}
			throw e;

		} finally {
			if (null != jmxc) {
				connectionPool.returnObject(serviceURL, jmxc);
			}
		}
	}

	public void setAlarm(MonitoredResource resource, boolean alarmOn) {
		this.alarms.put(resource, alarmOn);
		if (!isAlarmOn() && alarmOn) {
			// turn it on
			setAlarmOn(alarmOn);

		} else if (isAlarmOn() && !alarmOn) {
			// can I turn it off? need to check each resource alarm state
			boolean state = false;
			for (Map.Entry<MonitoredResource, Boolean> entry : this.alarms
					.entrySet()) {
				state |= entry.getValue().booleanValue();
			}
			setAlarmOn(state);
		}
	}

	public boolean isAlarmOn() {
		return alarmOn;
	}

	public void setAlarmOn(boolean alarmOn) {
		pcs.firePropertyChange("alarmOn", this.alarmOn, this.alarmOn = alarmOn);
		alarmThread.setOn(alarmOn);
	}

	public void addDashboard(Dashboard dashboard) {
		for (MonitoredResource resource : dashboard.getResource()) {
			addMonitoredResource(resource);
		}
	}
}
