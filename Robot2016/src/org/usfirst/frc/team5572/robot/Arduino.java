package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static org.usfirst.frc.team5572.robot.Configuration.*;

import static org.usfirst.frc.team5572.robot.Utils.*;

public class Arduino {
	private static AnalogInput ai;
	private static AnalogInput angle;
	private static SpeedController ao;
	private static DigitalOutput run;
	private static DigitalInput found;
	private static boolean running;

	public static void init() {
		ai = new AnalogInput(an_power_in);
		angle = new AnalogInput(an_angle_in);
		ao = new VictorSP(an_angle_out);
		run = new DigitalOutput(dio_angle_run);
		found = new DigitalInput(dio_angle_found);
		AnalogInput.setGlobalSampleRate(31250);
	}

	public static void setAngle(double angle) {
		ao.set(map(angle, def_angle_min, def_angle_max, -1, 1));
	}

	public static boolean angle(SpeedController sc, double angle) {
		start();
		setAngle(angle);
		if (isInPlace()) {
			end();
			sc.set(angle < 70 ? -0.09 : angle > 100 ? 0.1 : 0);
			return true;
		}
		sc.set(getMotor());
		return false;
	}

	public static void start() {
		run.set(true);
		running = true;
	}

	public static void end() {
		run.set(false);
		running = false;
	}

	public static boolean isRunning() {
		return running;
	}

	public static double getAngle() {
		double[] dataAngle = new double[def_sampler_size];
		for (int i = 0; i < def_sampler_size; i++) {
			dataAngle[i] = angle.getAverageVoltage();
		}
		double angle = Math.round(4 * map(mode(dataAngle), 2.673, 4.477,
				def_angle_min, def_angle_max)) / 4;
		return angle;
	}

	public static void snoop() {
		SmartDashboard.putNumber("angle", getAngle());
		SmartDashboard.putNumber("motor", getMotor());
		SmartDashboard.putBoolean("running", isRunning());
	}

	public static double getMotor() {
		double[] data = new double[def_sampler_size];
		for (int i = 0; i < def_sampler_size; i++) {
			data[i] = ai.getAverageVoltage();
		}
		double modedata = mode(data);
		double motorOut = clamp(
				modedata < 3.4 ? map(modedata, .2, 3.4, -.33, 0)
						: (modedata > 3.4 ? map(modedata, 4.553, 3.4, 0.13, 0)
								: 0), -0.5, 0.15);
		return motorOut;
	}

	public static boolean isInPlace() {
		return found.get();
	}
}
