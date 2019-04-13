/**********************/
/** Code written by: **/
/**   Liam Coyle &   **/
/**  Grayson Wagner  **/
/**    from team:    **/
/**    3767 TITAN    **/
/**********************/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Drive;
import frc.robot.Manipulator;

//@SuppressWarnings("unused")
public class Robot extends TimedRobot {
	Joystick stick;
	Joystick stick2;
	Drive move;
	Manipulator manip;
	double ljy;
	double rjy;
	double ljx;
	double rjx;
	boolean pos1;
	boolean pos2;
	boolean pos3;
	boolean pos4;
	boolean pos5;
	boolean pos6;
	boolean pos7;
	Compressor c;
	SendableChooser<String> delaySwitch;
	SendableChooser<String> autoSwitch;
	public Robot() {
		move = new Drive(0, 1, 2, 3);
		manip = new Manipulator(4, 5, 6, 7);
		stick = new Joystick(5);
		stick2 = new Joystick(4);
		c = new Compressor(0);
	}

	@Override
	public void robotInit() {
		manip.enc.reset();
		c.setClosedLoopControl(true);
		new Thread(() -> { // Code for cameras.
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(); // Creates string "camera1", and starts it.
			camera.setResolution(200, 300); // Sets "camera"'s resolution
		}).start(); // Starts it.

		delaySwitch = new SendableChooser<>();
		delaySwitch.setDefaultOption("0", "0");
		delaySwitch.addOption("2.5", "2.5");
		delaySwitch.addOption("5", "5");
		delaySwitch.addOption("7.5", "7.5");
		delaySwitch.addOption("10", "10");
		SmartDashboard.putData("Delay", delaySwitch);
		autoSwitch = new SendableChooser<>();
		autoSwitch.setDefaultOption("doNothing", "doNothing");
		autoSwitch.addOption("autoOne", "autoOne");
		autoSwitch.addOption("autoTwo", "autoTwo");
		autoSwitch.addOption("autoThree", "autoThree");
		SmartDashboard.putData("Auto Selection", autoSwitch);
	}

	public void autonomousPeriodic() {
		manip.enc.reset();
		while (isEnabled()) {
			if (stick.getRawButton(6) == true) {
				ljy = (0.6 * stick.getRawAxis(1));
				rjy = (0.6 * stick.getRawAxis(5));
				ljx = (0.6 * stick.getRawAxis(0));
				rjx = (0.6 * stick.getRawAxis(4));
			} else {
				ljy = (stick.getRawAxis(1));
				rjy = (stick.getRawAxis(5));
				ljx = (stick.getRawAxis(0));
				rjx = (stick.getRawAxis(4));
			}
			if (stick2.getRawButton(5) == false) {
				if (stick2.getPOV() == 180) {
					
					manip.liftPos(true, false, false, false, false, false, false, false);
				} else if (stick2.getPOV() == 90) {
					
					manip.liftPos(false, true, false, false, false, false, false, false);
				} else if (stick2.getPOV() == 0) {
					
					manip.liftPos(false, false, true, false, false, false, false, false);
				} else if (stick2.getPOV() == 270) {
					
					manip.liftPos(false, false, false, false, false, false, false, true);
				}
			}
			if (stick2.getRawButton(5)) {
				if (stick2.getPOV() == 180) {
					
					manip.liftPos(false, false, false, true, false, false, false, false);
				} else if (stick2.getPOV() == 90) {
					
					manip.liftPos(false, false, false, false, true, false, false, false);
				} else if (stick2.getPOV() == 0) {
					
					manip.liftPos(false, false, false, false, false, true, false, false);
				} else if (stick2.getPOV() == 270) {
					
					manip.liftPos(false, false, false, false, false, false, true, false);
				}
			}

			move.mecanumDrive(-ljy, -ljx, -rjx);
			//move.cartDrive(-ljy, -ljx, -rjx);

			manip.lift(stick2.getRawAxis(3), stick2.getRawAxis(2));
			manip.grab(stick2.getRawButton(1), stick2.getRawButton(2), stick2.getRawButton(3), stick2.getRawButton(4), stick2.getRawButton(8));
			manip.grap(stick2.getRawButton(6));
			manip.fly(stick.getRawButton(1), stick.getRawButton(2));
			manip.clap(stick.getRawButton(4));
			manip.clap(stick2.getRawButton(7));
			Timer.delay(0.005); // wait for a motor update time

			SmartDashboard.putNumber("Gyro", move.gyro.getAngle());
			SmartDashboard.putNumber("Lift Encoder", manip.enc.getRaw());
			SmartDashboard.putNumber("error", Math.cbrt(Math.abs(manip.error/10000)));
			SmartDashboard.putNumber("POV", stick2.getPOV());
			SmartDashboard.putBoolean("Stall", manip.armStallbool);
			SmartDashboard.putBoolean("Pneumatics", manip.squeeze);
		}
	}

	public void teleopPeriodic() {
		while (isEnabled()) {
			if (stick.getRawButton(6) == true) {
				ljy = (0.6 * stick.getRawAxis(1));
				rjy = (0.6 * stick.getRawAxis(5));
				ljx = (0.6 * stick.getRawAxis(0));
				rjx = (0.6 * stick.getRawAxis(4));
			} else {
				ljy = (stick.getRawAxis(1));
				rjy = (stick.getRawAxis(5));
				ljx = (stick.getRawAxis(0));
				rjx = (stick.getRawAxis(4));
			}
			if (stick2.getRawButton(5) == false) {
				if (stick2.getPOV() == 180) {
					
					manip.liftPos(true, false, false, false, false, false, false, false);
				} else if (stick2.getPOV() == 90) {
					
					manip.liftPos(false, true, false, false, false, false, false, false);
				} else if (stick2.getPOV() == 0) {
					
					manip.liftPos(false, false, true, false, false, false, false, false);
				} else if (stick2.getPOV() == 270) {
					
					manip.liftPos(false, false, false, false, false, false, false, true);
				}
			}
			if (stick2.getRawButton(5)) {
				if (stick2.getPOV() == 180) {
					
					manip.liftPos(false, false, false, true, false, false, false, false);
				} else if (stick2.getPOV() == 90) {
					
					manip.liftPos(false, false, false, false, true, false, false, false);
				} else if (stick2.getPOV() == 0) {
					
					manip.liftPos(false, false, false, false, false, true, false, false);
				} else if (stick2.getPOV() == 270) {
					
					manip.liftPos(false, false, false, false, false, false, true, false);
				}
			}

			move.mecanumDrive(-ljy, -ljx, -rjx);
			//move.cartDrive(-ljy, -ljx, -rjx);

			manip.lift(stick2.getRawAxis(3), stick2.getRawAxis(2));
			manip.grab(stick2.getRawButton(1), stick2.getRawButton(2), stick2.getRawButton(3), stick2.getRawButton(4), stick2.getRawButton(8));
			manip.grap(stick2.getRawButton(6));
			manip.fly(stick.getRawButton(1), stick.getRawButton(2));
			manip.clap(stick.getRawButton(4));
			manip.clap(stick2.getRawButton(7));
			Timer.delay(0.005); // wait for a motor update time

			SmartDashboard.putNumber("Gyro", move.gyro.getAngle());
			SmartDashboard.putNumber("Lift Encoder", manip.enc.getRaw());
			SmartDashboard.putNumber("error", Math.cbrt(Math.abs(manip.error/10000)));
			SmartDashboard.putNumber("POV", stick2.getPOV());
			SmartDashboard.putBoolean("Stall", manip.armStallbool);
			SmartDashboard.putBoolean("Pneumatics", manip.squeeze);
		}
	}
}