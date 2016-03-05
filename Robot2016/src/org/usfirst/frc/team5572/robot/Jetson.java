package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Jetson {

	private static final int lowest = 6;

	private static PWM pwm[] = new PWM[5];

	public static void init() {
		for (int i = lowest; i <= lowest + 5; i++) {
			pwm[i - lowest] = new PWM(i);
		}
	}

	private static int distance, x, y, yaw, pitch;

	// Order: Yaw, Pitch, X, Y, Distance
	public static void update() {
		yaw = pwm[0].getRaw() - 128;
		pitch = pwm[1].getRaw() - 128;
		x = pwm[2].getRaw();
		y = pwm[3].getRaw();
		distance = pwm[4].getRaw();

		SmartDashboard.putNumber("tegra_yaw", yaw);
		SmartDashboard.putNumber("tegra_pitch", pitch);
		SmartDashboard.putNumber("tegra_x", x);
		SmartDashboard.putNumber("tegra_y", y);
		SmartDashboard.putNumber("tegra_distance", distance);
	}

}