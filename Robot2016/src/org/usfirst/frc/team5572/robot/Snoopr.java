package org.usfirst.frc.team5572.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

import static org.usfirst.frc.team5572.robot.Configuration.*;

public class Snoopr {

	private static AHRS ahrs; // NAV-X
	private static Encoder left, right;
	private static AnalogInput poten, pressureSwitch; // Cannon Potentiometer
	private static DigitalInput liftBottom, liftTop;

	public static void init() {
		ahrs = new AHRS(SPI.Port.kMXP);
		ahrs.reset();
		left = new Encoder(5, 6, true, EncodingType.k4X);
		right = new Encoder(3, 4, true, EncodingType.k4X);
		poten = new AnalogInput(2);
		pressureSwitch = new AnalogInput(1);
		liftBottom = new DigitalInput(7);
		liftTop = new DigitalInput(8);
	}

	public static double getTotalYaw() {
		return ahrs.getAngle();
	}

	public static boolean[] getDio() {
		return new boolean[] { false, false, false, liftBottom.get(), liftTop.get() };
	}

	public static double getAngle() {
		double m = def_poten_a * getV() + def_poten_k;
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
		return getPressureSwitchV() * def_pressure_a + def_pressure_k;
	}

}
