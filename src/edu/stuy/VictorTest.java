/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.stuy;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class VictorTest extends SimpleRobot {

    Victor test;
    CurrentThing lol;

    public VictorTest() {
        test = new Victor(2);      //CHANGE THIS WAS KIND OF PULLED OUT OF MY ASS
        test.setSafetyEnabled(false);
        lol = new CurrentThing(4); //MAKE SURE THIS IS THE CHANNEL YOU WANT
        lol.start();
    }

    //TODO: possibally grapht the data?
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            SmartDashboard.putDouble("Current thing", lol.getCurrent());
            SmartDashboard.putDouble("battery voltage", DriverStation.getInstance().getBatteryVoltage());
            SmartDashboard.putDouble("channel", test.getChannel());
            SmartDashboard.putDouble("raw PWN value", test.getRaw());
            SmartDashboard.putBoolean("is alive?", test.isAlive());
        }
    }
}
