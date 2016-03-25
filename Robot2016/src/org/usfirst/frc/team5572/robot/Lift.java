package org.usfirst.frc.team5572.robot;

import org.usfirst.frc.team5572.robot.DriveStation;
import org.usfirst.frc.team5572.robot.Snoopr;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import static org.usfirst.frc.team5572.robot.Configuration.*;

public class Lift {

	private static DoubleSolenoid platform;
	private static SpeedController sc;

	public static void init() {
		platform = new DoubleSolenoid(12, sol_lift_primer_f, sol_lift_primer_r);
		sc = new VictorSP(0);
		platform.set(Value.kForward);
	}

	private static boolean cancelPressed = false;

	public static void update(boolean test) {
		// if(!isEndGame) return;
		if (DriveStation.a_getKey(11) && !cancelPressed) {
			// Engage/Disengage
			platform.set(platform.get() == Value.kForward ? Value.kReverse : Value.kForward);
			cancelPressed = true;
		} else if (!DriveStation.a_getKey(11)) {
			cancelPressed = false;
		}
		if (DriveStation.a_getKey(10)) {
			sc.set(-.8);
		} else if ((DriveStation.a_getKey(9) && Snoopr.getDio()[4])) {
			sc.set(0.8);
		} else {
			sc.set(0);
		}
		if (!Snoopr.getDio()[3] && !test) {
			sc.set(0);
		}
	}

}
