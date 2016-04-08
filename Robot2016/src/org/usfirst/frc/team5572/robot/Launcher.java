
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Configuration.bind_ctr_auto_angle0;
import static org.usfirst.frc.team5572.robot.Configuration.bind_ctr_auto_angle1;
import static org.usfirst.frc.team5572.robot.Configuration.bind_ctr_auto_angle2;
import static org.usfirst.frc.team5572.robot.Configuration.bind_ctr_auto_angle3;
import static org.usfirst.frc.team5572.robot.Configuration.bind_ctr_auto_angle4;
import static org.usfirst.frc.team5572.robot.Configuration.bind_ctr_intake;
import static org.usfirst.frc.team5572.robot.Configuration.bind_ctr_outtake;
import static org.usfirst.frc.team5572.robot.Configuration.can_wheel_intake;
import static org.usfirst.frc.team5572.robot.Configuration.def_default_angle_0;
import static org.usfirst.frc.team5572.robot.Configuration.def_default_angle_1;
import static org.usfirst.frc.team5572.robot.Configuration.def_default_angle_2;
import static org.usfirst.frc.team5572.robot.Configuration.def_default_angle_3;
import static org.usfirst.frc.team5572.robot.Configuration.def_default_angle_4;
import static org.usfirst.frc.team5572.robot.Configuration.def_sol_mod;
import static org.usfirst.frc.team5572.robot.Configuration.pwm_primer;
import static org.usfirst.frc.team5572.robot.Configuration.pwm_wheel_cannon;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Launcher {
    static SpeedController intake;
    static SpeedController  primer;
    static SpeedController angle_actual;
                           
    public static void init( ) {
        PowerDistributionPanel.setDefaultSolenoidModule(def_sol_mod);
        intake = new CANTalon(can_wheel_intake);
        primer = new CANTalon(pwm_primer);
        angle_actual = new VictorSP(pwm_wheel_cannon);
    }
    
    private static Value   rollingIn = Value.kOff;
    private static Value   primerIn = Value.kOff;
    private static boolean angle0    = false,
                                   angle1 = false, angle2 = false, angle3 = false, angle4 = false;
    private static long    wait      = 0;
                                     
    public static void update( ) {
        if ( rollingIn.equals(Value.kForward) ) {
            intake.set(-1);
        } else if ( rollingIn.equals(Value.kReverse) ) {
            intake.set(1);
        } else {
            intake.set(0);
        }
        if ( primerIn.equals(Value.kForward) ) {
            primer.set(-1);
        } else if ( primerIn.equals(Value.kReverse) ) {
            primer.set(1);
        } else {
            primer.set(0);
        }
        for ( int i = 0; i < 8; i++ ) {
            SmartDashboard.putNumber("Knob " + i, DriveStation.getKnob(i));
        }
        for ( int i = 0; i < 11; i++ ) {
            SmartDashboard.putBoolean("Button " + i, DriveStation.getSwitch(i));
        }
        angle_actual.set(DriveStation.b_getThrottle() * 0.6 * -DriveStation.b_y()
                - ( Arduino.getAngle() > 65 ? 0 : Arduino.getAngle() > -19 ? .15 : 0 ));
        SmartDashboard.putNumber("StickOut", DriveStation.b_getThrottle() * 0.6 * -DriveStation.b_y());
        if ( DriveStation.b_getKey(-1) && wait < System.nanoTime() ) {
            wait = System.nanoTime() + 1250000000L;
        }
        if ( DriveStation.b_getKey(bind_ctr_auto_angle0) || angle0 ) {
            if ( Arduino.angle(angle_actual, def_default_angle_0) ) {
                angle_actual.set(-.1);
                angle0 = false;
            } else {
                angle0 = true;
            }
        }
        if ( DriveStation.b_getKey(bind_ctr_auto_angle1) || angle1 ) {
            if ( Arduino.angle(angle_actual, def_default_angle_1) ) {
                angle_actual.set(-.1);
                angle1 = false;
            } else {
                angle1 = true;
            }
        }
        if ( DriveStation.b_getKey(bind_ctr_auto_angle2) || angle2 ) {
            if ( Arduino.angle(angle_actual, def_default_angle_2) ) {
                angle_actual.set(-.1);
                angle2 = false;
            } else {
                angle2 = true;
            }
        }
        if ( DriveStation.b_getKey(bind_ctr_auto_angle3) || angle3 ) {
            if ( Arduino.angle(angle_actual, def_default_angle_3) ) {
                angle_actual.set(-.1);
                angle3 = false;
            } else {
                angle3 = true;
            }
        }
        if ( DriveStation.b_getKey(bind_ctr_auto_angle4) || angle4 ) {
            if ( Arduino.angle(angle_actual, def_default_angle_4) ) {
                angle_actual.set(-.1);
                angle4 = false;
            } else {
                angle4 = true;
            }
        }
        if ( wait > System.nanoTime() ) {
            rollingIn = Value.kForward;
        } else {
            rollingIn = Value.kOff;
            primerIn = Value.kOff;
        }
        SmartDashboard.putBoolean("Found", Arduino.isInPlace());
        if ( wait - /* 1e9 / 2 */5e8 < System.nanoTime() && wait > System.nanoTime() ) {
            primerIn = Value.kReverse;
        }
        SmartDashboard.putNumber("wait", wait);
        /* if ( DriveStation.b_getKey(bind_ctr_auto_angle0) ) { if (
         * Arduino.angle(angle_actual, def_default_angle_0) )
         * angle_actual.set(-.1); } if (
         * DriveStation.b_getKey(bind_ctr_auto_angle1) ) { if (
         * Arduino.angle(angle_actual, def_default_angle_1) )
         * angle_actual.set(-.1); } */
        if ( DriveStation.b_getKey(bind_ctr_outtake) ) {
            rollingIn = Value.kForward;
        } else if ( DriveStation.b_getKey(bind_ctr_intake) ) {
            rollingIn = Value.kReverse;
        } else if ( wait < System.nanoTime() ) {
            rollingIn = Value.kOff;
        }
    }
}
