package org.usfirst.frc.team5572.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

import static org.usfirst.frc.team5572.robot.Conf.*;

public class Snoopr {

	private static AHRS ahrs; // NAV-X
	private static Encoder left, right;
	private static AnalogInput poten, pressureSwitch; // Cannon Potentiometer
	private static DigitalInput cockDio, lockDio, grabberDio, liftBottom, liftTop; // Digital
																					// inputs
	private static final double a = (a1 - a0) / (v1 - v0); // Voltage
															// coefficient for
															// potentiometer
	private static final double k = a1 - a * v1; // Voltage constant for
													// potentiometer
	private static final double pa = (p3 - p2) / (v3 - v2);
	private static final double pk = p3 - pa * v3;

	public static void init() {
		ahrs = new AHRS(SPI.Port.kMXP);
		ahrs.reset();
		left = new Encoder(5, 6, true, EncodingType.k4X);
		right = new Encoder(3, 4, true, EncodingType.k4X);
		poten = new AnalogInput(2);
		pressureSwitch = new AnalogInput(1);
		grabberDio = new DigitalInput(2);
		cockDio = new DigitalInput(0);
		lockDio = new DigitalInput(1);
		liftBottom = new DigitalInput(7);
		liftTop = new DigitalInput(8);
	}

	public static double getTotalYaw() {
		return ahrs.getAngle();
	}

	public static boolean[] getDio() {
		return new boolean[] { !cockDio.get(), !lockDio.get(), grabberDio.get(), liftBottom.get(), liftTop.get() };
	}

	public static double getAngle() {
		double m = a * getV() + k;
		while (m > 180)
			m -= 360;
		while (m < -180)
			m += 360;
		return m;
	}

	public static double getV() {
		/*
		 * double m = 0, max = -1, min = -1; for (int i = 0; i <
		 * potentiometer_avg_amnt; i++) { m += poten.getValue()/819.2; max = max
		 * == -1 ? m : (max > m ? max : m); min = min == -1 ? m : (min < m ? min
		 * : m); } m -= max; m -= min;
		 */
		return /* m / (potentiometer_avg_amnt - 2) */ poten.getAverageVoltage();
	}

	public static void zero() {
		ahrs.zeroYaw();
	}

	public static double getLeftEncoderRaw() {
		return left.get();
	}

	public static double getRightEncoderRaw() {
		return right.get();
	}

	public static void resetEncoders() {
		right.reset();
		left.reset();
	}

	public static double getPressureSwitchV() {
		return pressureSwitch.getAverageVoltage();
	}

	public static double getPressure() {
		return getPressureSwitchV() * pa + pk;
	}

}
