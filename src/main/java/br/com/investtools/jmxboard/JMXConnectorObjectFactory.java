package br.com.investtools.jmxboard;

import java.io.IOException;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.pool.KeyedPoolableObjectFactory;

/**
 * This class creates the JMXConnector which is kept in an object pool, managed
 * by commons-pool.
 * 
 * @author tuler
 * 
 */
public class JMXConnectorObjectFactory implements KeyedPoolableObjectFactory {

	@Override
	public void activateObject(Object serviceURL, Object obj) throws Exception {
	}

	@Override
	public void destroyObject(Object serviceURL, Object obj) throws Exception {
		JMXConnector connector = (JMXConnector) obj;
		connector.close();
	}

	@Override
	public Object makeObject(Object serviceURL) throws Exception {
		JMXServiceURL url = new JMXServiceURL(serviceURL.toString());
		JMXConnector jmxc = JMXConnectorFactory.connect(url);
		return jmxc;
	}

	@Override
	public void passivateObject(Object serviceURL, Object obj) throws Exception {
	}

	@Override
	public boolean validateObject(Object serviceURL, Object obj) {
		JMXConnector connector = (JMXConnector) obj;
		try {
			connector.connect();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
