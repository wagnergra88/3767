/**********************/
/** Code written by: **/
/**   Liam Coyle &   **/
/**  Grayson Wagner  **/
/**    from team:    **/
/**    3767 TITAN    **/
/**********************/

package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.VictorSP;

//@SuppressWarnings("unused")
public class Drive {
	ADXRS450_Gyro gyro;
	VictorSP flm;
	VictorSP frm;
	VictorSP rlm;
	VictorSP rrm;
	boolean switched = false;

	public Drive(final int flmP, final int frmP, final int rlmP, final int rrmP) {
		gyro = new ADXRS450_Gyro();
		flm = new VictorSP(flmP);
		frm = new VictorSP(frmP);
		rlm = new VictorSP(rlmP);
		rrm = new VictorSP(rrmP);
	}

	public void arcadedrive(double ljy, double ljx) { //standard arcade, used for autonomous
		flm.set(-ljy + ljx);
		frm.set(ljy + ljx);
		rlm.set(-ljy + ljx);
		rrm.set(ljy + ljx);
	}

	public void dualDrive(double ljy, double rjy, double ljx, boolean toggle) { //normal drive code for swiching between tank and arcade
		if (toggle) {
			switched = !switched;
			Timer.delay(0.25);
		} if (switched) {
			flm.set(-ljy);
			frm.set(rjy);
			rlm.set(-ljy);
			rrm.set(rjy);
		} else {
			flm.set(-ljy + ljx);
			frm.set(ljy + ljx);
			rlm.set(-ljy + ljx);
			rrm.set(ljy + ljx);
		}
	}

	public void mecanumDrive(double ljy, double ljx, double rjx) { //standard mecanum drive code
		double r = Math.hypot(ljx, ljy);
		double robotAngle = Math.atan2(ljy, -ljx) - Math.PI/4;
		final double fl = r * Math.cos(robotAngle) - rjx;
		final double fr = r * Math.sin(robotAngle) + rjx;
		final double rl = r * Math.sin(robotAngle) - rjx;
		final double rr = r * Math.cos(robotAngle) + rjx;

		flm.set(-fl);
		frm.set(fr);
		rlm.set(-rl);
		rrm.set(rr);
	}

	public void cartDrive(double ljy, double ljx, double rjx) { //cartesian drive code
		double y = ljy;
		double x = ljx;
		double gyroAngle = (gyro.getAngle() + 90);

		y = -y;

	    double[] rotated = rotateVector(x, y, gyroAngle);
	    x = rotated[0];
	    y = rotated[1];

		flm.set(y + x + rjx);
		frm.set(-y + x + rjx);
		rlm.set(y - x + rjx);
		rrm.set(-y - x + rjx);
	}

	  public double[] rotateVector(double x, double y, double angle) { //Necessary for cartesian drive
		    double cosA = Math.cos(angle * (3.14159 / 180.0));
		    double sinA = Math.sin(angle * (3.14159 / 180.0));
		    double[] out = new double[2];
		    out[0] = x * cosA - y * sinA;
		    out[1] = x * sinA + y * cosA;
		    return out;
	  }
}