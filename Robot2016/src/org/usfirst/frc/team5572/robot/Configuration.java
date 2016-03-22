package org.usfirst.frc.team5572.robot;

public class Configuration {

	public static final short an_potentiometer = 0;
	public static final short an_pressure = 0;
	public static final short dio_enc_r_a = 0;
	public static final short dio_enc_r_b = 0;
	public static final short dio_enc_l_a = 0;
	public static final short dio_enc_l_b = 0;
	public static final short dio_lift_top = 0;
	public static final short dio_lift_low = 0;
	public static final short dio_cannon_top = 0;
	public static final short dio_cannon_low = 0;
	public static final short sol_primer_f = 0;
	public static final short sol_primer_r = 0;
	public static final short sol_lift_primer = 0;
	public static final short pwm_wheel_fl = 0;
	public static final short pwm_wheel_fr = 0;
	public static final short pwm_wheel_bl = 0;
	public static final short pwm_wheel_br = 0;
	public static final int can_wheel_lift = 0;
	public static final int can_wheel_cannon = 0;
	public static final int can_wheel_intake = 0;
	public static final int bind_ctr_intake = 2;
	public static final int bind_ctr_fire = -1;
	public static final int bind_drv_up_qual = 0;
	public static final int bind_drv_dn_qual = 0;
	public static final int bind_drv_lift_prime = 0;
	public static final int bind_drv_lift_up = 0;
	public static final int bind_drv_lift_dn = 0;
	public static final int def_camera_quality = 50;
	public static final int def_sol_mod = 12;
	public static final int def_roll_sleep = 20;
	public static final int def_primer_sleep = 20;
	public static final int def_poten_a = 0;
	public static final int def_poten_k = 0;
	public static final int def_pressure_a = 0;
	public static final int def_pressure_k = 0;

	public static final double a = 0; // (a1 - a0) / (v1 - v0); // Voltage
	// coefficient for
	// potentiometer
	public static final double k = 0; // a1 - a * v1; // Voltage constant for
	// potentiometer
	public static final double pa = 0; // (p3 - p2) / (v3 - v2);
	public static final double pk = 0; // p3 - pa * v3;

}
