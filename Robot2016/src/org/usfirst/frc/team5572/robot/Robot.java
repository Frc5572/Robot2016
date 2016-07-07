
package org.usfirst.frc.team5572.robot;

import org.usfirst.frc.team5572.util.TimerSystem;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Robot extends SampleRobot {
    SendableChooser sc = new SendableChooser(); // Option dialogue for
                                                // autonomous
    
    /**
     * Initialization of the robot. When the robot is first powered and all of
     * the pre-init functions are completed, this method is called. <br />
     * This initializes all systems and sets default settings
     */
    @Override
    protected void robotInit( ) {
        DriveStation.init(); // Initializes controller handling
        Snoopr.init(); // Initializes sensor handling
        Arduino.init(); // Initializes subprocessor handling
        DriveTrain.init(); // Initializes drivetrain actuators
        Launcher.init(); // Initializes launching system actuators
        Lift.init(); // Initializes lift system actuators
        sc.addObject("Drive Straight", 0); // Creates option to simply drive
                                           // straight during autonomous
        sc.addObject("Spy Zone", 1); // Creates option to shoot from the spyzone
                                     // during autonomous
        sc.addDefault("Spy Zone & Reach", 2); // Creates option to shoot from
                                              // the spyzone, then reach the
                                              // defense during autonomous
        sc.addObject("Test Driving", 3); // Creates option that performs "Spy
                                         // Zone & Reach" without shooting
                                         // during autonomous
        SmartDashboard.putData("Auto", sc); // Place the autonomous options on
                                            // the Smart Dashboard
    }
    
    /**
     * Called at the beginning of the teleop period. This method performs all
     * functions that occur during the teleoperated period, since no alternate
     * threads are present in this project
     */
    @Override
    public void operatorControl( ) {
        DriveStation.beginCamera(); // Establish connection between the client
                                    // and robot for image transmission
        while ( isOperatorControl() && isEnabled() ) { // Checks each iteration
                                                       // to make sure that
                                                       // teleop is still
                                                       // enabled.
            Launcher.update(); // Run launcher event. This handles all launcher
                               // components, including the shooting system, and
                               // the arm movement system. This also handles
                               // arduino communication events, such as
                               // auto-targetting.
            DriveStation.updateCamera(); // Send current image from the camera
                                         // to the client
            DriveTrain.teleop(); // Run drivetrain event. This handles all
                                 // drivetrain components
            Lift.update(false); // Run lift event. This handles all lift
                                // components, such as primer pistons and the
                                // lifting motors. The false parameter denotes
                                // that the sensors that are positioned on the
                                // top and the bottom of the lift mechanism
                                // should not be ignored, and should stop the
                                // lift system functions if they are activated.
            Arduino.snoop(); // Send arduino information to the smart dashboard.
                             // Should the arduino's auto-targetting system fail
                             // to perform, the arduino will send angle
                             // information for manual shooting. This method
                             // sends that information to the client.
            TimerSystem.update(); // Update all timed events. This is a helper
                                  // class that allows systems to run on system
                                  // time rather than on ticking time.
            Timer.delay(.005); // Allow all systems to update on the hardware
                               // side before resetting them.
        }
        DriveStation.endCamera(); // End connection between robot and client for
                                  // image transmission. This allows the robot
                                  // to run other modes on lower bandwidth if
                                  // needed.
    }
    
    /**
     * Called at the beginning of the autonomous period. This method performs
     * all functions that occur during the autonomous period, since no alternate
     * threads are present in this project
     */
    @Override
    public void autonomous( ) {
        DriveTrain.resetGlobalAngle(); // Sets stored offset for gyroscope to
                                       // the current angle so that the global
                                       // angle returns 0.
        DriveTrain.driveStraightReset(); // Sets stored offset for encoders to
                                         // the current values so that the
                                         // global distance traveled returns 0.
        if ( ( int ) sc.getSelected() == 1 || ( int ) sc.getSelected() == 2 ) { // If
                                                                                // the
                                                                                // autonomous
                                                                                // mode
                                                                                // is
                                                                                // supposed
                                                                                // to
                                                                                // shoot.
            while ( !Arduino.useTegra(Launcher.linAct, 0.4, 0.7) && isAutonomous() && isEnabled() ); // Set
                                                                                                     // the
                                                                                                     // angle
                                                                                                     // using
                                                                                                     // the
                                                                                                     // arduino
            double angle = Arduino.getAngle() + 1;// Get new desired angle.
                                                  // Arduino always overshoots
                                                  // the angle. This is fine
                                                  // when coming from below, but
                                                  // since this is coming from
                                                  // above in this case, it
                                                  // comes
                                                  // up about one degree to
                                                  // compensate
            while ( Arduino.getAngle() < angle && isAutonomous() && isEnabled() )
                Launcher.linAct.set(-0.4);
            Launcher.linAct.set(0);
            while ( !Launcher.fire() && isAutonomous() && isEnabled() ); // Execute
                                                                         // the
                                                                         // firing
                                                                         // sequence.
            System.out.println("done"); // Debug "done" appears when shooting
                                        // cycle has finished.
        }
        if ( ( int ) sc.getSelected() == 2 || ( int ) sc.getSelected() == 3 ) { // If
                                                                                // the
                                                                                // autonomous
                                                                                // mode
                                                                                // is
                                                                                // supposed
                                                                                // to
                                                                                // reach
                                                                                // the
                                                                                // defenses
            while ( Math.abs(Snoopr.getLeftEncoderRaw()) < 200 && isAutonomous() && isEnabled() ) {
                DriveTrain.drivelr(-.7, -.3); // Get off of the wall. This is an
                                              // approximation that is fixed
                                              // using the gyroscope later
            }
            while ( !DriveTrain.driveStraight(0.4, 0.5, 24) && isAutonomous() && isEnabled() ); // Move
                                                                                                // away
                                                                                                // from
                                                                                                // the
                                                                                                // wall
            DriveTrain.drivelr(0, 0); // Stop
            while ( !DriveTrain.setGlobalAngle(90, 0.2, 0.22) && isAutonomous() && isEnabled() ); // Turn
                                                                                                  // perpendicular
                                                                                                  // to
                                                                                                  // the
                                                                                                  // wall
            DriveTrain.drivelr(0, 0); // Stop
            DriveTrain.driveStraightReset(); // Reset encoders so that the
                                             // overall distance is reset to 0.
            while ( !DriveTrain.driveStraight(0.4, 0.5, 24) && isAutonomous() && isEnabled() ); // Drive
                                                                                                // towards
                                                                                                // the
                                                                                                // defense.
                                                                                                // TODO:
                                                                                                // This
                                                                                                // needs
                                                                                                // to
                                                                                                // be
                                                                                                // modified
                                                                                                // to
                                                                                                // actually
                                                                                                // reach
                                                                                                // the
                                                                                                // defense.
            DriveTrain.drivelr(0, 0); // Stop at the end of the movement period.
        }
        DriveTrain.drivelr(0, 0); // Safety stop. Isn't needed, but is there
                                  // just for peace of mind.
    }
    
    /**
     * Called at the beginning of the test period. This never occurs during
     * competition, but can be called manually when not in FMS. This handles all
     * calibration and reset methods
     */
    @Override
    public void test( ) {
        while ( isEnabled() && isTest() ) {
            Lift.update(true); // Update the lift system. The true denotes that
                               // the sensors should be ignored. This allows for
                               // the lift to be move upwards, even though the
                               // bottom sensor is activated. Caution is
                               // advised, since there is no safety methods and
                               // is truly manual. The reason for having this is
                               // to put the lift into starting position between
                               // rounds.
        }
    }
}