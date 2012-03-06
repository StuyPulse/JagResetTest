/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.stuy;

import edu.wpi.first.wpilibj.AnalogChannel;
import java.util.Vector;

/**
 *
 * @author 694
 */
public class CurrentThing {

    private AnalogChannel analog;
    private Vector measurements;
    private Thread updateMeasurements;

    public CurrentThing(int channel) {
        analog = new AnalogChannel(channel);
        measurements = new Vector();
        updateMeasurements = new Thread() {

            public void run() {
                synchronized (measurements) {
                    measurements.addElement(new Double(analog.getVoltage()));
                    measurements.removeElementAt(0);
                }
            }
        };
    }

    public void start() {
        updateMeasurements.start();
    }

    public double getCurrent() {
        double sum = 0;
        synchronized (measurements) {
            for (int i = 0; i < measurements.size(); i++) {
                sum += ((Double) measurements.elementAt(i)).doubleValue();
            }
            return sum / measurements.size();
        }
    }
}
