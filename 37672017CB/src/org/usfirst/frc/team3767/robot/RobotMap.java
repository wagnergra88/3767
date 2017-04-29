package org.usfirst.frc.team3767.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.VictorSP;

public class RobotMap {
	VictorSP FLM = new VictorSP (1); // Front Left Motor
	VictorSP RLM = new VictorSP (2); // Rear Left Motor
	VictorSP FRM = new VictorSP (3); // Front Right Motor
	VictorSP RRM = new VictorSP (4); // Rear Right Motor
	VictorSP agiM = new VictorSP (5); // Ball Agitator
	VictorSP pickupM = new VictorSP (6); // Ball Pickup Thing
	VictorSP shooterM = new VictorSP (7); // Shooter
	VictorSP climbM = new VictorSP (8); // Climber
	ADXRS450_Gyro gyro = new ADXRS450_Gyro(); //Creates new ADXRS450 Gyroscope Object called gyro
}//