package com.grt192.sensor.spot;

import com.grt192.controller.spot.SensorServer;
import com.grt192.core.Sensor;
import com.grt192.networking.SocketEvent;
import com.grt192.networking.SocketListener;
import com.grt192.networking.spot.RadiogramClient;
import com.grt192.utils.Util;

/**
 * A NetSensor reads Sensor data from a matching <code>TelemetryServer</code>,
 * effectually making a copy of a remote sensor locally. Events are not
 * replicated.
 * 
 * @author ajc
 */
public class NetSensor extends Sensor implements SocketListener {

	private RadiogramClient client;

	/**
	 * Opens a sensor that reads data from a remote sensor
	 * 
	 * @param sourceAddress
	 *            address origin of remote sensor
	 * @param port
	 *            port remote sensor server is broadcasting on
	 */
	public NetSensor(String sourceAddress, int port) {
		client = new RadiogramClient(sourceAddress, port);
		client.start();
		// setPrinting(true);
	}

	public void start() {
		client.addSocketListener(this);
		// these don't start.
	}

	public void poll() {
		// no poll: only reads data on event
	}

	public void onConnect(SocketEvent e) {
	}

	public void onDisconnect(SocketEvent e) {
	}

	public void dataRecieved(SocketEvent e) {
		String s = e.getData();
		String type = s.substring(0, s.indexOf(SensorServer.SEPARATOR));
		String message = s.substring(s.indexOf(SensorServer.SEPARATOR) + 1);
		setState(type, Util.doubleValue(message));
		// log("Type:\t" + type);
		// log("Data:\t" + message);
	}
}
