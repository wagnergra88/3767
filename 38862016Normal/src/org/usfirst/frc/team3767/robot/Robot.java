package org.usfirst.frc.team3767.robot; //3767 package

import edu.wpi.first.wpilibj.SampleRobot; //imports all the files that the program uses
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;

public class Robot extends SampleRobot {
	RobotDrive myRobot;
	Joystick Stick;
	Talon armMotor = new Talon(5);
	DigitalInput limitSwitchUp;
	DigitalInput limitSwitchDown;
	DigitalInput LightsSwitcher;
	Timer timer;
	SendableChooser<String> AutonSwitch;
	final String Lowbar = "Lowbar";
	final String Normal = "Normal";
	final String doNothing = "doNothing";
	final String ChiliFries = "ChiliFries";
	BuiltInAccelerometer accelerometer;

	boolean driveToggle;
	boolean slowDrive;
	double xAxis;
	double yAxis;
	double zAxis;

	public Robot() {
		myRobot = new RobotDrive(9, 8, 6, 7); // FL BL FR BR Motor Order
		myRobot.setExpiration(0.1); // this is where everything is declared
		Stick = new Joystick(1);
		myRobot.setMaxOutput(1);
		limitSwitchUp = new DigitalInput(8);
		limitSwitchDown = new DigitalInput(9);
		LightsSwitcher = new DigitalInput(3);
		timer = new Timer();
		accelerometer = new BuiltInAccelerometer();

	}

	public void robotInit() { // stuff
		driveToggle = false;
		AutonSwitch = new SendableChooser<>();
		AutonSwitch.addDefault("Lowbar", Lowbar);
		AutonSwitch.addDefault("ChiliFries", ChiliFries);
		AutonSwitch.addDefault("Normal", Normal);
		AutonSwitch.addDefault("doNothing", doNothing);
		SmartDashboard.putData("AutonomousModes", AutonSwitch);
}

	@Override
	public void operatorControl() { // tele-op mode
		myRobot.setSafetyEnabled(false); // declaring stuff
		accelerometer(); // calls the accelerometer function
		while (isOperatorControl() && isEnabled()) {
			while (isOperatorControl() && isEnabled() && driveToggle == false) { // tank
				myRobot.tankDrive(-1 * Stick.getRawAxis(1), -1 * Stick.getRawAxis(5)); // speed of robot in (-1)		
				arm(Stick.getRawButton(6), Stick.getRawButton(5), (Stick.getRawAxis(3) > .1));
				accelerometer(); // calls the accelerometer function
				while (Stick.getRawAxis(2) > .1) { // slow button
					myRobot.tankDrive(-0.5 * Stick.getRawAxis(1), -0.5 * Stick.getRawAxis(5)); // reduces speed for maneuvering (-.5)																								
					arm(Stick.getRawButton(6), Stick.getRawButton(5), Stick.getRawAxis(3) > .1);
					accelerometer(); // calls the accelerometer function
				}
				if (Stick.getRawButton(8) == true) {
					driveToggle = true; // tank/arcade toggle
					Timer.delay(0.25);
				}
				Timer.delay(0.005);
			}
			while (isOperatorControl() && isEnabled() && driveToggle == true) { // left stick arcade
				myRobot.arcadeDrive(-1 * Stick.getRawAxis(1), -1 * Stick.getRawAxis(0)); // speed of robot and joysticks (-1 dft speed)
				arm(Stick.getRawButton(6), Stick.getRawButton(5), (Stick.getRawAxis(3) > .1));
				accelerometer(); // calls the accelerometer function
				while (Stick.getRawAxis(2) > .1) { // slow button
					myRobot.arcadeDrive(-0.5 * Stick.getRawAxis(1), -0.5 * Stick.getRawAxis(0));
					arm(Stick.getRawButton(6), Stick.getRawButton(5), Stick.getRawAxis(3) > .1);
					accelerometer(); // calls the accelerometer function
				}
				if (Stick.getRawButton(8) == true) {
					driveToggle = false; // tank/arcade toggle
					Timer.delay(0.25);
				}
				Timer.delay(0.005);
			}
		}
	}

	@Override
	public void autonomous() { // autonomous code
		myRobot.setSafetyEnabled(false);
		String autoSelected = (String) AutonSwitch.getSelected();
		System.out.println("Lowbar" + autoSelected);
		switch (autoSelected) {
		case Lowbar: // "Lowbar" mode, puts arm down then drives for 3 seconds
			armMotor.set(0.45);
			Timer.delay(1.0);
			armMotor.set(0);
			myRobot.arcadeDrive(0.75, -.175);//-.175
			Timer.delay(3);
			myRobot.arcadeDrive(0, 0);
			break;
		case Normal: // "Normal" mode, keeps arm up and drives for 5 seconds
			armMotor.set(-0.30);
			myRobot.arcadeDrive(0.85, 0);
			Timer.delay(4);
			myRobot.arcadeDrive(0, 0);
			armMotor.set(0);
			break;
		case ChiliFries: // "ChiliFries" mode, drives forward, puts arm down and drives forward
			Timer.delay(.05); //prevents code failure
			myRobot.arcadeDrive(.50, -.14);//-.09
			Timer.delay(2.5);
			myRobot.arcadeDrive(0, 0);
			armMotor.set(.45);
			Timer.delay(2);
			myRobot.arcadeDrive(.75, 0);
			Timer.delay(3);
			myRobot.arcadeDrive(0, 0);
			armMotor.set(0);
			break;
		case doNothing: // "doNothing" mode, does nothing
		default: // this means that "doNothing" mode is the default mode
			myRobot.arcadeDrive(0, 0);
			break;

		}
	}

	public void arm(boolean up, boolean down, boolean powerUp) { // arm code
		if (up && limitSwitchUp.get() == false) {
			armMotor.set(-0.40); // arm up speed (negative because inverted)
		} else if (down && limitSwitchDown.get() == false) {
			armMotor.set(0.40); // arm down speed
		} else if (powerUp && limitSwitchUp.get() == false) {
			armMotor.set(-0.60); // arm fast up
		} else {
			armMotor.set(0); // does nothing if no value is received
		}
	}


	public void accelerometer() { // gets x, y, and z values from the built in accelerometer and puts the data to the smart dashboard
		xAxis = accelerometer.getX();
		yAxis = accelerometer.getY();
		zAxis = accelerometer.getZ();
		SmartDashboard.putNumber("x", xAxis);
		SmartDashboard.putNumber("y", yAxis);
		SmartDashboard.putNumber("z", zAxis);
	}

}