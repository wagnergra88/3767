/**********************/
/** Code written by: **/
/**   Liam Coyle &   **/
/**  Grayson Wagner  **/
/**    from team:    **/
/**    3767 TITAN    **/
/**********************/

package org.usfirst.frc.team3767.robot;

import org.usfirst.frc.team3767.robot.Drive;
import org.usfirst.frc.team3767.robot.Manipulator;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

//@SuppressWarnings("unused")
public class Auto {
	Manipulator manip;
	Drive move;
	SendableChooser<String> delaySwitch;
	SendableChooser<String> autoSwitch;
	String gameData;

	public void autonomous() {
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		DriverStation.getInstance().getGameSpecificMessage().charAt(0);
		switch (delaySwitch.getSelected()) {
		case "0":
			move.arcadedrive(0, 0);
			break;
		case "2.5":
			move.arcadedrive(0, 0);
			Timer.delay(2.25);
			break;
		case "5":
			move.arcadedrive(0, 0);
			Timer.delay(5);
			break;
		case "7.5":
			move.arcadedrive(0, 0);
			Timer.delay(7.5);
			break;
		case "10":
			move.arcadedrive(0, 0);
			Timer.delay(10);
			break;
		} switch (autoSwitch.getSelected()) {
		case "doNothing":
			move.arcadedrive(0, 0);
			break;
		case "autoOne":
			//code here
			break;
		case "autoTwo":
			//code here
			break;
		case "autoThree":
			//code here
			break;
		}
	}
}