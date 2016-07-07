
package org.usfirst.frc.team5572.robot;

public class Conf {
    public static final boolean debug_smartDash      = false; // show debug data variable
    public static final boolean drawl_smartDash      = false; // show markings on camera feed
    public static final short   an_angle_in          = 2; // analog port
    public static final short   an_tegra_in          = 1; // analog port
    public static final short   an_dist_in           = 0; // analog port
    public static final short   an_angle_out         = 9; // analog port
    public static final short   an_power_in          = 3; // analog port
    public static final short   dio_enc_r_a          = 3; // digital I/O port
    public static final short   dio_enc_r_b          = 4; // digital I/O port
    public static final short   dio_enc_l_a          = 5; // digital I/O port
    public static final short   dio_enc_l_b          = 6; // digital I/O port
    public static final short   dio_lift_top         = 8; // digital I/O port
    public static final short   dio_lift_low         = 7; // digital I/O port
    public static final short   dio_bin              = 2; // digital I/O port
    public static final short   dio_angle_run        = 0; // digital I/O port
    public static final short   dio_angle_found      = 1; // digital I/O port
    public static final short   dio_tegra_run        = 9; // digital I/O port
    public static final short   dio_tegra_found      = 1; // digital I/O port
    public static final short   pwm_wheel_fl         = 3; // PWM port
    public static final short   pwm_wheel_fr         = 2; // PWM port
    public static final short   pwm_wheel_bl         = 5; // PWM port
    public static final short   pwm_wheel_br         = 4; // PWM port
    public static final short   pwm_primer0           = 0; // PWM port
    public static final short   pwm_primer1           = 1; // PWM port
    public static final short   sol_lift_primer_f    = 0; // PCM port
    public static final short   sol_lift_primer_r    = 1; // PCM port
    public static final int     can_wheel_lift       = 1; // CAN port (Digitally assigned)
    public static final int     can_wheel_intake     = 3; // CAN port (Digitally assigned)
    public static final int     bind_ctr_intake      = 4; // Controller key (Operator)
    public static final int     bind_ctr_outtake     = 3; // Controller key (Operator)
    public static final int     bind_ctr_fire        = -1; // Controller key (Operator)
    public static final int     bind_ctr_auto_angle0 = 5; // Controller key (Operator)
    public static final int     bind_ctr_setup_ball  = 2; // Controller key (Operator)
    public static final int     bind_drv_lift_prime  = 8; // Controller key (Driver)
    public static final int     bind_drv_lift_up     = 6; // Controller key (Driver)
    public static final int     bind_drv_lift_dn     = 7; // Controller key (Driver)
    public static final double  def_angle_max        = 66.76; // Defined value (max value arduino can send)
    public static final double  def_angle_min        = -22.69; // Defined value (min value arduino can send)
    public static final double  def_angle_max_out    = 80; // Defined value (max value arduino can read)
    public static final double  def_angle_min_out    = -25; // Defined value (min value arduino can read)
    public static final int     def_sampler_size     = 11; // Amount of values to read before handling
}
