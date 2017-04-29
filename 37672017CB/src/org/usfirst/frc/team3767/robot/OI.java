package org.usfirst.frc.team3767.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;

import org.usfirst.frc.team3767.robot.commands.ExampleCommand;

@SuppressWarnings("unused")
public class OI {
	BuiltInAccelerometer BIA;
	Joystick Stick = new Joystick(1);
	boolean driveToggle = Stick.getRawButton(8);
	double tankleftstick = Stick.getRawAxis(1);
	double slowDrive = Stick.getRawAxis(2);
	double xAxis = BIA.getX();
	double yAxis = BIA.getY();
	double zAxis = BIA.getZ();
}