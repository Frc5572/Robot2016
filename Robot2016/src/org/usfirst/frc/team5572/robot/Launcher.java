
package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import static org.usfirst.frc.team5572.robot.Configuration.*;



public class Launcher {
    private static CANTalon        intake;
    private static DoubleSolenoid  primer;
    private static SpeedController angle_actual;
                                   
    public static void init( ) {
        PowerDistributionPanel.setDefaultSolenoidModule(def_sol_mod);
        intake = new CANTalon(can_wheel_intake);
        primer = new DoubleSolenoid(def_sol_mod, sol_primer_f, sol_primer_r);
        angle_actual = new VictorSP(pwm_wheel_cannon);
    }
    
    private static Value rollingIn = Value.kOff;
    
    public static void update( ) {
        if ( rollingIn.equals(Value.kForward) ) {
            intake.set(1);
        } else if ( rollingIn.equals(Value.kReverse) ) {
            intake.set(-1);
        } else {
            intake.set(0);
        }
        
        angle_actual.set(DriveStation.b_getThrottle() * 0.6 * -DriveStation.b_y());
        
        primer.set(DriveStation.b_getKey(-1) ? Value.kForward : Value.kReverse);
        
        if ( DriveStation.b_getKey(bind_ctr_outtake) ) {
            rollingIn = Value.kForward;
        } else if ( DriveStation.b_getKey(bind_ctr_intake) ) {
            rollingIn = Value.kReverse;
        } else {
            rollingIn = Value.kOff;
        }
    }
}
