package org.usfirst.frc.team5572.alamo;

import static org.usfirst.frc.team5572.alamo.Conf.*;

import org.usfirst.frc.team5572.alamo.DriveStation;
import org.usfirst.frc.team5572.alamo.DriveTrain;
import org.usfirst.frc.team5572.alamo.Snoopr;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain {

	private static final int[] leftCIMs = { 3, 5 }; // PWM Channels
	private static final int[] rightCIMs = { 2, 4 }; // PWM Channels

	private static SpeedController[] left = new SpeedController[leftCIMs.length];
	private static SpeedController[] right = new SpeedController[rightCIMs.length];

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
		SmartDashboard.putNumber("Left Encoders", Snoopr.getLeftEncoderRaw());
		SmartDashboard.putNumber("Right Encoders", Snoopr.getRightEncoderRaw());
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

	public static void driveStraightReset() {
		Snoopr.resetEncoders();
	}

	public static boolean driveStraight(double speed, double dist) {
		if (Snoopr.getRightEncoderRaw() <= dist) {
			double delta = Snoopr.getRightEncoderRaw() - Snoopr.getLeftEncoderRaw();
			double modDelta = delta / 100d;
			double speed2 = clamp(0.002*dist - Snoopr.getRightEncoderRaw(), .3, speed);
			DriveTrain.drivelr(-speed2, (-speed2 + modDelta));
			return false;
		}
		return true;
	}

	private static double max(double a, double b) {
		return a > b ? a : b;
	}

	private static int signum(double a) {
		return a == 0 ? 0 : (a > 0 ? 1 : -1);
	}

	public static boolean setGlobalAngle(double angle, double thresh) {
		double curr = Snoopr.getAngle();
		if (Math.abs(angle - curr) <= thresh)
			return true;
		turn(angle - curr);
		return false;
	}

	public static void turn(double angle) {
		while (angle > 180)
			angle -= 360;
		while (angle < -180)
			angle += 360;
		drive(0, angle / 180);
	}
}