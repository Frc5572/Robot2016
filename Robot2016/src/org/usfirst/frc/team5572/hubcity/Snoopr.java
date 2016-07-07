package org.usfirst.frc.team5572.hubcity;

import static org.usfirst.frc.team5572.hubcity.Configuration.dio_enc_l_a;
import static org.usfirst.frc.team5572.hubcity.Configuration.dio_enc_l_b;
import static org.usfirst.frc.team5572.hubcity.Configuration.dio_enc_r_a;
import static org.usfirst.frc.team5572.hubcity.Configuration.dio_enc_r_b;
import static org.usfirst.frc.team5572.hubcity.Configuration.dio_lift_low;
import static org.usfirst.frc.team5572.hubcity.Configuration.dio_lift_top;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;

public class Snoopr {

	private static AHRS ahrs; // NAV-X
	private static Encoder left, right;
	private static DigitalInput liftBottom, liftTop;

	public static void init() {
		ahrs = new AHRS(SPI.Port.kMXP);
		ahrs.reset();
		left = new Encoder(dio_enc_l_a, dio_enc_l_b, true, EncodingType.k4X);
		right = new Encoder(dio_enc_r_a, dio_enc_r_b, true, EncodingType.k4X);
		liftBottom = new DigitalInput(dio_lift_low);
		liftTop = new DigitalInput(dio_lift_top);
	}

	public static double getTotalYaw() {
		return ahrs.getAngle();
	}

	public static boolean[] getDio() {
		return new boolean[] { false, false, false, liftBottom.get(),
				liftTop.get() };
	}

	public static void zero() {
		ahrs.zeroYaw();
	}

	public static double getLeftEncoderRaw() {
		return left.get();
	}

	public static double getRightEncoderRaw() {
		return right.get() * 1.08;
	}

	public static void resetEncoders() {
		right.reset();
		left.reset();
	}

}
