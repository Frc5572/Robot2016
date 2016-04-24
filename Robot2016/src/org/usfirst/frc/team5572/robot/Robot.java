
package org.usfirst.frc.team5572.robot;

import org.usfirst.frc.team5572.util.TimerSystem;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;



public class Robot extends SampleRobot {
    
    @Override
    protected void robotInit( ) {
        DriveStation.init();
        Snoopr.init();
        Arduino.init();
        DriveTrain.init();
        Launcher.init();
    }
    
    @Override
    public void operatorControl( ) {
        DriveStation.beginCamera();
        while ( isOperatorControl() && isEnabled() ) {
            Launcher.update();
            DriveStation.updateCamera();
            DriveTrain.teleop();
            Arduino.snoop();
            TimerSystem.update();
            Timer.delay(.005);
        }
        DriveStation.endCamera();
    }
    
    @Override
    public void autonomous( ) {
        while((!Launcher.setAngle(43.5)) && isAutonomous() && isEnabled());
        while((!Launcher.fire()) && isAutonomous() && isEnabled());
        Snoopr.zero();
        DriveTrain.drivelr(-.4, -0.24);
        Timer.delay(0.5);
        DriveTrain.driveStraightReset();
        while((!DriveTrain.driveStraight(0.34, 2, 20)) && isAutonomous() && isEnabled()) /*DriveTrain.feedData()*/;
        System.out.println("exit");
        DriveTrain.driveStraightReset();
        while((!DriveTrain.driveStraight(0.34, 2, 20)) && isAutonomous() && isEnabled()) /*DriveTrain.feedData()*/;
        while((!DriveTrain.setGlobalAngle(90, 0.5, 0.26)) && isAutonomous() && isEnabled());
        DriveTrain.driveStraightReset();
        while((!DriveTrain.driveStraight(0.34, 2, 20)) && isAutonomous() && isEnabled()) /*DriveTrain.feedData()*/;
        DriveTrain.drivelr(0, 0);
    }
}