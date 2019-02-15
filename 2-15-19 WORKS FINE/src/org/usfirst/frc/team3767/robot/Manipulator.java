/**********************/
/** Code written by: **/
/**   Liam Coyle &   **/
/**  Grayson Wagner  **/
/**    from team:    **/
/**    3767 TITAN    **/
/**********************/

package org.usfirst.frc.team3767.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.DigitalInput;

@SuppressWarnings("unused")

  public class Manipulator {
	DoubleSolenoid grasp;
/**    Encoder enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	int count = enc.get();
	double distance = enc.getRaw();
	double rate = enc.getRate();
	boolean direction = enc.getDirection();
	boolean stopped = enc.getStopped(); **/
	DigitalInput limitSwitchUp;
	DigitalInput limitSwitchDown;
	VictorSP gbm;
	VictorSP upm;
	VictorSP armm;
	boolean squeeze;
//	PIDController pid;
	double yP;
	double p = 0.0;
	double i = 0.0;
	double d = 0.0;
	double f = 0.0; 

		public Manipulator(final int gbmP, final int upmP, final int armmP) {
/**		enc.setMaxPeriod(.1);
		enc.setMinRate(10);
		enc.setDistancePerPulse(5);
		enc.setReverseDirection(true);
		enc.setSamplesToAverage(7);
		enc.reset(); 
		
	    pid = new PIDController(p, i, d, f, enc, upm, 0);
		pid.setInputRange(-1000,  1000);
	    pid.setOutputRange(-1.0, 1.0); **/
		limitSwitchUp = new DigitalInput(0);
		limitSwitchDown = new DigitalInput(1);
		grasp = new DoubleSolenoid(0, 1);
		gbm = new VictorSP(gbmP);
		upm = new VictorSP(upmP);
     	armm = new VictorSP(armmP);
		squeeze = false;
//		enc = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	}

/**	public void grap(boolean clutch, boolean release) {
		if (clutch) {
			grasp.set(Value.kForward);
		} else if (release) {
			grasp.set(Value.kReverse);
		} else {
			return;
		}
	} **/

	public void grap(boolean toggle) {
		if (toggle) {
			squeeze = !squeeze;
			Timer.delay(0.25);
		} if (squeeze) {
			grasp.set(Value.kForward);
		} else if (squeeze == false) {
			grasp.set(Value.kReverse);
		} else {
			
		}
	}

	public void grab(boolean grabIn, boolean grabOut, boolean armOpen, boolean armClose) {
		if (grabIn) {
			gbm.set(0.5);
		} else if (grabOut) {
			gbm.set(-0.5);
		} else {
			gbm.set(0);
		}
		if (armOpen) {
			armm.set(0.5);
		} else if (armClose) {
			armm.set(-0.5);
		} else {
			armm.set(0);
		}
	}

	public void lift(double liftUp, double liftDown) {
		if (liftUp > 0.05 && liftDown < 0.05 && limitSwitchUp.get() == false) {
			upm.set(1 * liftUp);
		} else if (liftDown > 0.05 && liftUp < 0.05 && limitSwitchDown.get() == false) {
			upm.set(-1 * liftDown);
		} else {
			upm.set(0);
		}
	}

	public void liftPos(boolean pos1, boolean pos2, boolean pos3, boolean pos4, boolean pos5, boolean pos6, boolean pos7) {
		if (pos1) {
			//pid1
		} else if (pos2) {
			//pid2
		} else if (pos3) {
			//pid3
		} else if (pos4) {
			//pid4
		} else if (pos5) {
			//pid5
		} else if (pos6) { 
			//pid6
		} else if (pos7) {
			//pid7
		} else {
			upm.set(0);
		}
	} 
} 