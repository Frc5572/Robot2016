
package org.usfirst.frc.team5572.broken;

import static org.usfirst.frc.team5572.robot.Conf.bind_ctr_intake;
import static org.usfirst.frc.team5572.robot.Conf.bind_ctr_outtake;
import static org.usfirst.frc.team5572.robot.Conf.bind_ctr_setup_ball;
import static org.usfirst.frc.team5572.robot.Conf.can_wheel_intake;
import static org.usfirst.frc.team5572.robot.Conf.pwm_primer0;
import static org.usfirst.frc.team5572.robot.Conf.pwm_primer1;

import org.usfirst.frc.team5572.robot.Arduino;
import org.usfirst.frc.team5572.robot.DriveStation;
import org.usfirst.frc.team5572.robot.Snoopr;
import org.usfirst.frc.team5572.util.Roller;
import org.usfirst.frc.team5572.util.TimerSystem;
import org.usfirst.frc.team5572.util.TimerSystem.Time;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;



public class Launcher {
    public static SpeedController linAct;                                     // Linear
                                                                              // actuator
    private static Roller         primer0, primer1, roller; // Top wheel, Bottom
                                                            // wheel, flywheels
    public static boolean         m = false;                               //
                                    
    /** Arduino set angle utility method for the linear actuator */
    public static boolean setAngle( double angle ) {
        return Arduino.angle(linAct, angle, 0.4);
    }
    
    /**
     * Initialization method for launcher components. Must be called before any
     * other method in Launcher
     */
    public static void init( ) {
        linAct = new CANTalon(7);
        roller = new Roller(new CANTalon(can_wheel_intake), 1, -1, 0);
        primer0 = new Roller(new VictorSP(pwm_primer0), 1, -0.4, 0);
        primer1 = new Roller(new VictorSP(pwm_primer1), 1, -0.4, 0);
        m = false;
        autoalign = false;
        auto = false;
    }
    
    private static boolean auto      = false;
    private static boolean autoalign = false;
    private static double  back      = 0;
                                     
    /** Update inputs and outputs of the launcher system. */
    public static void update( ) {
        if ( !m ) //
            if ( ! ( DriveStation.b_y() < 0 && Arduino.getAngle() < -18 )
                    && ! ( DriveStation.b_y() > 0 && Arduino.getAngle() > 68 ) )
                linAct.set(-DriveStation.b_getThrottle() * 0.5 * DriveStation.b_y());
        if ( DriveStation.b_getKey(-1) )
            TimerSystem.execute(launch);
        if ( DriveStation.b_getKey(bind_ctr_intake) ) {
            roller.backwards();
            if ( Snoopr.getDio()[0] ) {
                primer0.backwards();
                primer1.backwards();
                back = System.nanoTime() + 5e8;
            } else {
                if ( back < System.nanoTime() ) {
                    primer0.zero();
                    primer1.zero();
                } else {
                    primer0.backwards();
                    primer1.backwards();
                }
            }
        } else if ( DriveStation.b_getKey(bind_ctr_outtake) ) {
            roller.forward();
            primer0.forward();
            primer1.forward();
        } else if ( DriveStation.b_getKey(bind_ctr_setup_ball) ) {
            primer0.interpolate(.75f);
            primer1.interpolate(.25f);
        } else if ( !auto ) {
            primer0.zero();
            primer1.zero();
            roller.zero();
        }
        if ( ( DriveStation.b_getKey(5) || autoalign ) && Arduino.isTargetThere() && !DriveStation.b_getKey(6) ) {
            m = !Arduino.useTegra(linAct, 0.4, 0.7);
            autoalign = m;
        } else {
            autoalign = false;
            Arduino.endTegra();
            m = false;
        }
        if ( DriveStation.b_getKey(11) ) {
            System.out.println(Arduino.angle(linAct, 43.37, .7));
        } else {
            Arduino.end();
        }
    }
    
    /** {@link #launch launch} is in the process of firing */
    private static boolean isFiring       = false;
    /** {@link #launch launch} has finished firing */
    private static boolean finishedFiring = false;
                                          
    /** Firing sequence utility method for the primer and roller */
    public static boolean fire( ) {
        if ( finishedFiring ) {
            finishedFiring = false;
            return true;
        }
        if ( !isFiring ) {
            TimerSystem.execute(auto_launch);
        }
        TimerSystem.update();
        return false;
    }
    
    /** Reset firing sequence utility method private variables */
    public static void resetAuto( ) {
        isFiring = false;
        finishedFiring = false;
    }
    
    private static Time launch      = new Time() {                                                                                                                                               // Timed
                                                                                                                                                                                                 // sequence
                                                                                                                                                                                                 // for
                                                                                                                                                                                                 // launching
                                                                                                                                                                                                 // the
                                                                                                                                                                                                 // ball
                                        @Override
                                        public boolean run( long time ) {
                                            if ( time > 1e9 || DriveStation.b_getKey(6) ) {                                                             // After
                                                                                                                                                        // 1
                                                                                                                                                        // second
                                                primer0.zero();
                                                primer1.zero();
                                                roller.zero();
                                                auto = false;
                                                return true;
                                            } else if ( time > .7e9 ) {                                                                                                     // If
                                                                                                                                                                            // not
                                                                                                                                                                            // after
                                                                                                                                                                            // 1
                                                                                                                                                                            // second,
                                                                                                                                                                            // after
                                                                                                                                                                            // 0.7
                                                                                                                                                                            // seconds
                                                primer0.forward();
                                                primer1.forward();
                                            } else {                                                                                                                                           // If
                                                                                                                                                                                               // not
                                                                                                                                                                                               // after
                                                                                                                                                                                               // 0.7
                                                                                                                                                                                               // seconds
                                                primer0.zero();
                                                primer1.zero();
                                            }
                                            auto = true;
                                            roller.forward();
                                            return false;
                                        }
                                    };
    private static Time auto_launch = new Time() {                                                                                                                                               // Timed
                                                                                                                                                                                                 // sequence
                                                                                                                                                                                                 // for
                                                                                                                                                                                                 // launching
                                                                                                                                                                                                 // the
                                                                                                                                                                                                 // ball
                                                                                                                                                                                                 // during
                                                                                                                                                                                                 // autonomous
                                        @Override
                                        public boolean run( long time ) {
                                            finishedFiring = false;
                                            isFiring = true;
                                            if ( time > 1e9 ) {
                                                primer0.zero();
                                                primer1.zero();
                                                roller.zero();
                                                auto = false;
                                                finishedFiring = true;
                                                isFiring = false;
                                                return true;
                                            } else if ( time > .7e9 ) {
                                                primer0.forward();
                                                primer1.forward();
                                            } else {
                                                primer0.zero();
                                                primer1.zero();
                                            }
                                            auto = true;
                                            roller.forward();
                                            return false;
                                        }
                                    };
}
