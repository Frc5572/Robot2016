
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.*;
import static org.usfirst.frc.team5572.util.NumberUtils.*;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;



public class DriveTrain {
    private static final int[]       leftCIMs  = pwm_wheel_l;        // PWM
                                                                                        // Channels
    private static final int[]       rightCIMs = pwm_wheel_r;        // PWM
                                                                                        // Channels
    private static SpeedController[] left      = new SpeedController[leftCIMs.length];
    private static SpeedController[] right     = new SpeedController[rightCIMs.length];
                                               
    /**
     * Initialization method for drive train components. Must be called before
     * any other method in DriveTrain
     */
    public static void init( ) {
        for ( int i = 0; i < leftCIMs.length; i++ ) {
            left[i] = new VictorSP(leftCIMs[i]);
        }
        for ( int i = 0; i < rightCIMs.length; i++ ) {
            right[i] = new VictorSP(rightCIMs[i]);
        }
    }
    
    /**
     * Lock is checked every tick. If it is false, the teleop method will not
     * check for client input
     */
    public static boolean lock = false;
    
    /**
     * Tick method
     */
    public static void teleop( ) {
        if ( !lock )
            drive();
    }
    
    /** Input handling */
    private static void drive( ) {
        double k = DriveStation.a_getThrottle();
        double y = k * DriveStation.a_y();
        double x = k * DriveStation.a_x();
        double l, r;
        x = signum(x) * ( x * x );
        y = signum(y) * ( y * y );
        if ( signum(x) == signum(y) ) {
            r = signum(y) * max(y * signum(y), x * signum(x));
            l = y - x;
        } else {
            l = signum(y) * max(y * signum(y), x * signum(x));
            r = y + x;
        }
        drivelr(l, r);
    }
    
    /** Single tick drive using left and right values */
    public static void drivelr( double l, double r ) {
        for ( int i = 0; i < left.length; i++ ) {
            left[i].set(l);
        }
        for ( int i = 0; i < right.length; i++ ) {
            right[i].set(r);
        }
    }
    
    /** Single tick drive using forward and curve values */
    public static void drive( double o, double c ) {
        double x = o;
        double y = c;
        double l, r;
        if ( y >= 0.0 ) {
            y = ( y * y );
        } else {
            y = - ( y * y );
        }
        if ( x >= 0.0 ) {
            x = ( x * x );
        } else {
            x = - ( x * x );
        }
        if ( signum(x) == signum(y) ) {
            r = signum(y) * max(y * signum(y), x * signum(x));
            l = y - x;
        } else {
            l = signum(y) * max(y * signum(y), x * signum(x));
            r = y + x;
        }
        drivelr(l, r);
    }
}