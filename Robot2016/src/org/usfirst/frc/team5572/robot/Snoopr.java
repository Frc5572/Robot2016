
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.*;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SPI;



/** Handler for sensors */
public class Snoopr {
    private static AHRS         ahrs;                            // NAV-X
    private static Encoder      left, right;                     // Left and
                                                                 // right
                                                                 // encoders.
    private static DigitalInput liftBottom, liftTop, ballInPlace; // Sensors for
                                                                  // lift and
                                                                  // launcher
                                                                  // system
                                
    /**
     * Initialization method for sensors. Must be called before any other method
     * in Snoopr
     */
    public static void init( ) {
        ahrs = new AHRS(SPI.Port.kMXP); // Nav-x is on mxp
        ahrs.reset(); // set default offsets
        left = new Encoder(dio_enc_l_a, dio_enc_l_b, true, EncodingType.k4X);
        right = new Encoder(dio_enc_r_a, dio_enc_r_b, true, EncodingType.k4X);
        liftBottom = new DigitalInput(dio_lift_low);
        liftTop = new DigitalInput(dio_lift_top);
        ballInPlace = new DigitalInput(dio_bin);
    }
    
    /** Get global yaw. Reset with {@link #zero() Snoopr.zero()} */
    public static double getTotalYaw( ) {
        return ahrs.getAngle();
    }
    
    /** Get current digital input values. */
    public static boolean[] getDio( ) {
        return new boolean[] { ballInPlace.get(), false, false, liftBottom.get(), liftTop.get() };
    }
    
    /**
     * Reset global yaw. After performing this, {@link #getTotalYaw()
     * Snoopr.getTotalYaw()} should return 0 until the robots yaw changes
     */
    public static void zero( ) {
        ahrs.zeroYaw();
    }
    
    /** Returns raw value from left wheel encoder */
    public static double getLeftEncoderRaw( ) {
        return left.get();
    }
    
    /** Returns raw value from right wheel encoder */
    public static double getRightEncoderRaw( ) {
        return right.get() * 1.08; // Modified due to encoder inconsistencies.
    }
    
    /**
     * Resets encoder values. AFter performing this {@link #getLeftEncoderRaw()
     * Snoopr.getLeftEncoderRaw()} & {@link #getRightEncoderRaw()
     * Snoopr.getRightEncoderRaw()} should return 0 until the drivetrain moves.
     */
    public static void resetEncoders( ) {
        right.reset();
        left.reset();
    }
}
