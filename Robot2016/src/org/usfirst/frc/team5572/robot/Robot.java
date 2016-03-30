
package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static org.usfirst.frc.team5572.robot.Configuration.*;



public class Robot extends SampleRobot {
    private static enum Option {
        SPY, REACH, CROSS
    }
    
    @Override
    protected void robotInit( ) {
        DriveTrain.init();
        DriveStation.init();
        Launcher.init();
        Snoopr.init();
        Arduino.init();
        sc.addObject("Spy Zone", Option.SPY);
        sc.addObject("Cross", Option.CROSS);
        sc.addDefault("Reach", Option.REACH);
        SmartDashboard.putData("Autonomous", sc);
    }
    
    private SendableChooser sc = new SendableChooser();
    
    @Override
    public void autonomous( ) {
        if ( sc.getSelected().equals(Option.SPY) ) {
            DriveTrain.driveStraightReset();
            while ( !DriveTrain.driveStraight(0.35, 0.05, def_auto_spy_fwd) && isEnabled() && isAutonomous() )
                DriveTrain.feedData();
            System.out.println("done");
            DriveTrain.drivelr(0, 0);
            DriveTrain.resetGlobalAngle();
            DriveTrain.feedData();
            while ( !DriveTrain.setGlobalAngle(def_auto_spy_turn, 0.5, 0.2) && isEnabled() && isAutonomous() )
                DriveTrain.feedData();
            DriveTrain.drivelr(0, 0);
            while ( !Arduino.angle(Launcher.angle_actual, 59) && isEnabled() && isAutonomous() ) {
                Arduino.snoop();
            }
            double wait = System.nanoTime() + 1e9 * 5 / 4;
            while ( wait > System.nanoTime() && isEnabled() && isAutonomous() ) {
                Launcher.intake.set(1);
                if ( wait - 5e8 < System.nanoTime() && wait > System.nanoTime() ) {
                    Launcher.primer.set(Value.kForward);
                }
            }
            Launcher.intake.set(0);
            Launcher.primer.set(Value.kReverse);
        } else if ( sc.getSelected().equals(Option.CROSS) ) {} else {}
    }
    
    @Override
    public void operatorControl( ) {
        DriveTrain.driveStraightReset();
        DriveStation.beginCamera();
        while ( isOperatorControl() && isEnabled() ) {
            DriveTrain.teleop();
            Launcher.update();
            DriveStation.updateCamera();
            Arduino.snoop();
            Timer.delay(0.005);
        }
        DriveStation.endCamera();
    }
    
    @Override
    public void test( ) {}
    
    @Override
    protected void disabled( ) {
        DriveTrain.drivelr(0, 0);
    }
}