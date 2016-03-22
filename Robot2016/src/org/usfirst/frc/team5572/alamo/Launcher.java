package org.usfirst.frc.team5572.alamo;

import static org.usfirst.frc.team5572.alamo.Conf.*;

import org.usfirst.frc.team5572.alamo.Conf;
import org.usfirst.frc.team5572.alamo.DriveStation;
import org.usfirst.frc.team5572.alamo.Snoopr;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;

public class Launcher {

	private static Compressor comp;
	private static DoubleSolenoid IntakeSystem;
	private static DoubleSolenoid cockingSystem;
	private static DoubleSolenoid lock;
	private static DoubleSolenoid unknown;
	/** Talon for cannon rotation */
	private static SpeedController sc;
	/** Talon for intake rotation */
	private static SpeedController roll;

	public static void init() {
		PowerDistributionPanel.setDefaultSolenoidModule(12);

		comp = new Compressor(12);
		IntakeSystem = new DoubleSolenoid(12, grabber_forward, grabber_reverse);// Intake
																				// Sysytem
		cockingSystem = new DoubleSolenoid(12, pull_forward, pull_reverse); // Cocking
																			// system.
		lock = new DoubleSolenoid(12, lock_forward, lock_reverse);
		sc = new CANTalon(1); // Talon for cannon rotation
		roll = new CANTalon(3); // Talon for intake rotation
	}

	private static int accum = 0;

	public static boolean setAngle(double angle, double threshold, double speed) {
		double curr = Snoopr.getAngle();
		if (Math.abs(curr - angle) <= threshold) {
			accum++;
			if (accum > 300)
				return true;
			return false;
		}
		accum = 0;
		System.out.println(curr + ":" + limit((angle - curr) / 3));
		changeAngle(Math.abs(limit((angle - curr) / 3)) * (limit((angle - curr) / 3) > 0 ? speed : -speed));
		return false;
	}

	public static boolean setAngleV(double angle, double threshold, double speed) {
		double curr = Snoopr.getV();
		if (Math.abs(curr - angle) <= threshold)
			return true;
		System.out.println(curr + ":" + limit((angle - curr) / 2));
		changeAngle((limit(angle - curr) > 0 ? 1 : -1) * speed);
		return false;
	}

	public static boolean autofire() {
		if (time > 0) {
			time--;
			return false;
		}
		boolean dios[] = Snoopr.getDio(); // Cock, lock, grab
		snoop();
		if (dios[1]) { // Checks if the lock is in place
			cockingSystem.set(Value.kForward); // Retracts the cocking
												// pistons
			time = 20;
		} else if (!dios[0]) { // Checks if the cock sensor is not being
								// activated
			cockingSystem.set(Value.kReverse); // Extends the cocking
												// pistons
			return false;
		}
		roll.set(-rollSpeed);
		lock.set(Value.kReverse); // Lock the lock

		if (dios[0] && dios[1]) {
			roll.set(0);
			return true;
		} else {
			return false;
		}
	}

	public static void fire() {
		lock.set(Value.kForward);
	}

	// *
	public static double getComp() {
		return comp.getCompressorCurrent();
	}

	public static void lock() {
		lock.set(Value.kReverse);
	}

	public static void release() {
		lock.set(Value.kForward);
	}

	// */
	public static void openClaw() {
		IntakeSystem.set(Value.kForward);
	}

	public static void closeClaw() {
		IntakeSystem.set(Value.kReverse);
	}

	// *
	public static void _cock() {
		cockingSystem.set(Value.kReverse);
	}

	public static void unknownOpen() {
		unknown.set(Value.kReverse);
	}

	public static void unknownClose() {
		unknown.set(Value.kForward);
	}

	public static void uncock() {
		cockingSystem.set(Value.kForward);
	}
	// */

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
		SmartDashboard.putString("lock",
				lock.get() == Value.kOff ? "Off" : (lock.get() == Value.kForward ? "Forward" : "Reverse"));
		SmartDashboard.putString("grabber", IntakeSystem.get() == Value.kOff ? "Off"
				: (IntakeSystem.get() == Value.kForward ? "Forward" : "Reverse"));
		SmartDashboard.putString("puller", cockingSystem.get() == Value.kOff ? "Off"
				: (cockingSystem.get() == Value.kForward ? "Forward" : "Reverse"));
		SmartDashboard.putNumber("PressureV", Snoopr.getPressureSwitchV());
		SmartDashboard.putNumber("Pressure", Snoopr.getPressure());
	}

	private static int time = 0;

	private static boolean run = false;
	private static int isShooting = 0;
	private static boolean isCancelPressed = false, check = true, manual = false;

	private static boolean overrideClaw = false;

	public static void end() {
		begin();
	}

	public static void begin() {
		lock.set(Value.kForward); // unlock trigger
		IntakeSystem.set(Value.kReverse); // close grabber
		cockingSystem.set(Value.kForward); // uncock
		run = false;
		SmartDashboard.putNumber("Set Angle", defAngle);
	}

	public static void update() {
		changeAngle(DriveStation.b_y() * DriveStation.b_getThrottle());
		if (DriveStation.b_getKey(2)) {
			setAngle(SmartDashboard.getNumber("Set Angle"), 1, .54);
		}
		snoop(); // Send data to the SmartDashboard
		if (DriveStation.b_getKey(button_oclaw)) { // Overrides the claws
													// current state. Will
													// not fire unless run
													// is false
			openClaw();
			overrideClaw = true;
		}
		if (DriveStation.b_getKey(button_cclaw)) { // Overrides the claws
													// current state. Will
													// not fire unless run
													// is false
			closeClaw();
			overrideClaw = false;
		}
		if (DriveStation.b_getKey(button_roll)) { // Roll the intake wheel
													// in, so that the ball
													// will enter a position
													// to be shot
			roll.set(-rollSpeed);
		} else if (DriveStation.b_getKey(button_unroll)) { // Roll the
															// intake wheel
															// out, so that
															// the ball will
															// exit the
															// grabber
			roll.set(rollSpeed);
		} else { // Neither is set, so don't do anything
			roll.set(0);
		}
		if (DriveStation.b_getKey(3) && DriveStation.b_getKey(4) && DriveStation.b_getKey(5)) {
			manual = true;
			time = 10;
			return;
		} else if (DriveStation.b_getKey(3) && DriveStation.b_getKey(4) && DriveStation.b_getKey(6)) {
			manual = false;
			time = 10;
			return;
		}
		if (manual) {
			if (DriveStation.b_getKey(3)) {
				cockingSystem.set(Value.kReverse);
			} else if (DriveStation.b_getKey(5)) {
				cockingSystem.set(Value.kForward);
			}
			if (DriveStation.b_getKey(4)) {
				lock.set(Value.kReverse);
			} else if (DriveStation.b_getKey(6)) {
				lock.set(Value.kForward);
			}
		} else {
			if (DriveStation.b_getKey(button_cancel) && !isCancelPressed) {
				run = true;
				isCancelPressed = true;
			}
			if (!DriveStation.b_getKey(button_cancel)) {
				isCancelPressed = false;
			}
			if (time > 0) { // If the robot should wait
				time--;
				SmartDashboard.putString("Launcher Error Code", "A");
				return;
			}
			boolean dios[] = Snoopr.getDio(); // Cock, lock, grab
			if (isShooting != 0) { // Is in the shooting phase
				if (isShooting == 1) { // Opens claw
					IntakeSystem.set(Value.kForward);
					if (!dios[2]) { // Claw is open
						isShooting = 2;
						time = launcherWait; // Wait for $launcherWait seconds
					}
					SmartDashboard.putString("Launcher Error Code", "B");
					return;
				}
				isShooting = 0; // Set back to cocking mode
				lock.set(Value.kForward); // Release the spring
				time = resetWait; // Wait for $resetWait seconds. This delay
									// allows the ball to leave the grabber
									// before closing it again
				run = false; // Disable autocock. This is for safety reasons
			} else { // Cocking phase
				if (DriveStation.b_getKey(button_shoot) && dios[1]) { // Is the
																		// fire
																		// button
																		// being
																		// pressed
																		// and
																		// is
																		// the
																		// locking
																		// mechanism
																		// in
																		// place
					isShooting = 1; // Start the shooting phase
					check = true; // Start periodic checking of the compressor
									// so that we do not run without enough
									// pressure
					SmartDashboard.putString("Launcher Error Code", "C");
					return;
				}
				if (!run) { // Make sure autocock is on
					SmartDashboard.putString("Launcher Error Code", "D");
					return;
				}
				if ((Snoopr.getAngle() > 40 || (Snoopr.getPressureSwitchV() < Conf.pressure) && check)) { // Checks
					// compressor
					// to
					// makes
					// sure
					// enough
					// pressure
					// is
					// built up
					// to
					// start
					// auto-cocking
					SmartDashboard.putString("Launcher Error Code", "E");
					return;
				}
				check = false; // Stop the cocking phase. This is in place so
								// that the cocking phase does not stop midphase
								// due to loss of pressure.
				if (dios[1]) { // Checks if the lock is in place
					cockingSystem.set(Value.kForward); // Retracts the cocking
														// pistons
				} else if (!dios[0]) { // Checks if the cock sensor is not being
										// activated
					cockingSystem.set(Value.kReverse); // Extends the cocking
														// pistons
					SmartDashboard.putString("Launcher Error Code", "Test");
					return;
				}
				lock.set(Value.kReverse); // Lock the lock
				if (!overrideClaw)
					closeClaw();
				SmartDashboard.putString("Launcher Error Code", "G");
			}
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
		value = limit(value * -1);
		value = value < 0 ? value : value * cannon_motor_coef;
		sc.set(value);
	}

}
