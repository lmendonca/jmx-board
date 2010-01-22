package br.com.investtools.jmxboard.table;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import br.com.investtools.jmxboard.MonitoredResource;

public class DashboardTableCellRenderer extends JLabel implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		// text
		if (value instanceof Date) {
			String pattern = "HH:mm:ss";
			DateFormat format = new SimpleDateFormat(pattern);
			setText(format.format((Date) value));
		} else if (value instanceof Number) {
			// optional formatting
			if (table.getModel() instanceof DashboardTableModel) {
				DashboardTableModel model = (DashboardTableModel) table
						.getModel();
				MonitoredResource resource = model.getResource(row);
				String pattern = resource.getFormat();
				if (pattern != null) {
					NumberFormat format = new DecimalFormat(pattern);
					setText(format.format(value));
				} else {
					setText(value.toString());
				}
			} else {
				setText(value.toString());
			}

		} else {
			setText(value != null ? value.toString() : "");
		}

		// background color for alarm indication
		DashboardTableModel model = (DashboardTableModel) table.getModel();
		MonitoredResource resource = model.getResource(row);
		setOpaque(true);
		if (resource.isAlarmOn()) {
			setForeground(Color.RED);
		} else {
			setForeground(Color.BLACK);
		}
		return this;
	}
}
