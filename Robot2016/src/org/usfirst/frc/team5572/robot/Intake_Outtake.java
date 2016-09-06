
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.bind_ctr_intake;
import static org.usfirst.frc.team5572.robot.Conf.bind_ctr_outtake;
import static org.usfirst.frc.team5572.robot.Conf.bind_ctr_setup_ball;
import static org.usfirst.frc.team5572.robot.Conf.can_wheel_intake;
import static org.usfirst.frc.team5572.robot.Conf.pwm_primer0;
import static org.usfirst.frc.team5572.robot.Conf.pwm_primer1;

import java.util.Timer;

import org.usfirst.frc.team5572.util.Roller;
import org.usfirst.frc.team5572.util.TimerSystem;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.VictorSP;



public class Intake_Outtake {
    private static Roller primer0, primer1, roller; // Top wheel, Bottom
    // wheel, flywheels
    
    public static void init( ) {
        roller = new Roller(new CANTalon(can_wheel_intake), 1, -1, 0);
        primer0 = new Roller(new VictorSP(pwm_primer0), 1, -0.4, 0);
        primer1 = new Roller(new VictorSP(pwm_primer1), 1, -0.4, 0);
    }
    
    private static double back = 0, nback = 0;
    
    public static void update( ) {
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
        } else if ( DriveStation.b_getKey(-1) ) {
            nback = System.nanoTime() + 5e8;
        } else if ( nback > System.nanoTime() ) {
            roller.forward();
            primer0.forward();
            primer1.forward();
        } else if ( DriveStation.b_getKey(bind_ctr_setup_ball) ) {
            primer0.interpolate(.75f);
            primer1.interpolate(.25f);
        } else {
            roller.zero();
            primer0.zero();
            primer1.zero();
        }
    }
    
    public static void outtake(){
        TimerSystem.execute(outtake);
    }
    
    private static TimerSystem.Time outtake = new TimerSystem.Time() {
        @Override
        public boolean run( long time ) {
            if(time < 5e8){
                roller.forward();
                primer0.forward();
                primer1.forward();
                return false;
            }else{
                roller.zero();
                primer0.zero();
                primer1.zero();
                return true;
            }
        }
    };
}
