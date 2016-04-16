package org.usfirst.frc.team5572.hubcity;

import static org.usfirst.frc.team5572.hubcity.Configuration.def_auto_spy_fwd;
import static org.usfirst.frc.team5572.hubcity.Configuration.def_auto_spy_turn;

import org.usfirst.frc.team5572.util.TimerSystem;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	private static enum Option {
		SPY, REACH, CROSS
	}

	@Override
	protected void robotInit() {
		DriveTrain.init();
		DriveStation.init();
		Launcher.init();
		Snoopr.init();
		Lift.init();
		Arduino.init();
		sc.addObject("Spy Zone", Option.SPY);
		sc.addObject("Cross", Option.CROSS);
		sc.addDefault("Reach", Option.REACH);
		SmartDashboard.putData("Autonomous", sc);
	}

	private SendableChooser sc = new SendableChooser();

	@Override
	public void autonomous() {
	    Arduino.end();
		if (sc.getSelected().equals(Option.SPY)) {
		    //Launcher.primer.set(Value.kForward); //TODO: Fix Primer
			//*
			DriveTrain.driveStraightReset();
			
			while (!DriveTrain.driveStraight(0.35, 0.05, def_auto_spy_fwd)
					&& isEnabled() && isAutonomous())
				DriveTrain.feedData();
			
			System.out.println("done");
			DriveTrain.drivelr(0, 0);
			//*
			DriveTrain.resetGlobalAngle();
			DriveTrain.feedData();
			while (!DriveTrain.setGlobalAngle(def_auto_spy_turn, 0.5, 0.29)
					&& isEnabled() && isAutonomous())
				DriveTrain.feedData();
			///
			DriveTrain.drivelr(0, 0);
			while (!Arduino.angle(Launcher.angle_actual, 65) && isEnabled()
					&& isAutonomous()) {
				Arduino.snoop();
			}
			double wait = System.nanoTime() + 1e9 * 5 / 4;
			while (wait > System.nanoTime() && isEnabled() && isAutonomous()) {
				Launcher.intake.set(-1);
				if (wait - 7.5e8 < System.nanoTime() && wait > System.nanoTime()) {
					//Launcher.primer.set(Value.kReverse); //TODO: Fix Primer
				}
			}
			Launcher.intake.set(0);
			//Launcher.primer.set(Value.kForward); //TODO: Fix Primer
			
		//*/
		} else if (sc.getSelected().equals(Option.CROSS)) {
		    //Launcher.primer.set(Value.kForward); //TODO: Fix Primer
            //*
            DriveTrain.driveStraightReset();
            
            while (!DriveTrain.driveStraight(0.35, 0.05, 150)
                    && isEnabled() && isAutonomous())
                DriveTrain.feedData();
            
            System.out.println("done");
            DriveTrain.drivelr(0, 0);
		} else {
		    //Launcher.primer.set(Value.kForward); //TODO: Fix Primer
            //*
            DriveTrain.driveStraightReset();
            
            while (!DriveTrain.driveStraight(0.35, 0.05, 50)
                    && isEnabled() && isAutonomous())
                DriveTrain.feedData();
            
            System.out.println("done");
            DriveTrain.drivelr(0, 0);
		}
	}

	@Override
	public void operatorControl() {
		Arduino.end();
		DriveTrain.driveStraightReset();
		DriveStation.beginCamera();
		while (isOperatorControl() && isEnabled()) {
			DriveTrain.teleop();
			Launcher.update();
			Lift.update(false);
			DriveStation.updateCamera();
			Arduino.snoop();
			TimerSystem.update();
			Timer.delay(0.005);
		}
		DriveStation.endCamera();
	}

	@Override
	public void test() {
		LiveWindow.setEnabled(false);
		while (isTest() && isEnabled()) {
			Lift.update(false);
			Timer.delay(0.005);
		}
	}

	@Override
	protected void disabled() {
		DriveTrain.drivelr(0, 0);
	}
}