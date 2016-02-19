package org.usfirst.frc.team5572.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveStation {

	private static int session;
	private static Image frame;
	private static NIVision.Rect rect;

	private static final int joystick0 = 0;
	private static final int joystick1 = 1;

	private static Joystick stick0;
	private static Joystick stick1;

	public static void init() {
		stick0 = new Joystick(joystick0);
		stick1 = new Joystick(joystick1);
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);
	}

	public static boolean a_getKey(int m) {
		if (m == -1)
			return stick0.getTrigger();
		return stick0.getRawButton(m);
	}

	public static boolean b_getKey(int m) {
		if (m == -1)
			return stick1.getTrigger();
		return stick1.getRawButton(m);
	}

	public static double a_x() {
		return stick0.getX();
	}

	public static double b_x() {
		return stick1.getX();
	}

	public static double a_y() {
		return stick0.getY();
	}

	public static double b_y() {
		return stick1.getY();
	}

	public static double a_getThrottle() {
		return (-stick0.getZ() + 1) / 2;
	}

	public static double b_getThrottle() {
		return (-stick0.getThrottle() + 1) / 2;
	}

	public static void beginCamera() {
		NIVision.IMAQdxStartAcquisition(session);
		rect = new NIVision.Rect(10, 10, 100, 100);
		CameraServer.getInstance().setQuality(10);
	}

	public static void endCamera() {
		NIVision.IMAQdxStopAcquisition(session);
	}

	public static void updateCamera() {
		NIVision.IMAQdxGrab(session, frame, 1);
		CameraServer.getInstance().setImage(frame);
		if (a_getKey(7)) {
			CameraServer.getInstance().setQuality(CameraServer.getInstance().getQuality() - 1);
		}
		if (a_getKey(6)) {
			CameraServer.getInstance().setQuality(CameraServer.getInstance().getQuality() + 1);
		}
		SmartDashboard.putNumber("Camera Quality", CameraServer.getInstance().getQuality());
	}

}
