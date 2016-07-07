
package org.usfirst.frc.team5572.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.FlipAxis;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class DriveStation {
    private static int       session;
    private static Image     frame;
    private static final int joystick0 = 0;
    private static final int joystick1 = 1;
    private static Joystick  stick0;
    private static Joystick  stick1;
                                       
    /**Initialize input*/
    public static void init( ) {
        stick0 = new Joystick(joystick0);
        stick1 = new Joystick(joystick1);
        try {
            frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
            session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            NIVision.IMAQdxConfigureGrab(session);
        } catch ( Exception e ) {
            System.out.println("No Camera");
        }
        if ( Conf.drawl_smartDash ) {
            SmartDashboard.putNumber("line0_x0", /* 278 */ 0);
            SmartDashboard.putNumber("line0_x1", /* 392 */ 0);
            SmartDashboard.putNumber("line1_x0", /* 295 */ 0);
            SmartDashboard.putNumber("line1_x1", /* 377 */ 0);
            SmartDashboard.putNumber("line2_x0", /* 295 */ 0);
            SmartDashboard.putNumber("line2_x1", /* 377 */ 0);
            SmartDashboard.putNumber("line3_x0", /* 295 */ 0);
            SmartDashboard.putNumber("line3_x1", /* 377 */ 0);
            SmartDashboard.putNumber("line0_y", /* 250 */ 0);
            SmartDashboard.putNumber("line1_y", /* 30 */ 0);
            SmartDashboard.putNumber("line2_y", /* 40 */ 0);
            SmartDashboard.putNumber("line3_y", /* 280 */ 0);
        }
    }
    
    /**Get key from controller {@value #joystick0}*/
    public static boolean a_getKey( int m ) {
        if ( m == -1 )
            return stick0.getTrigger();
        return stick0.getRawButton(m);
    }

    /**Get key from controller {@value #joystick1}*/
    public static boolean b_getKey( int m ) {
        if ( m == -1 )
            return stick1.getTrigger();
        return stick1.getRawButton(m);
    }

    /**Get axis x from controller {@value #joystick0}*/
    public static double a_x( ) {
        return stick0.getX();
    }

    /**Get axis x from controller {@value #joystick1}*/
    public static double b_x( ) {
        return stick1.getX();
    }

    /**Get axis y from controller {@value #joystick0}*/
    public static double a_y( ) {
        return stick0.getY();
    }

    /**Get axis y from controller {@value #joystick1}*/
    public static double b_y( ) {
        return stick1.getY();
    }

    /**Get throttle from controller {@value #joystick0}*/
    public static double a_getThrottle( ) {
        return ( -stick0.getZ() + 1 ) / 2;
    }

    /**Get throttle from controller {@value #joystick1} (marked as the z-axis)*/
    public static double b_getThrottle( ) {
        return ( -stick1.getThrottle() + 1 ) / 2;
    }
    
    /**Establish camera-client connection*/
    public static void beginCamera( ) {
        try {
            NIVision.IMAQdxStartAcquisition(session);
            CameraServer.getInstance().setQuality(50);
        } catch ( Exception e ) {}
    }
    
    /**End camera-client connection*/
    public static void endCamera( ) {
        try {
            NIVision.IMAQdxStopAcquisition(session);
        } catch ( Exception e ) {}
    }
    
    /**Update camera-client connection*/
    public static void updateCamera( ) {
        try {
            NIVision.IMAQdxGrab(session, frame, 1);
            NIVision.imaqTranspose(frame, frame);
            NIVision.imaqFlip(frame, frame, FlipAxis.HORIZONTAL_AXIS);
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) ( ( NIVision.imaqGetImageSize(frame).height ) * 0.05 ),
                            NIVision.imaqGetImageSize(frame).width / 2 - 25,
                            ( int ) ( ( NIVision.imaqGetImageSize(frame).height ) * 0.9 ), 4),
                    DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0x00ff00);
            CameraServer.getInstance().setImage(frame);
            if ( a_getKey(10) ) {
                CameraServer.getInstance().setQuality(CameraServer.getInstance().getQuality() - 1);
            }
            if ( a_getKey(11) ) {
                CameraServer.getInstance().setQuality(CameraServer.getInstance().getQuality() + 1);
            }
        } catch ( Exception e ) {
            DriverStation.reportError("Cameracrash", false);
            e.printStackTrace();
        }
        SmartDashboard.putNumber("Camera Quality", CameraServer.getInstance().getQuality());
    }
}
