/**********************/
/** Code written by: **/
/**   Liam Coyle &   **/
/**  Grayson Wagner  **/
/**    from team:    **/
/**    3767 TITAN    **/
/**********************/

package frc.robot;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.Robot;

//@SuppressWarnings("unused")
  public class Manipulator {
	Encoder enc;
	Robot robot;
	DoubleSolenoid grasp;
	DoubleSolenoid flyFront;
	DoubleSolenoid flyRear;
	VictorSP gbm;
	VictorSP upm;
	VictorSP armm1;
	VictorSP armm2;
	boolean squeeze;
	boolean squeeze1;
	boolean squeeze2;
	boolean armStallbool;
	boolean neg;
	double kP;
	double error;
	double error2;
	double threshold;
	double roc;
	double erroratstart;

	public Manipulator(final int gbmP, final int upmP, final int armm1P, final int armm2P) {
		enc = new Encoder(0, 1, false, EncodingType.k4X);
		enc.setMaxPeriod(.1);
		enc.setMinRate(10);
		enc.setDistancePerPulse(5);
		enc.setReverseDirection(false);
		enc.setSamplesToAverage(7);
		enc.reset();

		grasp = new DoubleSolenoid(0, 1);
		flyFront = new DoubleSolenoid(2, 3);
		flyRear = new DoubleSolenoid(4, 5);
		gbm = new VictorSP(gbmP);
		upm = new VictorSP(upmP);
		armm1 = new VictorSP(armm1P);
		armm2 = new VictorSP(armm2P);
		squeeze = false;
		squeeze1 = false;
		squeeze2 = false;
		armStallbool = false;
		kP = 1;
		threshold = 100;
		roc = -1000;
	}

	public void clap(boolean clap) {
		if (clap) {
			armStallbool = false;
			armm1.set(-0.5);
			armm2.set(-0.5);
			Timer.delay(0.05);
			armm1.set(0);
			armm2.set(0);
			Timer.delay(0.05);
		}
	}

	public void liftPID(double targetPos) {
		error = targetPos - enc.getRaw();
		if (error < 0) {
			neg = true;
		} else if (error > 0) {
			neg = false;
		} else {
			neg = false;
		}
		if (Math.abs(error) > threshold) {
			if (neg) {
				upm.set(Math.sqrt(Math.cbrt(Math.abs(error/10000))));
			} else {
				upm.set((-1 * Math.sqrt(Math.cbrt(Math.abs(error/10000)))) + 0.3);
			}
		} else {
		}
	}

	public void grap(boolean toggle) {
		if (toggle) {
			squeeze = !squeeze;
			Timer.delay(0.25);
		} if (squeeze) {
			grasp.set(Value.kForward);
		} else if (squeeze == false) {
			grasp.set(Value.kReverse);
		} else {}
	}

	public void fly(boolean toggleFront, boolean toggleRear) {
		if (toggleFront) {
			squeeze1 = !squeeze1;
			Timer.delay(0.25);
		} if (squeeze1) {
			flyFront.set(Value.kReverse);
		} else if (squeeze1 == false) {
			flyFront.set(Value.kForward);
		} else {}
		if (toggleRear) {
			squeeze2 = !squeeze2;
			Timer.delay(0.25);
		} if (squeeze2) {
			flyRear.set(Value.kForward);
		} else if (squeeze2 == false) {
			flyRear.set(Value.kReverse);
		} else {}
	}

	public void grab(boolean grabIn, boolean grabOut, boolean armOpen, boolean armClose, boolean armStall) {
		if (grabIn) {
			gbm.set(1);
		} else if (grabOut) {
			gbm.set(-1);
		} else {
			gbm.set(0.20);
		} if (armOpen) {
			armStallbool = false;
			armm1.set(0.8);
			armm2.set(0.8);
		} else if (armClose) {
			armStallbool = false;
			armm1.set(-0.5);
			armm2.set(-0.5);
		} else if (armStall) {
			if (armStallbool) {
				armStallbool = !armStallbool;
			} else {
				armStallbool = true;
			}
			Timer.delay(0.2);
		} else {
			if (armStallbool) {
				armm1.set(0.3);
				armm2.set(0.3);
			} else {
				armm1.set(0);
				armm2.set(0);
			}
		}
	}

	public void lift(double liftUp, double liftDown) {
		if (liftUp > 0.05 && liftDown < 0.05/** && limitSwitchUp.get() == false**/) {
			upm.set(1 * liftUp);
		} else if (liftDown > 0.05 && liftUp < 0.05/** && limitSwitchDown.get() == false**/) {
			upm.set(-1 * liftDown);
		} else {
			if (enc.getRaw() > -1000) {
				upm.set(0);
			} else {
				upm.set(0.23);
			}
		}
	}

	public void liftPos(boolean pos1, boolean pos2, boolean pos3, boolean pos4, boolean pos5, boolean pos6, boolean pos7, boolean pos8) {
		if (pos1) {
			liftPID(-1700);
			Timer.delay(0.1);
		} else if (pos2) {
			liftPID(-5000);
			Timer.delay(0.1);
		} else if (pos3) {
			liftPID(-8500);
			Timer.delay(0.1);
		} else if (pos4) {
			liftPID(-2900);
			Timer.delay(0.1);
		} else if (pos5) {
			liftPID(-6250);
			Timer.delay(0.1);
		} else if (pos6) { 
			liftPID(-9500);
			Timer.delay(0.1);
		} else if (pos7) {
			liftPID(-750);
			Timer.delay(0.1);
		} else if (pos8) {
			liftPID(-1850);
			Timer.delay(0.1);
		} else {
			upm.set(0);
		}
	}
}