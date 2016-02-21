package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class Jetson {
	
	private static AnalogInput ai0;
	private static DigitalInput dio0;
	private static DigitalInput dio1;
	private static DigitalOutput dio2;
	
	public static void init(){
		ai0 = new AnalogInput(0);
		dio0 = new DigitalInput(2);
		dio0 = new DigitalInput(2);
		dio2 = new DigitalOutput(2);
	}
	
	private static double calcAngle(double d){
		return d;
	}
	
	public static double getDesiredAngle(){
		dio2.set(true);
		if(!dio1.get() || dio0.get()){
			return -1;
		}
		double dist = ai0.getVoltage();
		
		return calcAngle(dist);
	}
	
	public static double getDesiredYaw(){
		dio2.set(true);
		if(!dio0.get() || dio1.get()){
			return -1;
		}
		return ai0.getVoltage()/20;
	}
	
}
