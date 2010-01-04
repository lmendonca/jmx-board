package br.com.investtools.jmxboard;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A dashboard is a collection of monitored resources.
 * 
 * @author tuler
 * 
 */
@XmlRootElement
public class Dashboard {

	private List<MonitoredResource> resources = new ArrayList<MonitoredResource>();

	@XmlElement
	public List<MonitoredResource> getResource() {
		return resources;
	}

	public void setResource(List<MonitoredResource> resources) {
		this.resources = resources;
	}

}
