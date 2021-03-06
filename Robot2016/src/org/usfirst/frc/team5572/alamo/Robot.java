package org.usfirst.frc.team5572.alamo;

import org.usfirst.frc.team5572.alamo.Conf;
import org.usfirst.frc.team5572.alamo.DriveStation;
import org.usfirst.frc.team5572.alamo.DriveTrain;
import org.usfirst.frc.team5572.alamo.Jetson;
import org.usfirst.frc.team5572.alamo.Launcher;
import org.usfirst.frc.team5572.alamo.Lift;
import org.usfirst.frc.team5572.alamo.Snoopr;
import org.usfirst.frc.team5572.alamo.StateMachine;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends SampleRobot {

	public void robotInit() {
		DriveStation.init();
		DriveTrain.init();
		Snoopr.init();
		Lift.init();
		Launcher.init();
		Jetson.init();
	}

	StateMachine currentStateMachine = null;

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * if-else structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */

	private static enum AutoMode {
		SpyZone, Cheval, RockWall, RoughTerrain, None;
	}

	private static AutoMode s = AutoMode.RockWall;

	public void autonomous() {
		// DriveTrain.drivelr(.35, .35);
		Snoopr.resetEncoders();
		Launcher.begin();
		DriveTrain.drivelr(0, 0);
		Launcher.closeClaw();
		if (s.equals(AutoMode.SpyZone)) {
			while (Snoopr.getPressureSwitchV() < Conf.pressure)
				if (!isEnabled() || !isAutonomous())
					break;
			while (!Launcher.setAngle(36, Conf.autoThresh, .54))
				if (!isEnabled() || !isAutonomous())
					break;
			Launcher.changeAngle(0);
			System.out.println("done");
			while (!Launcher.autofire())
				if (!isEnabled() || !isAutonomous())
					break;
			Launcher.openClaw();
			if (isEnabled() && isAutonomous()) {
				Timer.delay(3);
				Launcher.fire();
			}
		} else if (s.equals(AutoMode.Cheval)) {
			double delta = 0;
			while (!DriveTrain.driveStraight(0.5, 200)) {
				DriveTrain.feedData();
				if (!isAutonomous() || !isEnabled()) {
					break;
				}
			}
			System.out.println(delta);
			DriveTrain.drivelr(0, 0);
			while (true) {
				DriveTrain.feedData();
				if (!isAutonomous() || !isEnabled()) {
					System.out.println(delta);
					break;
				}
			}
		} else if (s.equals(AutoMode.RoughTerrain) || s.equals(AutoMode.RockWall)) {
			DriveTrain.drivelr(-.33, -.33);
			Timer.delay(s.equals(AutoMode.RoughTerrain) ? 6 : 2);
			/*
			 * while (Snoopr.getRightEncoderRaw() < 1600) {
			 * DriveTrain.drivelr(.66, .66); }
			 */
			DriveTrain.drivelr(0, 0);
		}
		/*
		 * currentStateMachine = auto1_State_01;
		 * currentStateMachine.startMachine(); while (isAutonomous() &&
		 * isEnabled()) { if (currentStateMachine != null) { if
		 * (currentStateMachine.isRunning) { currentStateMachine.tick(); } else
		 * { if (currentStateMachine.isDone) {
		 * currentStateMachine.stopMachine(); currentStateMachine =
		 * currentStateMachine.getNextState(); if (currentStateMachine != null)
		 * { currentStateMachine.startMachine(); } } } } }
		 * currentStateMachine.setMachineDone();
		 */
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	public void operatorControl() {
		drive(false);
	}

	@Override
	protected void disabled() {
		Launcher.changeAngle(0);
	}

	private void drive(boolean test) {
		Timer timer = new Timer();
		timer.start();
		Launcher.begin();
		if (test)
			Launcher.openClaw();
		DriveStation.beginCamera();
		while (((isOperatorControl() && !test) || (isTest() && test)) && isEnabled()) {
			timer.start();
			DriveStation.updateCamera();
			DriveTrain.teleop();
			Lift.update(test);
			if (DriveStation.a_getKey(-1)) {
				Jetson.autoTurn();
			}
			try {
				Launcher.update();
			} catch (Exception e) {
				e.printStackTrace();
			}
			while (timer.get() < 0.005)
				;
			timer.stop();
			timer.reset();
		}
		DriveStation.endCamera();
		Launcher.end();
		timer.stop();
	}

	/**
	 * Runs during test mode
	 */
	public void test() {
		drive(true);
	}

	StateMachine auto1_State_01 = new StateMachine("auto1_State_01") {

		int tickDelay;
		int doneDelay;

		public void initMachine() {
			tickDelay = 0;
			doneDelay = 0;
		}

		public void tick() {
			DriveTrain.drivelr(.4, .4);
			tickDelay++;
			if (tickDelay >= 100) {
				System.out.println(machineName);
				tickDelay = 0;
				doneDelay++;
				if (doneDelay >= 10) {
					setNextState(auto1_State_02);
					setMachineDone();
				}
			}
		}
	};

	StateMachine auto1_State_02 = new StateMachine("auto1_State_02") {

		public void initMachine() {
			Snoopr.zero();
		}

		public void tick() {
			double angle = Snoopr.getTotalYaw();
			DriveTrain.drivelr(-.35, .35);
			while (angle > 180) {
				angle -= 360;
			}
			if (abs(angle - 90) < 10)
				setMachineDone();
		}

		private double abs(double d) {
			return d > 0 ? d : -d;
		}

		public void cleanupMachine() {
			DriveTrain.drivelr(0, 0);
		}
	};
}
