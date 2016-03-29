
package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    
    private static Value   rollingIn = Value.kOff;
    private static boolean angle0    = false,
                                   angle1 = false;
    private static long    wait      = 0;
                                     
    public static void update( ) {
        if ( rollingIn.equals(Value.kForward) ) {
            intake.set(1);
        } else if ( rollingIn.equals(Value.kReverse) ) {
            intake.set(-1);
        } else {
            intake.set(0);
        }
        for ( int i = 0; i < 8; i++ ) {
            SmartDashboard.putNumber("Knob " + i, DriveStation.getKnob(i));
        }
        for ( int i = 0; i < 11; i++ ) {
            SmartDashboard.putBoolean("Button " + i, DriveStation.getSwitch(i));
        }
        angle_actual.set(DriveStation.b_getThrottle() * 0.6 * -DriveStation.b_y() - 0.1);
        if ( DriveStation.b_getKey(-1) && wait < System.nanoTime() ) {
            wait = System.nanoTime() + 1250000000L;
        }
        if ( DriveStation.b_getKey(bind_ctr_auto_angle0) || angle0 ) {
            if ( Arduino.angle(angle_actual, def_default_angle_0) ) {
                angle_actual.set(-.1);
                angle0 = false;
            } else
                angle0 = true;
        }
        if ( DriveStation.b_getKey(bind_ctr_auto_angle1) || angle1 ) {
            if ( Arduino.angle(angle_actual, def_default_angle_1) ) {
                angle_actual.set(-.1);
                angle1 = false;
            } else
                angle1 = true;
        }
        if ( wait > System.nanoTime() ) {
            rollingIn = Value.kForward;
        } else {
            rollingIn = Value.kOff;
            primer.set(Value.kReverse);
        }
        if ( wait - 1e9 / 2 < System.nanoTime() && wait > System.nanoTime() ) {
            primer.set(Value.kForward);
        }
        SmartDashboard.putNumber("wait", wait);
        /* if ( DriveStation.b_getKey(bind_ctr_auto_angle0) ) {
         * if ( Arduino.angle(angle_actual, def_default_angle_0) )
         * angle_actual.set(-.1);
         * }
         * if ( DriveStation.b_getKey(bind_ctr_auto_angle1) ) {
         * if ( Arduino.angle(angle_actual, def_default_angle_1) )
         * angle_actual.set(-.1);
         * } */
        if ( DriveStation.b_getKey(bind_ctr_outtake) ) {
            rollingIn = Value.kForward;
        } else if ( DriveStation.b_getKey(bind_ctr_intake) ) {
            rollingIn = Value.kReverse;
        } else if ( wait < System.nanoTime() ) {
            rollingIn = Value.kOff;
        }
    }
}
