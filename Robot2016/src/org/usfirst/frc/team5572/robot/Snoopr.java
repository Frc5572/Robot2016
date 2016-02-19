package org.usfirst.frc.team5572.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

import static org.usfirst.frc.team5572.robot.Conf.*;

public class Snoopr {

	private static AHRS ahrs; // NAV-X
	private static Encoder left, right;
	private static AnalogInput poten; // Cannon Potentiometer
	private static DigitalInput cockDio, lockDio, grabberDio; // Digital inputs
	private static final double a = (a1 - a0) / (v1 - v0); // Voltage
															// coefficient for
															// potentiometer
	private static final double k = a1 - a * v1; // Voltage constant for
													// potentiometer

	private static double min = -1, max = -1;

	public static void init() {
		ahrs = new AHRS(SPI.Port.kMXP);
		left = new Encoder(5, 6);
		right = new Encoder(3, 4);
		poten = new AnalogInput(2);
		grabberDio = new DigitalInput(2);
		cockDio = new DigitalInput(0);
		lockDio = new DigitalInput(1);
	}

	public static void markAngleDirty() {
		min = -1;
	}

	public static double getTotalYaw() {
		return ahrs.getAngle();
	}

	public static boolean[] getDio() {
		return new boolean[] { cockDio.get(), !lockDio.get(), grabberDio.get() };
	}

	public static double getAngle() {
		double m = a * poten.getVoltage() + k;
		while (m > 180)
			m -= 360;
		while (m < -180)
			m += 360;
		if (min == -1) {
			min = m;
			max = m;
		}
		max = max > m ? max : m;
		min = min < m ? min : m;
		return m/* (max + min)/2 */;
	}

	public static double getV() {
		double m = poten.getVoltage();
		while (m > 180)
			m -= 360;
		while (m < -180)
			m += 360;
		return m;
	}

	public static void zero() {
		ahrs.zeroYaw();
	}

	public static double getLeftEncoderDistance() {
		return left.get();
	}

	public static double getRightEncoderDistance() {
		return right.get();
	}

}
