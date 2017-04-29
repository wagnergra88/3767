package org.usfirst.frc.team3767.robot; // This is the 3767 package.
							// Here is where all the files we use in the code are imported from wpilib and opencv

import edu.wpi.first.wpilibj.SampleRobot; // "SampleRobot", the SampleRobot class.
import edu.wpi.first.wpilibj.RobotDrive; // "RobotDrive", the RobotDrive class.
import edu.wpi.first.wpilibj.SPI; // "SPI", used to reference the SPI port on the roboRIO.
import edu.wpi.first.wpilibj.Joystick; // "Joystick", used when assigning joysticks.
import edu.wpi.first.wpilibj.Timer; // "Timer", used to create a delay in the code.
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser; // "SendableChooser", a type of variable used in the code.
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; // "SmartDashboard", used when pushing/pulling data to/from the SmartDashboard.
import edu.wpi.first.wpilibj.VictorSP; // "VictorSP", used to reference the motor controller used on our robot.
import edu.wpi.cscore.UsbCamera; // "UsbCamera", the USB camera code we use.
import edu.wpi.first.wpilibj.ADXRS450_Gyro; // "ADXRS450_Gyro", the gyroscope we use.
import edu.wpi.first.wpilibj.BuiltInAccelerometer; // "BuiltInAccelerometer", used to reference the accelerometer that is built into the roboRIO.
import edu.wpi.first.wpilibj.CameraServer; // "CameraServer", used in camera code.
import edu.wpi.first.wpilibj.Encoder; // "Encoder", used to reference Encoders.
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.usfirst.frc.team3767.robot.GripPipeline;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

@SuppressWarnings("unused") // Suppresses warnings that pop up due to something that is unused, used to make code cleaner.

public class Robot extends SampleRobot { // The "Robot" class, where variables are assigned.
	DigitalInput gearDetector;
	RobotDrive myRobot; // Creates new RobotDrive variable "myRobot".
	Joystick Stick1; // Creates new Joystick variable "Stick1".
	Joystick Stick2; // Creates new Joystick variable "stick2".
	//	"*MotorControllerType* *MotorName* = new *MotorControllerType(roboRIO Input);" (Example motor controller setup).
	VictorSP agitatorMotor = new VictorSP(5); // Ball agitator motor, on port 5.
	VictorSP pickupMotor = new VictorSP (6); // Ball pickup motor, on port 6.
	//VictorSP shooterMotor = new VictorSP (7); // Shooter motor, on port 7.
	CANTalon shooterMotor = new CANTalon (0);
	VictorSP climbMotor = new VictorSP (8); // Climb motor, on port 8.
//	VictorSP gearIndicator = new VictorSP (9); // LED thang on port 9.
	Timer timer; // Creates new Timer variable "timer".
	NetworkTable table;
	//	All of the autonomous SendableChoosers that show up on the SmartDashboard.
	SendableChooser<String> AutonSwitch; // Creates new SendableChooser variable "AutonSwitch".
	SendableChooser<String> DelaySwitch; // Creates new SendableChooser variable "DelaySwitch".
	SendableChooser<String> driveAfterSwitch; // Creates new SendableChooser variable "driveAfterSwitch".
	SendableChooser<String> DriveSettings;
	
	public VisionThread vThread;

//	ADXRS450_Gyro gyro = new ADXRS450_Gyro(); // Creates new ADXRS450_Gyro variable "gyro".

	BuiltInAccelerometer accelerometer; // Creates new BuiltInAccelerometer variable "accelerometer".
	boolean driveToggle = false; // Creates new boolean variable "driveToggle".
	boolean shootToggle = false; // 2/18
	boolean normalDrive = true;
	boolean grain = false;
	boolean driveAfterBool = false; // Creates new boolean variable "driveAfterBool", and sets it to false.
	double slowDrive; // Creates new double variable "slowDrive".
	double xAxis; // Creates new double variable "xAxis".
	double yAxis; // Creates new double variable "yAxis".
	double zAxis;
	double driveAdjuster = 0.25;
		double centerx = 0.0;
		final Object img = new Object();
	public Robot() { // Public "Robot()", where different things are declared.
		myRobot = new RobotDrive(1, 2, 3, 4); // FL(1), BL(2), FR(3), BR(4), drive motor inputs on the roboRIO.
		myRobot.setExpiration(0.1); // Sets the expiration on myRobot to "0.1".
		Stick1 = new Joystick(1); // Sets joystick "Stick" to port 1.
		Stick2 = new Joystick(2); // Sets joystick "Stick2" to port 2
		myRobot.setMaxOutput(1); // Sets the max output of myRobot to "1".
		timer = new Timer(); // Sets variable "timer" to a Timer.
		accelerometer = new BuiltInAccelerometer(); // Sets "accelerometer" to the BuiltInAccelerometer.
		gearDetector = new DigitalInput(0);

	}

	@Override
	public void robotInit() { // Initialization Stuff
		new Thread(() -> { // Code for cameras.
			UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(); // Creates string "camera1", and starts it.
			camera1.setResolution(320, 240); // Sets "camera1"'s resolution to 140x140.
			UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture(); // Creates string "camera2", and starts it.
			camera2.setResolution(320, 240); // Sets "camera2"'s resolution to 140x140.
		}).start(); // Starts it.
//		double[] centerX;
//		table = NetworkTable.getTable("myContoursReport");
//		double[] CenterX = table.getNumberArray("centerX", centerX);
//		SmartDashboard.putNumber("table", centerX);

		driveToggle = false; // Sets variable driveToggle to false.
		
		DriveSettings = new SendableChooser<>();
		DriveSettings.addDefault("NormalDrive", "NormalDrive");//not grain
		DriveSettings.addObject("Grain", "Grain");//fine-grained drive
		SmartDashboard.putData("Drive Setting", DriveSettings);
		
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
		
		
		shooterMotor.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		shooterMotor.reverseSensor(false);
		shooterMotor.configNominalOutputVoltage(+0.0f, -0.0f);
		shooterMotor.configPeakOutputVoltage(+12.0f, -12.0f);
		shooterMotor.setProfile(0);
		shooterMotor.setF(0);
		shooterMotor.setP(0);
		shooterMotor.setI(0);
		shooterMotor.setD(0);
	}

	@Override
	public void operatorControl() { // Tele-Op mode.
		myRobot.setSafetyEnabled(false); // Declaring stuff.
		SmartDashboardStuff(); // Calls the SmartDashboardStuff.
		
		String driveSelected = DriveSettings.getSelected();
		switch(driveSelected){
		case "normalDrive" :
			normalDrive = true;
			grain = false;
			break;
		case "grain" :
			normalDrive = false;
			grain = true;
			break;
		}
			
		while (isOperatorControl() && isEnabled()) { // While it is Tele-Op and the robot is enabled, do this:
			while (isOperatorControl() && isEnabled() && driveToggle == false) { // Tank mode.
//				double gearIndicatorButton = Stick1.getRawButton(3) ? 1:0.0;
				if(normalDrive){
				myRobot.tankDrive(-1 * Stick1.getRawAxis(1), -1 * Stick1.getRawAxis(5));
				}
				else{
				myRobot.tankDrive(-1 * Stick1.getRawAxis(1) * Math.abs(Stick1.getRawAxis(1)), 
						-1 * Stick1.getRawAxis(5) * Math.abs(Stick1.getRawAxis(5)));
				}
				climbMotor.set(1 * Stick2.getRawAxis(2));
//				gearIndicator.set(gearIndicatorButton);
				ballcollector(Stick2.getRawButton(6), Stick2.getRawButton(2)); // If code is blue, it means that it is drive code commented out for
				shooter(Stick2.getRawAxis(3) > 0.1); // one of the sticks. Stick1 can drive, use slow drive, and switch drive modes,
				SmartDashboardStuff(); // Calls the SmartDashboardStuff.
				if (Stick1.getRawButton(8) == true) { // If button 8 on Stick1 is pressed, do this:
					driveToggle = true; // Tank/arcade toggle variable.
					Timer.delay(0.25); // 0.25 second delay.
				}
				while (Stick1.getRawButton(5) == true) {
					myRobot.tankDrive(-.5 * Stick1.getRawAxis(1), -.5 * Stick1.getRawAxis(5)); // Speed of robot (-1/100%).
					climbMotor.set(1 * Stick2.getRawAxis(2));
//					gearIndicator.set(gearIndicatorButton);
					ballcollector(Stick2.getRawButton(6), Stick1.getRawButton(2)); // If code is blue, it means that it is drive code commented out for
					shooter(Stick2.getRawAxis(3) > 0.1); // one of the sticks. Stick1 can drive, use slow drive, and switch drive modes,
					SmartDashboardStuff(); // Calls the SmartDashboardStuff.
					if (Stick1.getRawButton(8) == true) { // If button 8 on Stick1 is pressed, do this:
						driveToggle = true; // Tank/arcade toggle variable.
						Timer.delay(0.25); // 0.25 second delay.
					}
				}
			}
			while (isOperatorControl() && isEnabled() && driveToggle == true) { // Left stick arcade.
//				double gearIndicatorButton = Stick1.getRawButton(3) ? 1:0.0;
				if(normalDrive){
					myRobot.arcadeDrive(-1 * Stick1.getRawAxis(1), -1 * Stick1.getRawAxis(0));
				}
				else{
					myRobot.arcadeDrive(-1 * Stick1.getRawAxis(1) * Math.abs(Stick1.getRawAxis(1)),
							-1 * Stick1.getRawAxis(0) * Math.abs(Stick1.getRawAxis(0)));
				}
				climbMotor.set(1 * Stick2.getRawAxis(2));
//				gearIndicator.set(gearIndicatorButton);
				ballcollector(Stick2.getRawButton(6), Stick2.getRawButton(2)); // If code is blue, it means that it is drive code commented out for
				shooter(Stick2.getRawAxis(3) > 0.1); // one of the sticks. Stick1 can drive, use slow drive, and switch drive modes,
				SmartDashboardStuff(); // Calls the SmartDashboardStuff.
				if (Stick1.getRawButton(8) == true) { // If button 8 on Stick1 is pressed, do this:
					driveToggle = false; // Tank/arcade toggle variable.
					Timer.delay(0.25); // 0.25 second delay.
				}
				while (Stick1.getRawButton(5) == true) {
					myRobot.arcadeDrive(-.5 * Stick1.getRawAxis(1), -.5 * Stick1.getRawAxis(0)); // Speed of robot (-1/100%).
					climbMotor.set(1 * Stick2.getRawAxis(2));
//					gearIndicator.set(gearIndicatorButton);
					ballcollector(Stick2.getRawButton(6), Stick1.getRawButton(2)); // If code is blue, it means that it is drive code commented out for
					shooter(Stick2.getRawAxis(3) > 0.1); // one of the sticks. Stick1 can drive, use slow drive, and switch drive modes,
					SmartDashboardStuff(); // Calls the SmartDashboardStuff.
					if (Stick1.getRawButton(8) == true) { // If button 8 on Stick1 is pressed, do this:
						driveToggle = false; // Tank/arcade toggle variable.
						Timer.delay(0.25); // 0.25 second delay.
					}
				}
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
			shootToggle = false;
			break;
		case "stop":
			driveAfterBool = false;
			shootToggle = false;
			break;
		case "shoot":
			shootToggle = true;
			driveAfterBool = false;
			break;
		}

		switch (autonSelected) { /* Green text is the mode, */ /** blue text is the optional addon **/
		case "Shoot": /* "Shoot" mode, drives forward, shoots, */ /** turns toward loading station, drives **/
			myRobot.arcadeDrive(1, driveAdjuster);
			Timer.delay(0.25);
			myRobot.arcadeDrive(-1, driveAdjuster);
			Timer.delay(0.25);
			myRobot.arcadeDrive(0.8, driveAdjuster);
			Timer.delay(1);
			myRobot.arcadeDrive(0, 1);
			Timer.delay(1);
			myRobot.arcadeDrive(1, driveAdjuster);
			Timer.delay(1);
			myRobot.arcadeDrive(0, 0);
			shooter(true);
			Timer.delay(3.5);
			shooter(false);
			if (driveAfterBool == true) {
				myRobot.arcadeDrive(-.5, driveAdjuster);
				Timer.delay(1);
				myRobot.arcadeDrive(0, -1);
				Timer.delay(1);
				myRobot.arcadeDrive(1, driveAdjuster);
				Timer.delay(2);
				myRobot.arcadeDrive(0, 0);
			}
			else {
				myRobot.arcadeDrive(0, 0); }
			break;
		case "LeftGear": /* "LeftGear" mode, drives forward, turns toward gear loader, places gear, backs up, */ /** turns and drives toward loading station**/
			myRobot.arcadeDrive(1, driveAdjuster);
			Timer.delay(0.25);
			myRobot.arcadeDrive(-1, driveAdjuster);
			Timer.delay(0.25);
			myRobot.arcadeDrive(0.6, driveAdjuster);
			Timer.delay(1.25);
			myRobot.arcadeDrive(0, -.75);
			Timer.delay(.3);
			myRobot.arcadeDrive(0.6, driveAdjuster);
			Timer.delay(2.8);
			myRobot.arcadeDrive(0, 0);
			while (gearDetector.get() == false); {
				myRobot.arcadeDrive(0, 0);
			}
			Timer.delay(1);
			if (driveAfterBool == true) {
				myRobot.arcadeDrive(-0.6, driveAdjuster);
				Timer.delay(1);
				myRobot.arcadeDrive(0, 1);
				Timer.delay(1);
				myRobot.arcadeDrive(1, driveAdjuster);
				Timer.delay(2);
				myRobot.arcadeDrive(0, 0);
			}
			else if (shootToggle == true) {
				myRobot.arcadeDrive(-0.6, driveAdjuster);
				Timer.delay(1);
				myRobot.arcadeDrive(0, 1);
				Timer.delay(1.5);
				myRobot.arcadeDrive(.7, driveAdjuster);
				Timer.delay(2);
				myRobot.arcadeDrive(0, -0.5);
				Timer.delay(.5);
				myRobot.arcadeDrive(.7, driveAdjuster);
				Timer.delay(1);
				myRobot.arcadeDrive(0, 0);
				shooter(true);
				Timer.delay(3.5);
				shooter(false);
			}
			else {
				myRobot.arcadeDrive(0, 0); }
			break;
		case "RightGear": /* "RightGear" mode, drives forward, turns toward gear loader, places gear, backs up, */ /** turns and drives toward loading station**/
			myRobot.arcadeDrive(1, driveAdjuster);
			Timer.delay(0.25);
			myRobot.arcadeDrive(-1, driveAdjuster);
			Timer.delay(0.25);
			myRobot.arcadeDrive(0.6, driveAdjuster);
			Timer.delay(1.43);
			myRobot.arcadeDrive(0, .90);
			Timer.delay(.33);
			myRobot.arcadeDrive(0.6, driveAdjuster + 0.05);
			Timer.delay(2.9);
			while (gearDetector.get() == false){
				myRobot.arcadeDrive(0, 0);
			}
			Timer.delay(1);
			if (driveAfterBool == true) {
				myRobot.arcadeDrive(-0.7, driveAdjuster);
				Timer.delay(1);
				myRobot.arcadeDrive(0, -.8);
				Timer.delay(.75);
				myRobot.arcadeDrive(.8, driveAdjuster);
				Timer.delay(2);
				myRobot.arcadeDrive(0, 0); }
			else {
				myRobot.arcadeDrive(0, 0); }
			break;
		case "MiddleGear": /* drives forward, places gear, backs up, */ /** drives to boiler and shoots **/
			myRobot.arcadeDrive(1, driveAdjuster);
			Timer.delay(0.25);
			myRobot.arcadeDrive(-1, driveAdjuster);
			Timer.delay(0.25);
			myRobot.arcadeDrive(0.6, driveAdjuster);
			Timer.delay(1.5);
			myRobot.arcadeDrive(0, 0);
			while (gearDetector.get() == false); {
				myRobot.arcadeDrive(0, 0);
			}
			Timer.delay(1);
			if (shootToggle == true) {
				myRobot.arcadeDrive(-0.7, driveAdjuster);
				Timer.delay(0.5);
				myRobot.arcadeDrive(0, 1);
				Timer.delay(.5);
				myRobot.arcadeDrive(1, driveAdjuster);
				Timer.delay(2);
				myRobot.arcadeDrive(0, .4);
				Timer.delay(.5);
				myRobot.arcadeDrive(1, driveAdjuster);
				Timer.delay(1);
				myRobot.arcadeDrive(0, 0);
				shooter(true);
				Timer.delay(3.5);
				shooter(false); }
			else if (driveAfterBool == true){
			myRobot.arcadeDrive(-.7, driveAdjuster);
			Timer.delay(.5);
			myRobot.arcadeDrive(0, -.7);
			Timer.delay(.5);
			myRobot.arcadeDrive(1, driveAdjuster);
			Timer.delay(2);
			myRobot.arcadeDrive(0, 0);
			}
			else {
				myRobot.arcadeDrive(0, 0); }
			break;
		case "doNothing": // "doNothing" mode, does nothing
		default: // this means that "doNothing" mode is the default mode
			myRobot.arcadeDrive(0, 0);
			break;
		}
	}
	
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
		//shooterMotor.changeControlMode(TalonControlMode.Speed);
		//shooterMotor.set(1400); 
		if (shoot) {
			shooterMotor.set(0.9);// Shooter spin speed.
			agitatorMotor.set(1);
		} else {
			shooterMotor.set(0); // Does nothing if no value is received.
			agitatorMotor.set(0);
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
		SmartDashboard.putBoolean("Limit", gearDetector.get());
//		SmartDashboard.putNumber("Gyro", gyro.getAngle());
	}

}