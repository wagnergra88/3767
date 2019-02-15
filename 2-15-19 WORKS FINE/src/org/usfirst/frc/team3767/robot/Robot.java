/**********************/
/** Code written by: **/
/**   Liam Coyle &   **/
/**  Grayson Wagner  **/
/**    from team:    **/
/**    3767 TITAN    **/
/**********************/

package org.usfirst.frc.team3767.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team3767.robot.Drive;
import org.usfirst.frc.team3767.robot.Manipulator;
import org.usfirst.frc.team3767.robot.Simple;

//@SuppressWarnings("unused")
public class Robot extends Simple {
	Joystick stick;
	Joystick stick2;
	Joystick buttonbox;
	Drive move;
	Auto auto;
	Manipulator manip;
	double ljy;
	double rjy;
	double ljx;
	double rjx;
	Compressor c;
	SendableChooser<String> delaySwitch;
	SendableChooser<String> autoSwitch;
	String gameData;
	public Robot() {
		move = new Drive(0, 1, 2, 3);
		manip = new Manipulator(4, 5, 6);
		stick = new Joystick(5);
		stick2 = new Joystick(4);
		buttonbox = new Joystick(3);
		c = new Compressor(0);
	}

	@Override
	public void robotInit() {
		//manip.enc.reset();
		c.setClosedLoopControl(true);
		new Thread(() -> { // Code for cameras.
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(); // Creates string "camera1", and starts it.
			camera.setResolution(240, 320); // Sets "camera"'s resolution
			camera.setFPS(20);
		}).start(); // Starts it.

		delaySwitch = new SendableChooser<>();
		delaySwitch.addDefault("0", "0");
		delaySwitch.addObject("2.5", "2.5");
		delaySwitch.addObject("5", "5");
		delaySwitch.addObject("7.5", "7.5");
		delaySwitch.addObject("10", "10");
		SmartDashboard.putData("Delay", delaySwitch);
		autoSwitch = new SendableChooser<>();
		autoSwitch.addObject("doNothing", "doNothing");
		autoSwitch.addDefault("autoOne", "autoOne");
		autoSwitch.addObject("autoTwo", "autoTwo");
		autoSwitch.addObject("autoThree", "autoThree");
		SmartDashboard.putData("Auto Selection", autoSwitch);
	}

	public void autonomous() {
		auto.autonomous();
	}

	@Override
	public void operatorControl() {
		while (isOperatorControl() && isEnabled()) {
			if (stick.getRawButton(5) == true) {
				ljy = (0.5 * stick.getRawAxis(1));
				rjy = (0.5 * stick.getRawAxis(5));
				ljx = (0.5 * stick.getRawAxis(0));
				rjx = (0.5 * stick.getRawAxis(4));
			} else {
				ljy = (stick.getRawAxis(1));
				rjy = (stick.getRawAxis(5));
				ljx = (stick.getRawAxis(0));
				rjx = (stick.getRawAxis(4));
			}
			//manip.liftPos(buttonbox.getRawButton(1), buttonbox.getRawButton(2), buttonbox.getRawButton(3), buttonbox.getRawButton(4), buttonbox.getRawButton(5), buttonbox.getRawButton(6), buttonbox.getRawButton(7));
//			move.dualDrive(-ljy, -rjy, -ljx, stick.getRawButton(8));
			move.mecanumDrive(-ljy, -ljx, -rjx);
//			move.cartDrive(-ljy, -ljx, -rjx);
			if (stick.getRawButton(8) == true) {
				//manip.pid.setSetpoint(0.5);
			}
			manip.lift(stick.getRawAxis(3), stick.getRawAxis(2));
			manip.grab(stick2.getRawButton(1), stick2.getRawButton(2), stick2.getRawButton(3), stick2.getRawButton(4));
			manip.grap(stick2.getRawButton(6));
			Timer.delay(0.005); // wait for a motor update time

			SmartDashboard.putNumber("Gyro", move.gyro.getAngle());
			//SmartDashboard.putData("Lift Encoder", manip.enc);
		}
	}
	}