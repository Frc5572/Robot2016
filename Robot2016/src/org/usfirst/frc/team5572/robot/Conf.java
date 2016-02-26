package org.usfirst.frc.team5572.robot;

public class Conf {

	/* Controller */
	/** Button that fires the cannon */
	public static final int button_shoot = -1;
	/** Button that rolls the wheel inward */
	public static final int button_roll = 11;
	/** Button that rolls the wheel outward */
	public static final int button_unroll = 12;

	/** Button that opens the claw, without firing */
	public static final int button_oclaw = 9;
	/** Button that closes the claw */
	public static final int button_cclaw = 10;

	/**
	 * Power coefficient while the launcher is lowering its angle. Should not
	 * exceed 1
	 */
	public static final double cannon_motor_coef_down = 0.5;
	/**
	 * Power coefficient while the launcher is raising its angle. Should not
	 * exceed 1
	 */
	public static final double cannon_motor_coef_up = 0.75;

	/** Button that turns on or off the automatic cocking mechanism */
	public static final int button_cancel = 7;

	/* Solenoids */

	/** Solenoid id for the grabber */
	public static final int grabber_forward = 2;
	/** Solenoid id for the cocking mechanism */
	public static final int pull_forward = 3;
	/** Solenoid id for the lock */
	public static final int lock_forward = 1;
	/** Solenoid id for the lifters */
	public static final int plat_forward = 0;

	/**
	 * Solenoid Reverse id. Automatically Generated on startup, do not change
	 */
	public static final int grabber_reverse = grabber_forward + 4, pull_reverse = pull_forward + 4,
			lock_reverse = lock_forward + 4, plat_reverse = plat_forward + 4;
	
	public static final int lift_forward = 8;
	public static final int lift_reverse = 9;

	/** Limit the value to between -1 and 1 */
	public static double limit(double d) {
		return d > 1 ? 1 : (d < -1 ? -1 : d);
	}

	/** Where the Y is the aim of the robot for the camera **/
	public static final int yAim = 255;

	public static final double v1 = 4.2;
	public static final double v0 = 2.63;

	public static final double a1 = 63;
	public static final double a0 = -44;

	/** Speed of the intake wheels */
	public static final double rollSpeed = 1;

	public static final int launcherWait = 35;
	public static final int resetWait = 50;

	public static final int potentiometer_avg_amnt = 10;

	public static final double straight_line_divisor = 20;

	public static final double analog_play = 0.01;

	public static final int tegra_channels[] = { 0, 7, 8 }; // {analog,
																// in,
																// request }

}
