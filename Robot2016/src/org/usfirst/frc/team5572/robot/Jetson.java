package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Jetson {

	private static AnalogInput aix, aid;

	public static void init() {
		aix = new AnalogInput(0);
		aid = new AnalogInput(3);
	}

	private static double distance, x;

	// Order: Yaw, Pitch, X, Y, Distance
	public static void update() {
		distance = getScaled(aid.getAverageVoltage());
		x = getScaled(aix.getAverageVoltage()) - 128;
	}

	public static void autoTurn() {
		if (Math.abs(x - 128) < 1)
			return;
		DriveTrain.drive(0, Conf.limit(x / 16));
	}

	public static double getDistance() {
		return distance;
	}

	private static double getScaled(double x) {
		return (-2.473529652 * x * x + 103.2808743 * x - 13.4680382);
	}

}