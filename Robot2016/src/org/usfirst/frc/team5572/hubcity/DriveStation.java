
package org.usfirst.frc.team5572.hubcity;

import static org.usfirst.frc.team5572.hubcity.Configuration.def_use_switchboard;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Rect;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class DriveStation {
    private static int       session1;
    private static int       session;
    private static Image     frame;
    private static Image     frame1;
    private static final int joystick0 = 0;
    private static final int joystick1 = 1;
    private static final int joystick2 = 2;
    private static Joystick  stick0;
    private static Joystick  stick1;
    private static Joystick  stick2;
    private static boolean   use2Cam   = false;
                                       
    public static void init( ) {
        stick0 = new Joystick(joystick0);
        if ( !def_use_switchboard )
            stick1 = new Joystick(joystick1);
        else
            stick2 = new Joystick(joystick2);
        try {
            frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
            if ( use2Cam )
                frame1 = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
            session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            if ( use2Cam )
                session1 = NIVision.IMAQdxOpenCamera("cam1",
                        NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            NIVision.IMAQdxConfigureGrab(session);
        } catch ( Exception e ) {
            System.out.println("No Camera");
        }
        SmartDashboard.putNumber("line_x0", 186);
        SmartDashboard.putNumber("line_x1", 387);
        SmartDashboard.putNumber("line0_y", 183);
        SmartDashboard.putNumber("line1_y", 30);
        SmartDashboard.putNumber("line2_y", 40);
        SmartDashboard.putNumber("line3_y", 280);
    }
    
    public static boolean a_getKey( int m ) {
        if ( m == -1 )
            return stick0.getTrigger();
        return stick0.getRawButton(m);
    }
    
    public static boolean b_getKey( int m ) {
        if ( m == -1 )
            return stick1.getTrigger();
        return stick1.getRawButton(m);
    }
    
    public static double a_x( ) {
        return stick0.getX();
    }
    
    public static double b_x( ) {
        if ( def_use_switchboard )
            return 0;
        return stick1.getX();
    }
    
    public static double a_y( ) {
        return stick0.getY();
    }
    
    public static double b_y( ) {
        if ( def_use_switchboard )
            return 0;
        return stick1.getY();
    }
    
    public static double a_getThrottle( ) {
        return ( -stick0.getZ() + 1 ) / 2;
    }
    
    public static double b_getThrottle( ) {
        if ( def_use_switchboard )
            return 0;
        return ( -stick1.getThrottle() + 1 ) / 2;
    }
    
    public static double getKnob( int axis ) {
        if ( !def_use_switchboard )
            return 0;
        return stick2.getRawAxis(axis);
    }
    
    public static boolean getSwitch( int a ) {
        if ( !def_use_switchboard )
            return false;
        return stick2.getRawButton(a);
    }
    
    public static void out( int a, boolean b ) {
        if ( !def_use_switchboard )
            return;
        stick2.setOutput(a, b);
    }
    
    public static void beginCamera( ) {
        try {
            NIVision.IMAQdxStartAcquisition(session);
            if ( use2Cam )
                NIVision.IMAQdxStartAcquisition(session1);
            CameraServer.getInstance().setQuality(Configuration.def_camera_quality);
        } catch ( Exception e ) {}
    }
    
    public static void endCamera( ) {
        try {
            if ( use2Cam )
                NIVision.IMAQdxStopAcquisition(session1);
            NIVision.IMAQdxStopAcquisition(session);
        } catch ( Exception e ) {}
    }
    
    public static void updateCamera( ) {
        try {
            NIVision.IMAQdxGrab(session, frame, 1);
            if ( use2Cam )
                NIVision.IMAQdxGrab(session1, frame1, 1);
            int x0 = ( int ) SmartDashboard.getNumber("line_x0");
            int x1 = ( int ) SmartDashboard.getNumber("line_x1");
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) SmartDashboard.getNumber("line0_y"), x0, 4, x1 - x0), DrawMode.PAINT_VALUE,
                    ShapeMode.SHAPE_RECT, 0x0000ff);
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) SmartDashboard.getNumber("line1_y"), x0, 4, x1 - x0), DrawMode.PAINT_VALUE,
                    ShapeMode.SHAPE_RECT, 0x00ff00);
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) SmartDashboard.getNumber("line2_y"), x0, 4, x1 - x0), DrawMode.PAINT_VALUE,
                    ShapeMode.SHAPE_RECT, 0xff0000);
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) SmartDashboard.getNumber("line3_y"), x0, 4, x1 - x0), DrawMode.PAINT_VALUE,
                    ShapeMode.SHAPE_RECT, 0xffff00);
            CameraServer.getInstance().setImage(frame);
            if ( a_getKey(10) ) {
                CameraServer.getInstance().setQuality(CameraServer.getInstance().getQuality() - 1);
            }
            if ( a_getKey(11) ) {
                CameraServer.getInstance().setQuality(CameraServer.getInstance().getQuality() + 1);
            }
        } catch ( Exception e ) {}
        SmartDashboard.putNumber("Camera Quality", CameraServer.getInstance().getQuality());
    }
}
