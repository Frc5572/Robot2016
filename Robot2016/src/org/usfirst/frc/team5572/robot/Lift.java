package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Lift {
	
	private static DoubleSolenoid platform;
	private static DoubleSolenoid lift_engage;
	
	public static void init(){
		platform = new DoubleSolenoid(plat_forward, plat_reverse);
		lift_engage = new DoubleSolenoid(lift_forward, lift_forward);
	}
	
	private static boolean cancelPressed = false;
	
	public static void update(boolean isEndGame){
		if(!isEndGame) return;
		if (DriveStation.a_getKey(11) && !cancelPressed){
			// Engage/Disengage
			lift_engage.set(lift_engage.get() == Value.kForward ? Value.kReverse : Value.kForward);
			platform.set(platform.get() == Value.kForward ? Value.kReverse : Value.kForward);
			cancelPressed = true;
		} else if(!DriveStation.a_getKey(11)) {
			cancelPressed = false;
		}
	}
	
}
