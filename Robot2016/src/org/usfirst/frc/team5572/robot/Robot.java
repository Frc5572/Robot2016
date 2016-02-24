package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.vision.USBCamera;
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
		Launcher.init();
		
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
	public void autonomous() {
		//DriveTrain.drivelr(.35, .35);
		while (isAutonomous() && isEnabled()) {

		}
		DriveTrain.drivelr(0, 0);
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
	
	private void drive(boolean test){
		Timer timer = new Timer();
		timer.start();
		Timer delay = new Timer();
		Launcher.begin();
		DriveStation.beginCamera();
		while (((isOperatorControl() && !test) || (isTest() && test)) && isEnabled()) {
			timer.start();
			DriveStation.updateCamera();
			DriveTrain.teleop();
			Lift.update(135-timer.get() <= 20 || test);
			try {
				Launcher.update();
			} catch (Exception e) {
				e.printStackTrace();
			}
			while(timer.get() < 0.005);
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
