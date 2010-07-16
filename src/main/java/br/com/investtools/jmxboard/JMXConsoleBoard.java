/**
 * 
 */
package br.com.investtools.jmxboard;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import asg.cliche.Command;

/**
 * @author lmendonca
 * 
 */
public class JMXConsoleBoard {

	private Dashboard dashboard;

	public JMXConsoleBoard(Dashboard dashboard) {
		Controller controller = new Controller();
		controller.addDashboard(dashboard);
		this.dashboard = dashboard;
	}

	@Command
	public void print() {
		ResourceBundle rb = ResourceBundle.getBundle(JMXBoard.class.getName());

		List<MonitoredResource> resources = dashboard.getResource();

		int size = resources.size();
		Object[][] text = new Object[size + 1][4];
		int[] length = new int[4];
		int total = 0;

		text[total][0] = rb.getString("column.title");
		text[total][1] = rb.getString("column.value");
		text[total][2] = rb.getString("column.threshold");
		text[total][3] = rb.getString("column.lastUpdate");

		length[0] = text[total][0].toString().length();
		length[1] = text[total][1].toString().length();
		length[2] = text[total][2].toString().length();
		length[3] = text[total][3].toString().length();
		total++;

		for (MonitoredResource resource : resources) {
			text[total][0] = resource.getTitle();
			text[total][1] = resource.getValue();
			text[total][2] = resource.getThreshold();
			text[total][3] = resource.getLastUpdate();

			if (text[total][0] != null) {
				length[0] = Math.max(length[0], text[total][0].toString()
						.length());
			} else {
				text[total][0] = "";
			}
			if (text[total][1] != null) {
				length[1] = Math.max(length[1], text[total][1].toString()
						.length());
			} else {
				text[total][1] = "";
			}
			if (text[total][2] != null) {
				length[2] = Math.max(length[2], text[total][2].toString()
						.length());
			} else {
				text[total][2] = "";
			}
			if (text[total][3] != null) {
				length[3] = Math.max(length[3], text[total][3].toString()
						.length());
			} else {
				text[total][3] = "";
			}
			total++;
		}

		for (int i = 0; i < total; i++) {
			System.out.print(StringUtils.rightPad(text[i][0].toString(),
					length[0], " ")
					+ "|");
			System.out.print(StringUtils.rightPad(text[i][1].toString(),
					length[1], " ")
					+ "|");
			System.out.print(StringUtils.rightPad(text[i][2].toString(),
					length[2], " ")
					+ "|");
			System.out.println(StringUtils.rightPad(text[i][3].toString(),
					length[3], " ")
					+ "|");
		}
	}
}
