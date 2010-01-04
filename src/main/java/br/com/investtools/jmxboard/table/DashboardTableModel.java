package br.com.investtools.jmxboard.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import br.com.investtools.jmxboard.Dashboard;
import br.com.investtools.jmxboard.MonitoredResource;

public class DashboardTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private Dashboard dashboard;

	public DashboardTableModel(Dashboard dashboard) {
		this.dashboard = dashboard;

		for (int i = 0; i < dashboard.getResource().size(); i++) {
			final int index = i;
			MonitoredResource resource = dashboard.getResource().get(i);
			PropertyChangeListener listener = new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					fireTableRowsUpdated(index, index);
				}
			};
			resource.addPropertyChangeListener("value", listener);
			resource.addPropertyChangeListener("title", listener);
			resource.addPropertyChangeListener("lastUpdate", listener);
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return dashboard.getResource().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		MonitoredResource resource = getResource(rowIndex);
		switch (columnIndex) {
		case 0:
			return resource.getTitle();
		case 1:
			return resource.getValue();
		case 2:
			return resource.getThreshold();
		case 3:
			return resource.getLastUpdate();
		}
		return null;
	}

	public MonitoredResource getResource(int rowIndex) {
		return dashboard.getResource().get(rowIndex);
	}

}
