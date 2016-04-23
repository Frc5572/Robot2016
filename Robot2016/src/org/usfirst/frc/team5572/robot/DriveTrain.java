
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.*;
import static org.usfirst.frc.team5572.util.NumberUtils.*;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class DriveTrain {
    private static final int[]       leftCIMs  = { pwm_wheel_bl, pwm_wheel_fl };        // PWM
                                                                                        // Channels
    private static final int[]       rightCIMs = { pwm_wheel_br, pwm_wheel_fr };        // PWM
                                                                                        // Channels
    private static SpeedController[] left      = new SpeedController[leftCIMs.length];
    private static SpeedController[] right     = new SpeedController[rightCIMs.length];
                                               
    public static void init( ) {
        for ( int i = 0; i < leftCIMs.length; i++ ) {
            left[i] = new VictorSP(leftCIMs[i]);
        }
        for ( int i = 0; i < rightCIMs.length; i++ ) {
            right[i] = new VictorSP(rightCIMs[i]);
        }
    }
    
    public static boolean lock = false;
    
    public static void teleop( ) {
        if ( !lock )
            drive();
        feedData();
    }
    
    public static void feedData( ) {
        SmartDashboard.putNumber("Left Encoders", Snoopr.getLeftEncoderRaw());
        SmartDashboard.putNumber("Right Encoders", Snoopr.getRightEncoderRaw());
        SmartDashboard.putNumber("Yaw", Snoopr.getTotalYaw());
    }
    
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
    
    public static void drivelr( double l, double r ) {
        for ( int i = 0; i < left.length; i++ ) {
            left[i].set(-l);
        }
        for ( int i = 0; i < right.length; i++ ) {
            right[i].set(r);
        }
    }
    
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
    
    public static void driveStraightReset( ) {
        Snoopr.resetEncoders();
    }
    
    public static boolean driveStraight( double speed, double thresh, double dist_in ) {
        double dist = dist_in * 11.0107526882/* * .965517 */;
        if ( Snoopr.getRightEncoderRaw() <= dist - thresh ) {
            double delta = Snoopr.getRightEncoderRaw() - Snoopr.getLeftEncoderRaw();
            double modDelta = delta / 100d;
            double speed2 = clamp(0.002 * ( dist - Snoopr.getRightEncoderRaw() ), .2, speed);
            DriveTrain.drivelr(-speed2, -speed2 + modDelta);
            return false;
        }
        return true;
    }
    
    public static void resetGlobalAngle( ) {
        Snoopr.zero();
    }
    
    private static int accum = 0;
    
    public static boolean setGlobalAngle( double angle, double thresh, double min_power ) {
        double curr = Snoopr.getTotalYaw();
        while ( curr > 180 )
            curr -= 360;
        while ( curr < -180 )
            curr += 360;
        if ( Math.abs(angle - curr) <= thresh ) {
            System.out.print(angle - curr + "  ");
            accum++;
            if ( accum > 300 )
                return true;
            turn(0);
        } else {
            accum = 0;
            if ( Math.abs(turn(signum(angle - curr) * ( 90 * ( min_power - 0.05 ) ) + angle - curr)) < min_power )
                return true;
        }
        return false;
    }
    
    public static double turn( double angle ) {
        while ( angle > 180 )
            angle -= 360;
        while ( angle < -180 )
            angle += 360;
        double val = clampMotor(angle / 180 + 0.05);
        System.out.println(val);
        drivelr(-val, val);
        return val;
    }
}