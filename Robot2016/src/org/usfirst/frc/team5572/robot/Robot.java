
package org.usfirst.frc.team5572.robot;

import org.usfirst.frc.team5572.util.TimerSystem;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Robot extends SampleRobot {
    SendableChooser sc = new SendableChooser();
    SendableChooser tsc = new SendableChooser();
    
    @Override
    protected void robotInit( ) {
        DriveStation.init();
        Snoopr.init();
        Arduino.init();
        DriveTrain.init();
        Launcher.init();
        sc.addDefault("false", 0);
        sc.addObject("true", 1);
        sc.addObject("ttrue", 2);
        SmartDashboard.putData("run", sc);
        SmartDashboard.putNumber("angle_set", 0.0);
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
    public void test(){
        
    }
}