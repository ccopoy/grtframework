package com.grt192.core;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author anand
 */
public abstract class Controller extends GRTObject {

    protected Hashtable mechanisms;
    protected boolean running;

    public Controller() {
        mechanisms = new Hashtable();
        running = false;
    }

    public void start() {
        running = true;
        startListening();
        super.start();
    }

    public abstract void run();

    public void addMechanism(String name, Mechanism m) {
        mechanisms.put(name, m);
    }

    public Mechanism getMechanism(String name) {
        return (Mechanism) mechanisms.get(name);
    }

    public Hashtable getMechanisms() {
        return mechanisms;
    }

    public void setMechanisms(Hashtable mechanisms) {
        this.mechanisms = mechanisms;
    }

    public void stopControl() {
        stopControl(false);
    }

    public void stopControl(boolean disable) {
        if (disable) {
            Enumeration e = mechanisms.elements();
            while (e.hasMoreElements()) {
                ((Mechanism) (e.nextElement())).disable();
            }
        }
        //halt all mechanisms
        Enumeration mechs = mechanisms.elements();
        while (mechs.hasMoreElements()) {
            Enumeration actuators = ((Mechanism) mechs.nextElement()).getActuators().elements();
            while (actuators.hasMoreElements()) {
                Actuator a = ((Actuator) actuators.nextElement());
                a.clearQueue(Actuator.MAGIC);
                a.halt();
            }
        }

        System.out.println("STOPPING: " + this);
        stopListening();
        running = false;

    }

    public void startListening() {
    }

    public void stopListening() {
    }

    public boolean isRunning() {
        return running;
    }
}
