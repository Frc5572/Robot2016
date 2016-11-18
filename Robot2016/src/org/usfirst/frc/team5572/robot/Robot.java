
package org.usfirst.frc.team5572.robot;

import org.usfirst.frc.team5572.util.TimerSystem;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {
    @Override
    protected void robotInit( ) {
        DriveStation.init();
        DriveTrain.init();
        Launcher.init();
    }
    
    @Override
    public void operatorControl( ) {
        DriveStation.beginCamera();
        while ( isOperatorControl() && isEnabled() ) {
            Launcher.update();
            DriveTrain.teleop();
            TimerSystem.update();
            Timer.delay(.005);
            DriveStation.updateCamera();
        }
        DriveStation.endCamera();
    }

    @Override
    public void autonomous( ) {
    }
    
    @Override
    public void test( ) {
        while ( isEnabled() && isTest() ) {
        
        }
    }
}