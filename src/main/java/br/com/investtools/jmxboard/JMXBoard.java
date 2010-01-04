package br.com.investtools.jmxboard;

import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumnModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import br.com.investtools.jmxboard.table.DashboardTableCellRenderer;
import br.com.investtools.jmxboard.table.DashboardTableModel;

import net.miginfocom.swing.MigLayout;

public class JMXBoard extends JFrame {

	private static final long serialVersionUID = 1L;

	public JMXBoard(Dashboard dashboard) {
		Controller controller = new Controller();
		controller.addDashboard(dashboard);

		JTable table = new JTable();
		table.setModel(new DashboardTableModel(dashboard));
		ResourceBundle rb = ResourceBundle
				.getBundle(JMXBoard.class.getName());
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setHeaderValue(rb.getString("column.title"));
		columnModel.getColumn(1).setHeaderValue(rb.getString("column.value"));
		columnModel.getColumn(2).setHeaderValue(
				rb.getString("column.threshold"));
		columnModel.getColumn(3).setHeaderValue(
				rb.getString("column.lastUpdate"));
		columnModel.getColumn(0).setPreferredWidth(250);
		columnModel.getColumn(1).setPreferredWidth(50);
		columnModel.getColumn(2).setPreferredWidth(50);
		columnModel.getColumn(3).setPreferredWidth(100);
		table
				.setDefaultRenderer(Object.class,
						new DashboardTableCellRenderer());

		MigLayout layout = new MigLayout("fillx", "[fill]");
		setLayout(layout);

		JScrollPane pane = new JScrollPane(table);
		add(pane);

		setTitle(rb.getString("title"));
	}

	/**
	 * @param args
	 * @throws JAXBException
	 */
	public static void main(String[] args) throws JAXBException {
		if (args.length < 1) {
			System.err.println("[file.xml]");
			return;
		}

		// load the file
		JAXBContext c = JAXBContext
				.newInstance("br.com.investtools.jmxmonitor");
		Unmarshaller unmarshaller = c.createUnmarshaller();
		File file = new File(args[0]);
		final Dashboard dashboard = (Dashboard) unmarshaller.unmarshal(file);

		// create the UI and run
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				JMXBoard monitor = new JMXBoard(dashboard);
				monitor.pack();
				monitor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				monitor.setVisible(true);
			}
		});
	}

}
