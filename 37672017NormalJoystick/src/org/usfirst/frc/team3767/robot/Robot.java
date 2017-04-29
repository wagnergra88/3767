package org.usfirst.frc.team3767.robot; //3767 package

import edu.wpi.first.wpilibj.SampleRobot; //importing all the files we need for the program
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;

@SuppressWarnings("unused")
public class Robot extends SampleRobot {
	RobotDrive myRobot;
	Joystick StickRight;
	Joystick StickLeft;
	//	*MotorController* *MotorName* = new *MotorController(roboRIO Input)
	Talon agitatorMotor = new Talon(5); // ball agitator 5
	Talon pickupMotor = new Talon (4); // ball pickup thing 6
	Talon shooterMotor = new Talon (2); // shooter 7
	Talon climbMotor = new Talon (3); // climber 8
	Timer timer;
	//	Autonomous Stuffs
	SendableChooser<String> AutonSwitch;
	SendableChooser<String> DelaySwitch;
	SendableChooser<String> driveAfterSwitch;
	
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	
	BuiltInAccelerometer accelerometer;
	boolean driveToggle;
	boolean driveAfterBool = false;
	double shooterEnc;
	double slowDrive;
	double xAxis;
	double yAxis;
	double zAxis;

	Encoder Enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);

	public Robot() { // This is where everything is declared
		myRobot = new RobotDrive(9, 8, 6, 7); // FL(1), BL(2), FR(3), BR(4), inputs on the roboRIO
		myRobot.setExpiration(0.1);
		StickRight = new Joystick(0);
		StickLeft = new Joystick(2);
		myRobot.setMaxOutput(1);
		timer = new Timer();
		accelerometer = new BuiltInAccelerometer();

	}

	@Override
	public void robotInit() { // Initialization Stuff
		new Thread(() -> {
			UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture();
			camera1.setResolution(640, 480);

			CvSink cvSink1 = CameraServer.getInstance().getVideo();
			CvSource outputStream1 = CameraServer.getInstance().putVideo("Blur1", 640, 480);

			Mat source1 = new Mat();
			Mat output1 = new Mat();

			while(!Thread.interrupted()) {
				cvSink1.grabFrame(source1);
				Imgproc.cvtColor(source1, output1, Imgproc.COLOR_BGR2GRAY);
				outputStream1.putFrame(output1);
			}
		}).start();

		new Thread(() -> {
			UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture();
			camera2.setResolution(640, 480);

			CvSink cvSink2 = CameraServer.getInstance().getVideo();
			CvSource outputStream2 = CameraServer.getInstance().putVideo("Blur2", 640, 480);

			Mat source2 = new Mat();
			Mat output2 = new Mat();

			while(!Thread.interrupted()) {
				cvSink2.grabFrame(source2);
				Imgproc.cvtColor(source2, output2, Imgproc.COLOR_BGR2GRAY);
				outputStream2.putFrame(output2);
			}
		}).start();

		driveToggle = false;

		AutonSwitch = new SendableChooser<>(); // Auto Stuff
		AutonSwitch.addObject("MiddleGear", "MiddleGear");
		AutonSwitch.addDefault("Normal", "Normal");
		AutonSwitch.addObject("doNothing", "doNothing");
		SmartDashboard.putData("AutonomousModes", AutonSwitch);
		DelaySwitch = new SendableChooser<>();
		DelaySwitch.addObject("5s", "5s");
		DelaySwitch.addObject("2.5s", "2.5s");
		DelaySwitch.addDefault("0s", "0s");
		SmartDashboard.putData("DelayTimes", DelaySwitch);
		driveAfterSwitch = new SendableChooser<>();
		driveAfterSwitch.addObject("Drive Away", "driveAway");
		driveAfterSwitch.addDefault("Stop", "stop");
		SmartDashboard.putData("AutonEnd", driveAfterSwitch);

	}

	@Override
	public void operatorControl() { // Tele-Op mode
		myRobot.setSafetyEnabled(false); // Declaring stuff
		SmartDashboardStuff(); // Calls the SmartDashboardStuff
		while (isOperatorControl() && isEnabled()) {
			while (isOperatorControl() && isEnabled() && driveToggle == false) { // Tank mode
				myRobot.tankDrive(-1 * StickRight.getRawAxis(1), -1 * StickLeft.getRawAxis(1)); // Speed of robot (-1 / 100%)
				ballcollector(StickRight.getRawButton(2), StickRight.getRawButton(3));
				shooter(StickRight.getRawButton(1));
				climber(StickLeft.getRawButton(2));
				SmartDashboardStuff(); // Calls the SmartDashboardStuff
				while (StickLeft.getRawButton(1)) { // Slow button
					myRobot.tankDrive(-0.5 * StickRight.getRawAxis(1), -0.5 * StickLeft.getRawAxis(1)); // Reduces robot speed for maneuvering (-.5/50%)
					ballcollector(StickRight.getRawButton(2), StickRight.getRawButton(3));
					shooter(StickRight.getRawButton(1));
					climber(StickLeft.getRawButton(2));
					SmartDashboardStuff(); // Calls the SmartDashboardStuff
				}
				if (StickRight.getRawButton(7) == true) {
					driveToggle = true; // Tank/arcade toggle
					Timer.delay(0.25);
				}
				Timer.delay(0.005);
			}
			while (isOperatorControl() && isEnabled() && driveToggle == true) { // Left stick arcade
				myRobot.arcadeDrive(-1 * StickRight.getRawAxis(1), -1 * StickRight.getRawAxis(0)); // Speed of robot and joysticks (-1 dft speed)
				ballcollector(StickRight.getRawButton(6), StickRight.getRawButton(5));
				shooter(StickRight.getRawButton(6));
				climber(StickLeft.getRawButton(6));
				SmartDashboardStuff(); // Calls the SmartDashboardStuff
				while (StickLeft.getRawAxis(2) > .1) { // Slow button
					myRobot.arcadeDrive(-0.5 * StickRight.getRawAxis(1), -0.5 * StickRight.getRawAxis(0));
					ballcollector(StickRight.getRawButton(6), StickRight.getRawButton(5));
					shooter(StickRight.getRawButton(6));
					climber(StickLeft.getRawButton(6));
					SmartDashboardStuff(); // calls the SmartDashboardStuff
				}
				if (StickRight.getRawButton(8) == true) {
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
		}

		switch (autonSelected) {
		case "MiddleGear": // "MiddleGear" mode, puts arm down, then drives for 3 seconds
			myRobot.arcadeDrive(0.5, 0);
			Timer.delay(2);
			myRobot.arcadeDrive(0, 0);
			break;
		case "Normal": // Drive Forward and Shoot
			myRobot.arcadeDrive(.85, 0); 
			shooter(true);
			Timer.delay(2);
			myRobot.arcadeDrive(0, 0);
			agitator(true, false);
			if (driveAfterBool == true) {
				myRobot.arcadeDrive(-.85, 0);
				Timer.delay(2);
				myRobot.arcadeDrive(.5, 50);
				Timer.delay(2);
				myRobot.arcadeDrive(1, 0);
				Timer.delay(7);
				myRobot.arcadeDrive(0, 0); }
			else {
				myRobot.arcadeDrive(0, 0);
			}
			break; 
		case "doNothing": // "doNothing" mode, does nothing
		default: // this means that "doNothing" mode is the default mode
			myRobot.arcadeDrive(0, 0);
			break;
		}
	}

	public void agitator(boolean spinForward, boolean spinBackward) { // agitator code
		if (spinForward) {
			agitatorMotor.set(0.20); // agitator forward spin speed
		} else if (spinBackward) {
			agitatorMotor.set(-0.20); // agitator backward spin speed
		} else {
			agitatorMotor.set(0); // does nothing if no value is received
		}
	}

	public void ballcollector(boolean takeIn, boolean pushOut) { // agitator code
		if (takeIn) {
			pickupMotor.set(0.20); // agitator forward spin speed
		} else if (pushOut) {
			pickupMotor.set(-0.20); // agitator backward spin speed
		} else {
			pickupMotor.set(0); // does nothing if no value is received
		}
	}

	public void shooter(boolean shoot) { // agitator code
		if (shoot) {
			/*			while (Enc.getRate() > 3) {
			shooterMotor.set(1); } // agitator forward spin speed */
			shooterMotor.set(1); // agitator forward spin speed
		} else {
			shooterMotor.set(0); // does nothing if no value is received
		}
	}

	public void climber(boolean climbUp) { // agitator code
		if (climbUp) {
			climbMotor.set(0.20); // agitator forward spin speed
		} else {
			climbMotor.set(0); // does nothing if no value is received
		}
	}

	public void SmartDashboardStuff() { // Some stuff that gets put on the Smart Dashboard
		xAxis = accelerometer.getX(); // gets x, y, and z values from built in accelerometer and pushes data to the smart dash board
		yAxis = accelerometer.getY();
		zAxis = accelerometer.getZ();
		SmartDashboard.putNumber("x", xAxis);
		SmartDashboard.putNumber("y", yAxis);
		SmartDashboard.putNumber("z", zAxis);
		
		SmartDashboard.putNumber("Gyro", gyro.getAngle());
		
		shooterEnc = Enc.getRate();
		SmartDashboard.putNumber("Shooter Speed", shooterEnc);
	}

}