
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.bind_ctr_intake;
import static org.usfirst.frc.team5572.robot.Conf.bind_ctr_outtake;
import static org.usfirst.frc.team5572.robot.Conf.*;

import org.usfirst.frc.team5572.util.Roller;
import org.usfirst.frc.team5572.util.TimerSystem;
import org.usfirst.frc.team5572.util.TimerSystem.Time;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistributionPanel;



public class Launcher {
    public static SpeedController linAct;
    private static Roller         roller;
    private static DoubleSolenoid primer;
    public static boolean         m = false;
                                    
    /**
     * Initialization method for launcher components. Must be called before any
     * other method in Launcher
     */
    public static void init( ) {
        linAct = new CANTalon(can_launcher);
        roller = new Roller(new CANTalon(can_wheel_intake), -1, 0.4, 0);
        primer = new DoubleSolenoid(12, sol_primer_r, sol_primer_f);
        m = false;
        PowerDistributionPanel.setDefaultSolenoidModule(12);
    }
    
    /** Update inputs and outputs of the launcher system. */
    public static void update( ) {
        if ( !m ) {
            linAct.set(0.5 * DriveStation.b_y());
            if ( DriveStation.b_getKey(-1) )
                primer.set(Value.kForward);
            else
                primer.set(Value.kReverse);
            if ( DriveStation.b_getKey(bind_ctr_intake) ) {
                roller.backwards();
            } else if ( DriveStation.b_getKey(bind_ctr_outtake) ) {
                roller.forward_interpolate(DriveStation.b_getThrottle());
            } else {
                roller.zero();
            }
        }
    }
    
    private static Time launch = new Time() {
        @Override
        public boolean run( long time ) {
            roller.forward();
            if ( time >= 5e8 )
                primer.set(Value.kForward);
            if ( time >= 1e9 ) {
                primer.set(Value.kReverse);
                m = false;
                roller.zero();
                return true;
            }
            return false;
        }
    };
}
