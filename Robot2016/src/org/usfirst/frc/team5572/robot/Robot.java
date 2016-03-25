package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class Robot extends SampleRobot {

	@Override
	protected void robotInit() {
		DriveTrain.init();
		DriveStation.init();
		Snoopr.init();
	}

	@Override
	public void autonomous() {
		DriveTrain.driveStraightReset();
		while (!DriveTrain.driveStraight(0.4, 0.05, 50))
			DriveTrain.feedData();
		System.out.println("done");
		DriveTrain.drivelr(0, 0);
		DriveTrain.resetGlobalAngle();
		DriveTrain.feedData();
		while(!DriveTrain.setGlobalAngle(40, 0.5) && isEnabled() && isAutonomous()) DriveTrain.feedData();
		DriveTrain.drivelr(0, 0);
	}

	@Override
	public void operatorControl() {
		DriveTrain.driveStraightReset();
		while (isOperatorControl() && isEnabled()) {
			DriveTrain.teleop();
			Timer.delay(0.005);
		}
	}

	@Override
	public void test() {
		
	}

	@Override
	protected void disabled() {
		DriveTrain.drivelr(0, 0);
	}

}