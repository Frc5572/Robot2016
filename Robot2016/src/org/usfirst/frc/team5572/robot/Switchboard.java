package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Switchboard{
	private static int moveArm=5, fireButton=7, autoSetbutton=8, cockButton=9, intakeButton=10, outtakeButton=11;
	private static int[] autoSelections=new int[]{1,2,3,4};
	public static Joystick joy;
	private static boolean autoAim=false;
	private static int manAim;
	public static void init(int jo){
		joy=new Joystick(jo);
	}
	public static double getAutoAngle(){
		double ret=0;
		for(int i=0;i<autoSelections.length;i++){
			if(joy.getRawButton(autoSelections[i])){
				ret=joy.getRawAxis(i);
				break;
			}
		}
		return ret;
	}
	public static boolean getFire(){
		return joy.getRawButton(fireButton);
	}
	public static boolean getAutoSetButton(){
		return joy.getRawButton(autoSetbutton);
	}
	public static boolean getCockButton(){
		return joy.getRawButton(cockButton);
	}
	public static boolean getMoveArmButton(){
		return joy.getRawButton(moveArm);
	}
	public static boolean getIntakeButton(){
		return joy.getRawButton(intakeButton);
	}
	public static boolean getOutTakeButton(){
		return joy.getRawButton(outtakeButton);
	}
	public static boolean isAutoAim(){
		return autoAim;
	}
	public static double getManAim(){
		return joy.getRawAxis(0);
	}
	public static void listen(){
		new Thread(new Runnable(){
			private boolean expierimental=false;

			public void run(){
				
				if(Switchboard.getFire()){
					newLauncher.fire();
				}else{
					newLauncher.cock(0);
				}
				if(Switchboard.getCockButton()){
					newLauncher.cock(1);
				}else{
					newLauncher.cock(0);
				}
				if(Switchboard.getOutTakeButton()){
					newLauncher.roll(1);
				}else{
					newLauncher.roll(0);
				}
				if(Switchboard.getIntakeButton()){
					newLauncher.roll(-1);
				}else{
					newLauncher.roll(0);
				}
				if(expierimental){
					newLauncher.lift(Switchboard.getManAim());
				}else{
					if(Switchboard.getMoveArmButton()){
						newLauncher.setSpeed(Switchboard.getManAim());
					}else{
						newLauncher.setSpeed(0);
					}
				}
				newLauncher.update();
			}
		}).run();
	}
}
