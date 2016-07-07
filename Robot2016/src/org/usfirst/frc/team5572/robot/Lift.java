
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.*;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;



/** Handler of Lift operations*/
public class Lift {
    private static DoubleSolenoid  platform; // Solenoid for primer (piston that
                                             // pushes out the lift)
    private static SpeedController sc;       // Motor for lift
                                   
    /**
     * Initialization method for lift components. Must be called before any
     * other method in Lift
     */
    public static void init( ) {
        platform = new DoubleSolenoid(12, sol_lift_primer_f, sol_lift_primer_r);
        sc = new CANTalon(can_wheel_lift);
        ( ( CANTalon ) sc ).enableBrakeMode(true); // This causes the motor to
                                                   // apply force to reach the
                                                   // desired speed, rather than
                                                   // accelerating at the
                                                   // desired speed.
        platform.set(Value.kForward);
    }
    
    private static boolean cancelPressed = false; // Tap boolean
    
    /**
     * Update inputs and outputs of the lift system.
     * 
     * @param test
     *            whether or not to check sensors (false to check, true to not)
     */
    public static void update( boolean test ) {
        if ( DriveStation.a_getKey(bind_drv_lift_prime) && !cancelPressed ) {
            platform.set(platform.get() == Value.kForward ? Value.kReverse : Value.kForward);
            cancelPressed = true;
        } else if ( !DriveStation.a_getKey(bind_drv_lift_prime) ) {
            cancelPressed = false;
        }
        if ( DriveStation.a_getKey(bind_drv_lift_dn) && ( Snoopr.getDio()[3] || test ) ) {
            sc.set(-.8);
        } else if ( ( DriveStation.a_getKey(bind_drv_lift_up) && Snoopr.getDio()[4] )
                && ( Snoopr.getDio()[3] || test ) ) {
            sc.set(.8);
        } else {
            sc.set(0);
        }
        if ( !Snoopr.getDio()[3] && !test ) {
            sc.set(0);
        }
    }
}
