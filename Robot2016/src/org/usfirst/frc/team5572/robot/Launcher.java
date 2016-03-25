
package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;

import static org.usfirst.frc.team5572.robot.Configuration.*;



public class Launcher {
    private static CANTalon        intake;
    private static DoubleSolenoid  primer;
    private static PWM             setAngle;
    private static SpeedController angle_actual;
                                   
    public static void init( ) {
        intake = new CANTalon(can_wheel_intake);
        primer = new DoubleSolenoid(def_sol_mod, sol_primer_f, sol_primer_r);
        setAngle = new PWM(pwm_angle_out);
        angle_actual = new CANTalon(can_wheel_cannon);
    }
    
    private static Value rollingIn = Value.kOff;
    
    public static void update( ) {
        if ( rollingIn.equals(Value.kForward) ) {
            intake.set(1);
        } else if ( rollingIn.equals(Value.kReverse) ) {
            intake.set(-.5);
        } else {
            intake.set(0);
        }
        angle_actual.set(DriveStation.b_y());
        
        if ( DriveStation.b_getKey(11) ) {
            rollingIn = Value.kForward;
        } else if ( DriveStation.b_getKey(12) ) {
            rollingIn = Value.kReverse;
        } else {
            rollingIn = Value.kOff;
        }
    }
}
