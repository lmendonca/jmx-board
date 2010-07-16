package br.com.investtools.jmxboard;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import asg.cliche.ShellFactory;

public class JMXBoardMain {

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
		JAXBContext c = JAXBContext.newInstance("br.com.investtools.jmxboard");
		Unmarshaller unmarshaller = c.createUnmarshaller();
		File file = new File(args[0]);
		Dashboard dashboard = (Dashboard) unmarshaller.unmarshal(file);

		if (args.length > 1 && "1".equals(args[1])) {
			consoleBoard(dashboard);
		} else {
			jframeBoard(dashboard);
		}

	}

	private static void consoleBoard(final Dashboard dashboard) {
		JMXConsoleBoard board = new JMXConsoleBoard(dashboard);

		try {
			ShellFactory.createConsoleShell("JMX Monitor", "", board)
					.commandLoop();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void jframeBoard(final Dashboard dashboard) {
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
