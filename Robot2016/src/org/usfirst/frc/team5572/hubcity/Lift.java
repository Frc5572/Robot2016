
package org.usfirst.frc.team5572.hubcity;

import static org.usfirst.frc.team5572.hubcity.Configuration.bind_drv_lift_dn;
import static org.usfirst.frc.team5572.hubcity.Configuration.bind_drv_lift_prime;
import static org.usfirst.frc.team5572.hubcity.Configuration.bind_drv_lift_up;
import static org.usfirst.frc.team5572.hubcity.Configuration.can_wheel_lift;
import static org.usfirst.frc.team5572.hubcity.Configuration.sol_lift_primer_f;
import static org.usfirst.frc.team5572.hubcity.Configuration.sol_lift_primer_r;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SpeedController;



public class Lift {
    private static DoubleSolenoid  platform;
    private static SpeedController sc;
                                   
    public static void init( ) {
        platform = new DoubleSolenoid(12, sol_lift_primer_f, sol_lift_primer_r);
        sc = new CANTalon(can_wheel_lift);
        ((CANTalon) sc).enableBrakeMode(true);
        platform.set(Value.kForward);
    }
    
    private static boolean cancelPressed = false;
    
    public static void update( boolean test ) {
        if ( DriveStation.a_getKey(bind_drv_lift_prime) && !cancelPressed ) {
            platform.set(platform.get() == Value.kForward ? Value.kReverse : Value.kForward);
            cancelPressed = true;
        } else if ( !DriveStation.a_getKey(bind_drv_lift_prime) ) {
            cancelPressed = false;
        }
        if ( DriveStation.a_getKey(bind_drv_lift_dn) ) {
            sc.set(-.8);
        } else if ( ( DriveStation.a_getKey(bind_drv_lift_up) && Snoopr.getDio()[4] ) ) {
            sc.set(0.8);
        } else {
            sc.set(0);
        }
        if ( !Snoopr.getDio()[3] && !test ) {
            sc.set(0);
        }
    }
}
