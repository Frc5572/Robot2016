package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import static org.usfirst.frc.team5572.robot.Configuration.*;

public class Launcher {

	private static CANTalon intake;
	private static DoubleSolenoid primer;

	public static void init() {
		intake = new CANTalon(can_wheel_intake);
		primer = new DoubleSolenoid(def_sol_mod, sol_primer_f, sol_primer_r);
	}

	private static Value rollingIn = Value.kOff;

	private static int time = 0;

	private static int shooting = 0;

	public static void update() {
		if (rollingIn.equals(Value.kForward)) {
			intake.set(1);
		} else if (rollingIn.equals(Value.kReverse)) {
			intake.set(-.5);
		} else {
			intake.set(0);
		}

		if (time > 0) {
			time--;
			return;
		}
		if (shooting != 0) {
			if (shooting == 1) {
				primer.set(Value.kForward);
				shooting = 2;
				time = def_primer_sleep;
			} else {
				primer.set(Value.kReverse);
				shooting = 0;
			}
			return;
		} else {
			if (DriveStation.b_getKey(2)) {
				rollingIn = Value.kForward;
				shooting = 1;
				time = def_roll_sleep;
			}
		}
	}

}
