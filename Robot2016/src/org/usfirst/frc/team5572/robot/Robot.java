
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
            TimerSystem.update();
            //
            if ( Arduino.isRunning() && 1 != (int)sc.getSelected() ) {
                Arduino.end();
            } else if ( !Arduino.isRunning() && 1 == (int)sc.getSelected() ) {
                Arduino.start();
            }
            if ( Arduino.isRunningTegra() && 2 != (int)sc.getSelected() ) {
                Arduino.endTegra();
            } else if ( !Arduino.isRunningTegra() && 2 == (int)sc.getSelected() ) {
                Arduino.startTegra();
            }
            Arduino.setAngle(SmartDashboard.getNumber("angle_set"));
            Arduino.snoop();
            if ( DriveStation.b_getKey(8) ) {
                Launcher.linAct.set(Arduino.getMotor());
                Launcher.m = true;
            } else {
                Launcher.m = false;
            }
            //
            Timer.delay(.005);
        }
        DriveStation.endCamera();
    }
}