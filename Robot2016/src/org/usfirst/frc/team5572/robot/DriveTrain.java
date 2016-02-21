package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;

import static org.usfirst.frc.team5572.robot.Conf.*;

public class DriveTrain {

	private static final int[] leftCIMs = { 3, 4, 5 }; // PWM Channels
	private static final int[] rightCIMs = { 0, 1, 2 }; // PWM Channels

	private static SpeedController[] left = new SpeedController[leftCIMs.length];
	private static SpeedController[] right = new SpeedController[rightCIMs.length];

	private static int currdist = -1;

	public static void init() {
		for (int i = 0; i < leftCIMs.length; i++) {
			left[i] = new VictorSP(leftCIMs[i]);
		}
		for (int i = 0; i < rightCIMs.length; i++) {
			right[i] = new VictorSP(rightCIMs[i]);
		}
	}

	public static void teleop() {
		drive();
		feedData();
	}

	public static void feedData() {

	}

	private static void drive() {
		double k = DriveStation.a_getThrottle();
		double y = k * DriveStation.a_y();
		double x = k * DriveStation.a_x();
		double l, r;
		x = signum(x) * (x * x);
		y = signum(y) * (y * y);
		if (signum(x) == signum(y)) {
			r = signum(y) * max(y * signum(y), x * signum(x));
			l = y - x;
		} else {
			l = signum(y) * max(y * signum(y), x * signum(x));
			r = y + x;
		}
		drivelr(l, r);
	}

	public static void drivelr(double l, double r) {
		for (int i = 0; i < left.length; i++) {
			left[i].set(-l);
		}
		for (int i = 0; i < right.length; i++) {
			right[i].set(r);
		}
	}

	public static void drive(double o, double c) {
		double x = o;
		double y = c;
		double l, r;
		if (y >= 0.0) {
			y = (y * y);
		} else {
			y = -(y * y);
		}
		if (x >= 0.0) {
			x = (x * x);
		} else {
			x = -(x * x);
		}
		if (signum(x) == signum(y)) {
			r = signum(y) * max(y * signum(y), x * signum(x));
			l = y - x;
		} else {
			l = signum(y) * max(y * signum(y), x * signum(x));
			r = y + x;
		}
		drivelr(l, r);
	}
	
	public static void driveStraightReset(){
		currdist = -1;
	}

	public static boolean driveStraight(double speed, int dist) {
		if (currdist == -1) {
			currdist = dist;
			Snoopr.resetEncoders();
		}

		double l = Snoopr.getLeftEncoderDistance();
		double r = Snoopr.getLeftEncoderDistance();
		if ((l + r) / 2 >= currdist)
			return true;
		drive(speed, (l - r) / straight_line_divisor);
		return false;
	}

	private static double max(double a, double b) {
		return a > b ? a : b;
	}

	private static int signum(double a) {
		return a == 0 ? 0 : (a > 0 ? 1 : -1);
	}
}