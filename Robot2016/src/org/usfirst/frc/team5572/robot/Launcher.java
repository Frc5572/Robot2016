package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;

import static org.usfirst.frc.team5572.robot.Conf.*;

public class Launcher {

	private static Compressor comp;
	private static DoubleSolenoid grabber;
	private static DoubleSolenoid pull;
	private static DoubleSolenoid lock;
	private static DoubleSolenoid unknown;
	/** Talon for cannon rotation */
	private static SpeedController sc;
	/** Talon for intake rotation */
	private static SpeedController roll;

	public static void init() {
		PowerDistributionPanel.setDefaultSolenoidModule(12);
		comp = new Compressor(12);
		grabber = new DoubleSolenoid(grabber_forward, grabber_reverse);
		pull = new DoubleSolenoid(pull_forward, pull_reverse);
		lock = new DoubleSolenoid(lock_forward, lock_reverse);
		unknown = new DoubleSolenoid(unknown_forward, unknown_reverse);
		sc = new CANTalon(1); // Talon for cannon rotation
		roll = new CANTalon(3); // Talon for intake rotation
	}

	/*public static double getComp() {
		return comp.getCompressorCurrent();
	}

	public static void lock() {
		lock.set(Value.kReverse);
	}

	public static void release() {
		lock.set(Value.kForward);
	}
*/
	public static void openClaw() {
		grabber.set(Value.kForward);
	}

	public static void closeClaw() {
		grabber.set(Value.kReverse);
	}
/*
	public static void _cock() {
		pull.set(Value.kReverse);
	}

	public static void unknownOpen() {
		unknown.set(Value.kReverse);
	}

	public static void unknownClose() {
		unknown.set(Value.kForward);
	}

	public static void uncock() {
		pull.set(Value.kForward);
	}
*/
	
	public static void roll() {
		roll.set(-.75);
	}

	public static void unroll() {
		roll.set(0);
	}

	private static void snoop() {
		SmartDashboard.putNumber("Angle", Snoopr.getAngle());
		SmartDashboard.putNumber("Anglev", Snoopr.getV());
		SmartDashboard.putString("Dios", m(Snoopr.getDio()));
		SmartDashboard.putString("run", run + "");
		SmartDashboard.putString("isShooting", isShooting + "");
		SmartDashboard.putString("cancel", isCancelPressed + "");
		SmartDashboard.putNumber("check", check ? 1 : 0);
		SmartDashboard.putString("time", time + "");
		SmartDashboard.putString("lock", lock.get() == Value.kOff ? "Off" : (lock.get() == Value.kForward ? "Forward" : "Reverse"));
		SmartDashboard.putString("grabber", grabber.get() == Value.kOff ? "Off" : (grabber.get() == Value.kForward ? "Forward" : "Reverse"));
		SmartDashboard.putString("puller", pull.get() == Value.kOff ? "Off" : (pull.get() == Value.kForward ? "Forward" : "Reverse"));
	}

	private static int time = 0;

	private static boolean run = false;
	private static int isShooting = 0;
	private static boolean isCancelPressed = false, check = true;

	public static void end() {
		begin();
	}

	public static void begin() {
		lock.set(Value.kForward); // unlock trigger
		grabber.set(Value.kReverse); // close grabber
		pull.set(Value.kForward); // uncock
		run = false;
	}

	public static void update() {
		changeAngle(DriveStation.b_y());
		snoop();
		if (DriveStation.b_getKey(button_cancel) && !isCancelPressed) {
			run = true;
			isCancelPressed = true;
		}
		if (!DriveStation.b_getKey(button_cancel)) {
			isCancelPressed = false;
		}
		if (time > 0) {
			time--;
			SmartDashboard.putString("Return State","A");
			return;
		}
		if (DriveStation.b_getKey(button_oclaw)) {
			openClaw();
		}
		if (DriveStation.b_getKey(button_cclaw)) {
			closeClaw();
		}
		if (DriveStation.b_getKey(button_roll)) {
			roll.set(-rollSpeed);
		} else if (DriveStation.b_getKey(button_unroll)) {
			roll.set(rollSpeed);
		} else {
			roll.set(0);
		}
		// Cock, lock, grab
		boolean dios[] = Snoopr.getDio();
		if (isShooting != 0) {
			if (isShooting == 1) {
				grabber.set(Value.kForward);
				if (!dios[2]) {
					isShooting = 2;
					time = launcherWait;
				}
				SmartDashboard.putString("Return State","B");
				return;
			}
			isShooting = 0;
			lock.set(Value.kForward);
			time = resetWait;
			run = false;
		} else {
			if (DriveStation.b_getKey(button_shoot) && dios[1]) {
				isShooting = 1;
				check = true;
				SmartDashboard.putString("Return State","C");
				return;
			}
			if (!run) {
				SmartDashboard.putString("Return State","D");
				return;
			}
			if (!comp.getPressureSwitchValue() && check) {
				SmartDashboard.putString("Return State","E");
				return;
			}
			check = false;
			if (dios[1]) {
				pull.set(Value.kForward);
			}else if (!dios[0]) {
				pull.set(Value.kReverse);
				SmartDashboard.putString("Return State","F");
				return;
			}
			grabber.set(Value.kReverse);
			lock.set(Value.kReverse);
			SmartDashboard.putString("Return State","G");
		}
	}

	private static String m(boolean[] m) {
		String k = "[";
		for (int i = 0; i < m.length; i++) {
			k += (m[i] ? 1 : 0) + ",";
		}
		return k.substring(0, k.length() - 1) + "]";
	}

	public static void changeAngle(double value) {
		if (value > 0.05)
			Snoopr.markAngleDirty();
		value = limit(value * -1);
		value = value < 0 ? value * cannon_motor_coef_up : value * cannon_motor_coef_down;
		sc.set(value);
	}

}
