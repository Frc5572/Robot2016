
package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser chooser;

	StateMachine currentStateMachine = null;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
	public void autonomousInit() {
		autoSelected = (String) chooser.getSelected();
		System.out.println("Auto selected: " + autoSelected);

		// Init State Machine and start it...
		currentStateMachine = auto1_State_01;
		currentStateMachine.startMachine();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

		while (isAutonomous() && isEnabled()) {
			if (currentStateMachine != null) {
				if (currentStateMachine.isRunning) {
					currentStateMachine.tick();
				} else {
					if (currentStateMachine.isDone) {
						currentStateMachine.stopMachine();
						currentStateMachine = currentStateMachine.getNextState();
						if (currentStateMachine != null) {
							currentStateMachine.startMachine();
						}
					}
				}
			}
		}
	}



	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		while (isOperatorControl() && isEnabled()) {
			Timer.delay(0.020);		/* wait for one motor update time period (50Hz)     */
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

	// Machine for State 1
	StateMachine auto1_State_01 = new StateMachine("auto1_State_01") {

		int tickDelay;
		int doneDelay;

		public void initMachine() {
			tickDelay = 0;
			doneDelay = 0;
		}

		public void tick() {
			tickDelay++;
			if (tickDelay >= 100) {
				System.out.println(machineName);
				tickDelay = 0;
				doneDelay++;
				if (doneDelay >= 3) {
					setNextState(auto1_State_02);
					setMachineDone();
				}
			}
		};
	};

	// Machine for State 1
	StateMachine auto1_State_02 = new StateMachine("auto1_State_02") {

		int tickDelay;
		int doneDelay;

		public void initMachine() {
			tickDelay = 0;
			doneDelay = 0;
		}
		
		public void tick() {
			tickDelay++;
			if (tickDelay >= 100) {
				System.out.println(machineName);
				tickDelay = 0;
				doneDelay++;
				if (doneDelay >= 3) {
					setMachineDone();
				}
			}
		};
	};    

}
