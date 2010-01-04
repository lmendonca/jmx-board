package br.com.investtools.jmxboard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * A resource is any object property exposed through JMX.
 * 
 * @author tuler
 * 
 */
@XmlType
public class MonitoredResource {

	protected transient PropertyChangeSupport pcs = new PropertyChangeSupport(
			this);

	private String title;

	private long period;

	private String serviceURL;

	private String object;

	private String attribute;

	private Double threshold;

	private transient Object value;

	private transient String error;

	private transient Date lastUpdate;

	/**
	 * No-arg constructor for XML construction.
	 */
	public MonitoredResource() {
	}

	/**
	 * Full-arg constructor for code construction.
	 * 
	 * @param title
	 * @param period
	 * @param serviceURL
	 * @param object
	 * @param attribute
	 */
	public MonitoredResource(String title, long period, String serviceURL,
			String object, String attribute) {
		this.title = title;
		this.period = period;
		this.serviceURL = serviceURL;
		this.object = object;
		this.attribute = attribute;
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

	@XmlElement
	public long getPeriod() {
		return period;
	}

	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setPeriod(long period) {
		pcs.firePropertyChange("period", this.period, this.period = period);
	}

	public void setTitle(String title) {
		pcs.firePropertyChange("title", this.title, this.title = title);
	}

	@XmlElement
	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@XmlElement
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setError(String error) {
		pcs.firePropertyChange("error", this.error, this.error = error);
	}

	public String getError() {
		return error;
	}

	public void setValue(Object value) {
		pcs.firePropertyChange("value", this.value, this.value = value);
		setLastUpdate(new Date());
	}

	public Object getValue() {
		return value;
	}

	public void setThreshold(Double threshold) {
		pcs.firePropertyChange("threshold", this.threshold,
				this.threshold = threshold);
	}

	@XmlElement
	public Double getThreshold() {
		return threshold;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	private void setLastUpdate(Date lastUpdate) {
		pcs.firePropertyChange("lastUpdate", this.lastUpdate,
				this.lastUpdate = lastUpdate);
	}

	public boolean isAlarmOn() {
		if (value == null) {
			// no value yet, alarm is on
			return true;
		}

		if (value instanceof Boolean) {
			// if value is boolean and is false, alarm is on
			return !((Boolean) value).booleanValue();

		} else if (value instanceof Number) {
			if (threshold == null) {
				// no threshold set
				return false;
			}

			Number n = (Number) value;
			return n.doubleValue() > threshold.doubleValue();
		}

		return false;
	}

}
