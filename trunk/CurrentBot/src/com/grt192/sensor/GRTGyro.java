package com.grt192.sensor;

import java.util.Vector;

import com.grt192.core.Sensor;
import com.grt192.event.component.GyroEvent;
import com.grt192.event.component.GyroListener;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * GRTGyro is a continuously running sensor driver that collects and serves data
 * from a single-axis gyro
 * 
 * @author anand
 */
public class GRTGyro extends Sensor implements PIDSource {

	public static final double SPIKE_THRESHOLD = 1.0;
	public static final double CHANGE_THRESHOLD = .01;
	private Gyro gyro;
	private Vector gyroListeners;

	public static double DRIFT_THRESHOLD = 0.5;

	public GRTGyro(int channel, int pollTime, String id) {
		gyro = new Gyro(channel);
		setSleepTime(pollTime);
		setState("Angle", 0.0);
		gyroListeners = new Vector();
		this.id = id;
	}

	public void poll() {
		double previousValue = getState("Angle");
		double angle = gyro.getAngle() * 360.0 / 208.0;

		setState("Angle", angle);

		if (Math.abs(getState("Angle") - previousValue) >= SPIKE_THRESHOLD) {
			notifyGyroSpike();
		}
		if (Math.abs(getState("Angle") - previousValue) >= CHANGE_THRESHOLD) {
			notifyGyroChange();
		}
		notifyGyroListeners();
	}

	protected void notifyGyroSpike() {
		for (int i = 0; i < gyroListeners.size(); i++) {
			((GyroListener) gyroListeners.elementAt(i))
					.didAngleSpike(new GyroEvent(this, GyroEvent.SPIKE,
							getState("Angle")));
		}
	}

	protected void notifyGyroChange() {
		for (int i = 0; i < gyroListeners.size(); i++) {
			((GyroListener) gyroListeners.elementAt(i))
					.didAngleChange(new GyroEvent(this, GyroEvent.CHANGE,
							getState("Angle")));
		}
	}

	protected void notifyGyroListeners() {
		for (int i = 0; i < gyroListeners.size(); i++) {
			((GyroListener) gyroListeners.elementAt(i))
					.didReceiveAngle(new GyroEvent(this, GyroEvent.NEW_DATA,
							getState("Angle")));
		}
	}

	public Vector getGyroListeners() {
		return gyroListeners;
	}

	public void addGyroListener(GyroListener a) {
		gyroListeners.addElement(a);
	}

	public void removeGyroListener(GyroListener a) {
		gyroListeners.removeElement(a);
	}

	public String toString() {
		return "gyro: " + getState("Angle");
	}

	public double pidGet() {
		// fix asap
		return 0;
	}

}