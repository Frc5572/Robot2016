
package org.usfirst.frc.team5572.robot;

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
    private static Joystick  stick0;
    private static Joystick  stick1;
    private static boolean   use2Cam   = false;
                                       
    public static void init( ) {
        stick0 = new Joystick(joystick0);
        stick1 = new Joystick(joystick1);
        try {
            frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
            if ( use2Cam )
                frame1 = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
            session = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            if ( use2Cam )
                session1 = NIVision.IMAQdxOpenCamera("cam0",
                        NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            NIVision.IMAQdxConfigureGrab(session);
        } catch ( Exception e ) {
            System.out.println("No Camera");
        }
        SmartDashboard.putNumber("line0_x0", 278);
        SmartDashboard.putNumber("line0_x1", 392);
        SmartDashboard.putNumber("line1_x0", 295);
        SmartDashboard.putNumber("line1_x1", 377);
        SmartDashboard.putNumber("line2_x0", 295);
        SmartDashboard.putNumber("line2_x1", 377);
        SmartDashboard.putNumber("line3_x0", 295);
        SmartDashboard.putNumber("line3_x1", 377);
        SmartDashboard.putNumber("line0_y", 250);
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
        return stick1.getX();
    }
    
    public static double a_y( ) {
        return stick0.getY();
    }
    
    public static double b_y( ) {
        return stick1.getY();
    }
    
    public static double a_getThrottle( ) {
        return ( -stick0.getZ() + 1 ) / 2;
    }
    
    public static double b_getThrottle( ) {
        return ( -stick1.getThrottle() + 1 ) / 2;
    }
    
    public static void beginCamera( ) {
        try {
            NIVision.IMAQdxStartAcquisition(session);
            if ( use2Cam )
                NIVision.IMAQdxStartAcquisition(session1);
            CameraServer.getInstance().setQuality(50);
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
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) SmartDashboard.getNumber("line0_y"), ( int ) SmartDashboard.getNumber("line0_x0"),
                            4,
                            ( int ) SmartDashboard.getNumber("line0_x1")
                                    - ( int ) SmartDashboard.getNumber("line0_x0")),
                    DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0x000ff);
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) SmartDashboard.getNumber("line1_y"), ( int ) SmartDashboard.getNumber("line1_x0"),
                            4,
                            ( int ) SmartDashboard.getNumber("line1_x1")
                                    - ( int ) SmartDashboard.getNumber("line1_x0")),
                    DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0x0ff00);
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) SmartDashboard.getNumber("line2_y"), ( int ) SmartDashboard.getNumber("line2_x0"),
                            4,
                            ( int ) SmartDashboard.getNumber("line2_x1")
                                    - ( int ) SmartDashboard.getNumber("line2_x0")),
                    DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0xff0000);
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(( int ) SmartDashboard.getNumber("line3_y"), ( int ) SmartDashboard.getNumber("line3_x0"),
                            4,
                            ( int ) SmartDashboard.getNumber("line3_x1")
                                    - ( int ) SmartDashboard.getNumber("line3_x0")),
                    DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0xffff00);
            NIVision.imaqDrawShapeOnImage(frame, frame,
                    new Rect(200, NIVision.imaqGetImageSize(frame).width / 2 - 2, 40, 4), DrawMode.PAINT_VALUE,
                    ShapeMode.SHAPE_RECT, 0x00ff00);
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
