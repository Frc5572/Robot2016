package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Jetson {

	private static AnalogInput aix, aid;

	public static void init() {
		aix = new AnalogInput(0);
		aid = new AnalogInput(3);
	}

	private static double distance, x;

	public static void snoop() {
		SmartDashboard.putNumber("tegra_x", 97.73965393945 * x - 9.14202416674);
		SmartDashboard.putNumber("tegra_dist", 33.057 * distance * distance + 57.85 * distance - 8.79);
		SmartDashboard.putNumber("tegra_x_raw", aix.getAverageVoltage());
		SmartDashboard.putNumber("tegra_dist_raw", aid.getAverageVoltage());
	}

	// Order: Yaw, Pitch, X, Y, Distance
	public static void update() {
		snoop();
		distance = getScaled(aid.getAverageVoltage());
		x = getScaled(aix.getAverageVoltage());
	}

	public static void autoTurn() {
		if (Math.abs(x - 128) < 1)
			return;
		// DriveTrain.drive(0, Conf.limit(x / 16));
	}

	public static double getDistance() {
		return distance;
	}

	private static double getScaled(double x) {
		return x;
	}

}