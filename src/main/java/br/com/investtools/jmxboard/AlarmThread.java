package br.com.investtools.jmxboard;

import java.awt.Toolkit;

/**
 * Beeps continually while alarm is on.
 * 
 * @author tuler
 * 
 */
public class AlarmThread extends Thread {

	private static final long BEEP_INTERVAL = 2000;

	private volatile boolean on;

	public AlarmThread() {
		setName("Alarm");
		setDaemon(true);
	}

	@Override
	public void run() {
		while (true) {
			if (on) {
				Toolkit.getDefaultToolkit().beep();
			}

			try {
				Thread.sleep(BEEP_INTERVAL);
			} catch (InterruptedException e) {
			}
		}
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}
}
