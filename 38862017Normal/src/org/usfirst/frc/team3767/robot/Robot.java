package org.usfirst.frc.team3767.robot; // This is the 3767 package.
							// Here is where all the files we use in the code are imported from wpilib and opencv

import edu.wpi.first.wpilibj.SampleRobot; // "SampleRobot", the SampleRobot class.
import edu.wpi.first.wpilibj.RobotDrive; // "RobotDrive", the RobotDrive class.
import edu.wpi.first.wpilibj.SPI; // "SPI", used to reference the SPI port on the roboRIO.
import edu.wpi.first.wpilibj.Joystick; // "Joystick", used when assigning joysticks.
import edu.wpi.first.wpilibj.Timer; // "Timer", used to create a delay in the code.
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser; // "SendableChooser", a type of variable used in the code.
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; // "SmartDashboard", used when pushing/pulling data to/from the SmartDashboard.
import edu.wpi.first.wpilibj.Talon; // "Talon", used to reference the motor controller used on our robot.
import edu.wpi.cscore.UsbCamera; // "UsbCamera", the USB camera code we use.
import edu.wpi.first.wpilibj.ADXRS450_Gyro; // "ADXRS450_Gyro", the gyroscope we use.
import edu.wpi.first.wpilibj.BuiltInAccelerometer; // "BuiltInAccelerometer", used to reference the accelerometer that is built into the roboRIO.
import edu.wpi.first.wpilibj.CameraServer; // "CameraServer", used in camera code.
import edu.wpi.first.wpilibj.Encoder; // "Encoder", used to reference Encoders.

@SuppressWarnings("unused") // Suppresses warnings that pop up due to something that is unused, used to make code cleaner.
public class Robot extends SampleRobot { // The "Robot" class, where variables are assigned.
	RobotDrive myRobot; // Creates new RobotDrive variable "myRobot".
	Joystick Stick1; // Creates new Joystick variable "Stick1".
	Joystick Stick2; // Creates new Joystick variable "stick2".
	//	"*MotorControllerType* *MotorName* = new *MotorControllerType(roboRIO Input);" (Example motor controller setup).
	Talon agitatorMotor = new Talon(5); // Ball agitator motor, on port 5.
	Talon pickupMotor = new Talon (6); // Ball pickup motor, on port 6.
	Talon shooterMotor = new Talon (7); // Shooter motor, on port 7.
	Talon climbMotor = new Talon (8); // Climb motor, on port 8.
	Timer timer; // Creates new Timer variable "timer".
	//	All of the autonomous SendableChoosers that show up on the SmartDashboard.
	SendableChooser<String> AutonSwitch; // Creates new SendableChooser variable "AutonSwitch".
	SendableChooser<String> DelaySwitch; // Creates new SendableChooser variable "DelaySwitch".
	SendableChooser<String> driveAfterSwitch; // Creates new SendableChooser variable "driveAfterSwitch".
	
//	ADXRS450_Gyro gyro = new ADXRS450_Gyro(); // Creates new ADXRS450_Gyro variable "gyro".

	BuiltInAccelerometer accelerometer; // Creates new BuiltInAccelerometer variable "accelerometer".
	boolean driveToggle; // Creates new boolean variable "driveToggle".
	boolean shootToggle = false; // 2/18
	boolean driveAfterBool = false; // Creates new boolean variable "driveAfterBool", and sets it to false.
	double shooterEnc; // Creates new double variable "shooterEnc".
	double slowDrive; // Creates new double variable "slowDrive".
	double xAxis; // Creates new double variable "xAxis".
	double yAxis; // Creates new double variable "yAxis".
	double zAxis;

	Encoder Enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X); // Creates new Encoder variable "Enc".

	public Robot() { // Public "Robot()", where different things are declared.
		myRobot = new RobotDrive(1, 2, 3, 4); // FL(1), BL(2), FR(3), BR(4), drive motor inputs on the roboRIO.
		myRobot.setExpiration(0.1); // Sets the expiration on myRobot to "0.1".
		Stick1 = new Joystick(1); // Sets joystick "Stick" to port 1.
		Stick2 = new Joystick(2); // Sets joystick "Stick2" to port 2
		myRobot.setMaxOutput(1); // Sets the max output of myRobot to "1".
		timer = new Timer(); // Sets variable "timer" to a Timer.
		accelerometer = new BuiltInAccelerometer(); // Sets "accelerometer" to the BuiltInAccelerometer.

	}

	@Override
	public void robotInit() { // Initialization Stuff
/**		new Thread(() -> { // Code for cameras.
			UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(); // Creates string "camera1", and starts it.
			camera1.setResolution(50, 50); // Sets "camera1"'s resolution to 140x140.
			UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(); // Creates string "camera2", and starts it.
			camera2.setResolution(50, 50); // Sets "camera2"'s resolution to 140x140.
		}).start(); // Starts it. **/

		driveToggle = false; // Sets variable driveToggle to false.

		AutonSwitch = new SendableChooser<>(); // Auto Stuff.
		AutonSwitch.addObject("LeftGear", "LeftGear"); // 2/18
		AutonSwitch.addObject("RightGear", "RightGear"); // 2/18
		AutonSwitch.addObject("MiddleGear", "MiddleGear"); // Adds object "MiddleGear" to SmartDashboard.
		AutonSwitch.addDefault("Shoot", "Shoot"); // Adds object "Shoot" to SmartDashboard.
		AutonSwitch.addObject("doNothing", "doNothing"); // Adds object "doNothing" to SmartDashboard.
		SmartDashboard.putData("AutonomousModes", AutonSwitch); // Puts data "AutonomousModes" onto SmartDashboard.
		DelaySwitch = new SendableChooser<>(); // Creates new SendableChooser "DelaySwitch".
		DelaySwitch.addObject("5s", "5s"); // Adds object "5s" to the SmartDashboard.
		DelaySwitch.addObject("2.5s", "2.5s"); // Adds object "2.5s" to the SmartDashboard.
		DelaySwitch.addDefault("0s", "0s"); // Adds object "0s" to the SmartDashboard.
		SmartDashboard.putData("DelayTimes", DelaySwitch); // Puts data "DelayTimes" to the SmartDashboard.
		driveAfterSwitch = new SendableChooser<>(); // Creates new SendableChooser "driveAfterSwitch".
		driveAfterSwitch.addObject("Drive Away", "driveAway"); // Adds object "Drive Away" to the SmartDashboard.
		driveAfterSwitch.addDefault("Stop", "stop"); // Adds default "Stop" to the SmartDashboard.
		driveAfterSwitch.addObject("Shoot","shoot" );// 2/18
		SmartDashboard.putData("AutonEnd", driveAfterSwitch); // Puts data "AutonEnd" to the SmartDashboard.

	}

	@Override
	public void operatorControl() { // Tele-Op mode.
		myRobot.setSafetyEnabled(false); // Declaring stuff.
		SmartDashboardStuff(); // Calls the SmartDashboardStuff.
		while (isOperatorControl() && isEnabled()) { // While it is Tele-Op and the robot is enabled, do this:
			while (isOperatorControl() && isEnabled() && driveToggle == false) { // Tank mode.
				myRobot.tankDrive(-1 * Stick1.getRawAxis(1), -1 * Stick1.getRawAxis(5)); // Speed of robot (-1/100%).
				ballcollector(Stick1.getRawButton(6), Stick1.getRawButton(2)); // If code is blue, it means that it is drive code commented out for
				shooter(Stick1.getRawAxis(3) > 0.1); // one of the sticks. Stick1 can drive, use slow drive, and switch drive modes,
				climbMotor.set(1 * Stick1.getRawAxis(2));
//				climber(Stick1.getRawButton(4)); // Stick2 can only control the ballcollector, shooter, etc.
				SmartDashboardStuff(); // Calls the SmartDashboardStuff.
				while (Stick1.getRawButton(5)) { // Slow button.
					myRobot.tankDrive(-0.5 * Stick1.getRawAxis(1), -0.5 * Stick1.getRawAxis(5)); // Reduces robot speed for maneuvering (-.5/50%).
					ballcollector(Stick1.getRawButton(6), Stick1.getRawButton(2)); // If either of these buttons are pressed, do the aligned actions.
					shooter(Stick1.getRawAxis(3) > 0.1); // If button 1 on Stick2 is pressed, do the aligned actions.
					climbMotor.set(1 * Stick1.getRawAxis(2));
//					climber(Stick1switch.getRawButton(5)); // If button 4 on Stick2 is pressed, do the aligned actions.
					SmartDashboardStuff(); // Calls the SmartDashboardStuff.
				}
				if (Stick1.getRawButton(8) == true) { // If button 8 on Stick1 is pressed, do this:
					driveToggle = true; // Tank/arcade toggle variable.
					Timer.delay(0.25); // 0.25 second delay.
				}
/**				myRobot.tankDrive(-1 * Stick2.getRawAxis(1), -1 * Stick2.getRawAxis(5)); // Speed of robot (-1/100%)
				ballcollector(Stick2.getRawButton(6), Stick2.getRawButton(2));
				shooter(Stick2.getRawAxis(3) > 0.1);
				climbMotor.set(1 * Stick2.getRawAxis(2));
//				climber(Stick2.getRawButton(5));
				SmartDashboardStuff(); // Calls the SmartDashboardStuff
				while (Stick2.getRawButton(5)) { // Slow button
					myRobot.tankDrive(-0.5 * Stick2.getRawAxis(1), -0.5 * Stick2.getRawAxis(5)); // Reduces robot speed for maneuvering (-.5/50%).
					ballcollector(Stick2.getRawButton(6), Stick2.getRawButton(2));
					shooter(Stick2.getRawAxis(3) > 0.1);
					climbMotor.set(1 * Stick2.getRawAxis(2));
//					climber(Stick2.getRawButton(5));
					SmartDashboardStuff(); // Calls the SmartDashboardStuff
				}
				if (Stick2.getRawButton(8) == true) {
					driveToggle = true; // Tank/arcade toggle
					Timer.delay(0.25);
				}
				Timer.delay(0.005); // 0.005 second delay. **/
			}
			while (isOperatorControl() && isEnabled() && driveToggle == true) { // Left stick arcade.
				myRobot.arcadeDrive(-1 * Stick1.getRawAxis(1), -1 * Stick1.getRawAxis(0)); // Speed of robot (-1/100%).
				ballcollector(Stick1.getRawButton(6), Stick1.getRawButton(2));
				shooter(Stick1.getRawAxis(3) > 0.1);
				climbMotor.set(1 * Stick1.getRawAxis(2));
//				climber(Stick1.getRawButton(5));
				SmartDashboardStuff(); // Calls the SmartDashboardStuff.
				while (Stick1.getRawButton(5)) { // Slow button.
					myRobot.arcadeDrive(-0.5 * Stick1.getRawAxis(1), -0.5 * Stick1.getRawAxis(0)); // Reduces robot speed for maneuvering (-.5/50%).
					ballcollector(Stick1.getRawButton(6), Stick1.getRawButton(2));
					shooter(Stick1.getRawAxis(3) > 0.1);
					climbMotor.set(1 * Stick1.getRawAxis(2));
//					climber(Stick1.getRawButton(5));
					SmartDashboardStuff(); // Calls the SmartDashboardStuff.
				}
				if (Stick1.getRawButton(8) == true) { // If button 8 on Stick1 is pressed, do this:
					driveToggle = false; // Tank/arcade toggle variable.
					Timer.delay(0.25); // 0.25 second delay.
				}
/**				myRobot.arcadeDrive(-1 * Stick2.getRawAxis(1), -1 * Stick2.getRawAxis(0)); // Speed of robot and joyticks (-1 dft speed)
				ballcollector(Stick2.getRawButton(6), Stick2.getRawButton(2));
				shooter(Stick2.getRawAxis(3) > 0.1);
				climbMotor.set(1 * Stick2.getRawAxis(2));
//				climber(Stick2.getRawButton(5));
				SmartDashboardStuff(); // Calls the SmartDashboardStuff
				while (Stick2.getRawButton(5)) { // Slow button
					myRobot.arcadeDrive(-0.5 * Stick2.getRawAxis(1), -0.5 * Stick2.getRawAxis(0));
					ballcollector(Stick2.getRawButton(6), Stick2.getRawButton(2));
					shooter(Stick2.getRawAxis(3) > 0.1);
					climbMotor.set(1 * Stick2.getRawAxis(2));
//					climber(Stick2.getRawButton(5));
					SmartDashboardStuff(); // calls the SmartDashboardStuff
				}
				if (Stick2.getRawButton(8) == true) {
					driveToggle = false; // tank/arcade toggle
					Timer.delay(0.05);
				}
				Timer.delay(0.25); // 0.005 second delay. **/
			}
		}
	}

	@Override
	public void autonomous() { // Autonomous code.
		myRobot.setSafetyEnabled(false);
		String autonSelected = AutonSwitch.getSelected();
		System.out.println("Auton Selected:" + autonSelected);
		String delaySelected = DelaySwitch.getSelected();
		System.out.println("Delay Selected:" + delaySelected);
		String switchSelected = driveAfterSwitch.getSelected();
		System.out.println("Drive After:" + switchSelected);
		switch (delaySelected) {
		case "5s":
			Timer.delay(5);
			break;
		case "2.5s":
			Timer.delay(2.5);
			break;
		case "0s":
			Timer.delay(0);
			break;
		}
		switch (switchSelected){
		case "driveAway":
			driveAfterBool = true;
			break;
		case "stop":
			driveAfterBool = false;
			break;
		case "shoot":
			shootToggle = true;
			break;
		}

		switch (autonSelected) { /* Green text is the mode, */ /** blue text is the optional addon **/
		case "Shoot": /* "Shoot" mode, drives forward, shoots, */ /** turns toward loading station, drives **/
			myRobot.arcadeDrive(0.5, 0);
			Timer.delay(2);
			myRobot.arcadeDrive(0, 0);
			shooter(true);
			Timer.delay(5);
			shooter(false);
			if (driveAfterBool == true) {
				myRobot.arcadeDrive(-.5, 0);
				Timer.delay(2);
				myRobot.arcadeDrive(0, 1);
				Timer.delay(1);
				myRobot.arcadeDrive(1, 0);
				Timer.delay(2);
				myRobot.arcadeDrive(0, 0);
			}
			else {
				myRobot.arcadeDrive(0, 0); }
			break;
		case "LeftGear": /* "LeftGear" mode, drives forward, turns toward gear loader, places gear, backs up, */ /** turns and drives toward loading station**/
			myRobot.arcadeDrive(0.5, 0);
			Timer.delay(3.0);
			myRobot.arcadeDrive(0, .6);
			Timer.delay(1.2);
			myRobot.arcadeDrive(0.4, 0);
			Timer.delay(2.1);
			myRobot.arcadeDrive(0, 0);
			Timer.delay(2);
			if (driveAfterBool == true) {
				myRobot.arcadeDrive(0, 1);
				Timer.delay(1);
				myRobot.arcadeDrive(1, 0);
				Timer.delay(2);
				myRobot.arcadeDrive(0, 0);
			}
			else {
				myRobot.arcadeDrive(0, 0); }
			break;
		case "RightGear": /* "RightGear" mode, drives forward, turns toward gear loader, places gear, backs up, */ /** turns and drives toward loading station**/
			myRobot.arcadeDrive(0.5, 0);
			Timer.delay(2);
			myRobot.arcadeDrive(0, -1);
			Timer.delay(1);
			myRobot.arcadeDrive(0.4, 0);
			Timer.delay(1);
			myRobot.arcadeDrive(0, 0);
			Timer.delay(1);
			myRobot.arcadeDrive(-0.4, 0);
			Timer.delay(1);
			myRobot.arcadeDrive(0, 0);
			if (driveAfterBool == true) {
				myRobot.arcadeDrive(0, 1);
				Timer.delay(1);
				myRobot.arcadeDrive(1, 0); }
			else {
				myRobot.arcadeDrive(0, 0); }
			break;
		case "MiddleGear": /* drives forward, places gear, backs up, */ /** drives to boiler and shoots **/
			myRobot.arcadeDrive(0.6, 0);
			Timer.delay(3);
			myRobot.arcadeDrive(0, 0);
			if (shootToggle == true) {
			myRobot.arcadeDrive(-.5, 0);
			Timer.delay(2);
			myRobot.arcadeDrive(0, 1);
			Timer.delay(1);
			myRobot.arcadeDrive(0, 0);
			myRobot.arcadeDrive(.5, 0);
			Timer.delay(2);
			myRobot.arcadeDrive(0, 0);
			myRobot.arcadeDrive(0, 1);
			myRobot.arcadeDrive(0, 0);
			myRobot.arcadeDrive(0.5, 0);
			Timer.delay(3);
			myRobot.arcadeDrive(0, 0);
			shooter(true);
			Timer.delay(5);
			shooter(false); }
			else {
				myRobot.arcadeDrive(0, 0); }
			break;
		case "doNothing": // "doNothing" mode, does nothing
		default: // this means that "doNothing" mode is the default mode
			myRobot.arcadeDrive(0, 0);
			break;
		}
	}

/**	public void agitator(boolean spinForward) { // Agitator Code
		if (spinForward) {
			agitatorMotor.set(0.50); // Agitator spin speed.
		} else {
			agitatorMotor.set(0); // Does nothing if no value is received.
		}
	} **/
	
	public void ballcollector(boolean takeIn, boolean pushOut) { // Ball Collector Code
		if (takeIn) {
			pickupMotor.set(1); // Pickup motor forward spin speed.
		} else if (pushOut) {
			pickupMotor.set(-1); // Pickup motor backward spin speed.
		} else {
			pickupMotor.set(0); // Does nothing if no value is received.
		}
	}

	public void shooter(boolean shoot) { // Shooter Code
		if (shoot) {
			shooterMotor.set(0.9); // Shooter spin speed.
		} else {
			shooterMotor.set(0); // Does nothing if no value is received.
		}
	}

	public void climber(boolean climbUp) { // Climber Code
		if (climbUp) {
			climbMotor.set(-1); // Climber spin speed.
		} else {
			climbMotor.set(0); // Does nothing if no value is received.
		}
	}

	public void SmartDashboardStuff() { // Some stuff that gets put on the Smart Dashboard
		xAxis = accelerometer.getX(); // gets x, y, and z values from built in accelerometer and pushes data to the smart dash board
		yAxis = accelerometer.getY();
		zAxis = accelerometer.getZ();
		SmartDashboard.putNumber("x", xAxis);
		SmartDashboard.putNumber("y", yAxis);
		SmartDashboard.putNumber("z", zAxis);
		
//		SmartDashboard.putNumber("Gyro", gyro.getAngle());

		shooterEnc = Enc.getRate();
		SmartDashboard.putNumber("Shooter Speed", shooterEnc);
	}

}