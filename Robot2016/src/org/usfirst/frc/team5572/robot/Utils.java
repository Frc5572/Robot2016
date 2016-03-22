package org.usfirst.frc.team5572.robot;

public class Utils {

	public static final double clamp(double a, double min, double max) {
		return a < min ? min : (a > max ? max : a);
	}

	public static final double clampMotor(double a) {
		return clamp(a, -1, 1);
	}

	public static final double clampPositive(double a) {
		return clamp(a, 0, 1);
	}

	public static final double signum(double a) {
		return a == 0 ? 0 : a > 0 ? 1 : -1;
	}

	public static double max(double a, double b) {
		return a > b ? a : b;
	}

}
