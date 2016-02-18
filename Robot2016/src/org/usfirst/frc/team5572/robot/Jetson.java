package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Jetson {
	
	private static AnalogInput ai0;
	
	private static AnalogInput ai1;
	
	public static void init(){
		ai0 = new AnalogInput(0);
		ai1 = new AnalogInput(1);
	}
	
	public static int getAValue(){
		return ai0.getValue();
	}
	
	public static int getBValue(){
		return ai1.getValue();
	}
	
}
