package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.Joystick;

public class DriveStation {
	
	private static final int joystick0 = 0;
	private static final int joystick1 = 1;
	
	private static Joystick stick0;
	private static Joystick stick1;
	
	public static void init(){
		stick0 = new Joystick(joystick0);
		stick1 = new Joystick(joystick1);
	}
	
	public static boolean a_getKey(int m){
		if(m == -1)
			return stick0.getTrigger();
		return stick0.getRawButton(m);
	}
	
	public static boolean b_getKey(int m){
		if(m == -1)
			return stick1.getTrigger();
		return stick1.getRawButton(m);
	}
	
	public static double a_x(){
		return stick0.getX();
	}
	
	public static double b_x(){
		return stick1.getX();
	}
	
	public static double a_y(){
		return stick0.getY();
	}
	
	public static double b_y(){
		return stick1.getY();
	}
	
	public static double a_getThrottle(){
		return (-stick0.getZ() + 1) / 2;
	}
	
	public static double b_getThrottle(){
		return (-stick0.getThrottle() + 1) / 2;
	}
	
}
