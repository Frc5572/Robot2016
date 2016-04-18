
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.*;

import org.usfirst.frc.team5572.util.Roller;
import org.usfirst.frc.team5572.util.TimerSystem;
import org.usfirst.frc.team5572.util.TimerSystem.Time;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;



public class Launcher {
    public static SpeedController linAct;
    private static Roller         primer, roller;
    public static boolean         m = false;
                                    
    public static void init( ) {
        linAct = new CANTalon(7);
        roller = new Roller(new CANTalon(can_wheel_intake), 1, -1, 0);
        primer = new Roller(new VictorSP(pwm_primer), 1, -1, 0);
    }
    
    private static boolean auto = false;
    
    public static void update( ) {
        if ( !m )
            linAct.set(-DriveStation.b_getThrottle() * 0.5 * DriveStation.b_y());
        if ( DriveStation.b_getKey(-1) )
            TimerSystem.execute(launch);
        if ( DriveStation.b_getKey(bind_ctr_intake) ) {
            roller.backwards();
            if ( Snoopr.getDio()[0] ) {
                primer.backwards();
            } else {
                primer.zero();
            }
        } else if ( DriveStation.b_getKey(bind_ctr_outtake) ) {
            roller.forward();
            primer.forward();
        } else if ( !auto ) {
            primer.zero();
            roller.zero();
        }
        if(DriveStation.b_getKey(11)){
            Arduino.angle(linAct, 50, .7);
        }else{
            Arduino.end();
        }
        
    }
    
    private static Time launch = new Time() {
        @Override
        public boolean run( long time ) {
            if ( time > 2.5e9 ) {
                primer.zero();
                roller.zero();
                auto = false;
                return true;
            } else if ( time > .7e9 ) {
                primer.forward();
            } else {
                primer.zero();
            }
            auto = true;
            roller.forward();
            return false;
        }
    };
}
