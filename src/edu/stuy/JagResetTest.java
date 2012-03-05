/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.stuy;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTableKeyNotDefined;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class JagResetTest extends SimpleRobot {

    CANJaguar jag;
    CurrentThing currentThing;
    Joystick jstick;
    boolean isJaguarTimedOut;

    public JagResetTest() {
        isJaguarTimedOut = false;
        currentThing = new CurrentThing(4);
        currentThing.start();
        jstick = new Joystick(1);
        try {
            configureJag();
        }
        catch (CANTimeoutException e) {
            isJaguarTimedOut = true;
        }
    }

    public void robotInit() {
        try {
            resetJagIfBroken();
        } catch (Exception e) {
        }
    }

    public final void configureJag() throws CANTimeoutException {
        jag = new CANJaguar(3);
        jag.setPID(1, 0, 0);
        jag.changeControlMode(CANJaguar.ControlMode.kSpeed);
        jag.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
        jag.configEncoderCodesPerRev(250);
        jag.enableControl();
    }

    public void resetJagIfBroken() {
        try {
            if (isJaguarTimedOut) {
                configureJag();
                isJaguarTimedOut = false;
            }
        } catch (CANTimeoutException e) {
            isJaguarTimedOut = true;
        }
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            resetJagIfBroken();
            try {
                jag.setPID(SmartDashboard.getDouble("P"), SmartDashboard.getDouble("I"), SmartDashboard.getDouble("D"));
                jag.setX(SmartDashboard.getDouble("setpoint"));
            } catch (NetworkTableKeyNotDefined e) {
                SmartDashboard.putDouble("P", 0);
                SmartDashboard.putDouble("I", 0);
                SmartDashboard.putDouble("D", 0);
                SmartDashboard.putDouble("setpoint", 0);
            } catch (CANTimeoutException e) {
                isJaguarTimedOut = true;
            }

            try {
                SmartDashboard.putDouble("rpm", jag.getSpeed());
                SmartDashboard.putInt("faults", (int) (jag.getFaults()));
                SmartDashboard.putDouble("bus voltage", jag.getBusVoltage());
                SmartDashboard.putDouble("battery voltage", DriverStation.getInstance().getBatteryVoltage());
                SmartDashboard.putDouble("output voltage", jag.getOutputVoltage());
                SmartDashboard.putDouble("current", jag.getOutputCurrent());
                SmartDashboard.putDouble("tempreature", jag.getTemperature());
                SmartDashboard.putBoolean("power cycled", jag.getPowerCycled());

            } catch (CANTimeoutException e) {
                isJaguarTimedOut = true;
            }

            SmartDashboard.putDouble("Current thing", currentThing.getCurrent());
        }
    }
}
