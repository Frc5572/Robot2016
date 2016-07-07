
package org.usfirst.frc.team5572.robot;

import static org.usfirst.frc.team5572.robot.Conf.*;
import static org.usfirst.frc.team5572.util.NumberUtils.*;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Arduino {
    private static AnalogInput     ai;
    private static AnalogInput     angle;
    private static SpeedController ao;
    private static DigitalOutput   run;
    private static DigitalInput    found;
    private static DigitalInput    target;
    private static boolean         running;
    private static DigitalOutput   tegraRun;
    private static boolean         tegraRunning;
    private static AnalogInput     tegra;
    private static AnalogInput     distance;
                                   
    /**Initialize arduino components*/
    public static void init( ) {
        ai = new AnalogInput(an_power_in);
        angle = new AnalogInput(an_angle_in);
        ao = new VictorSP(an_angle_out);
        run = new DigitalOutput(dio_angle_run);
        found = new DigitalInput(dio_angle_found);
        tegraRun = new DigitalOutput(dio_tegra_run);
        tegra = new AnalogInput(an_tegra_in);
        distance = new AnalogInput(an_dist_in);
        target = new DigitalInput(11);
        AnalogInput.setGlobalSampleRate(31250);
    }
    
    /**Set output to arduino*/
    public static void setAngle( double angle ) {
        ao.set(map(angle, def_angle_min_out, def_angle_max_out, -1, 1));
    }
    
    /**Note that arduino should be sending*/
    public static void startTegra( ) {
        tegraRun.set(true);
        tegraRunning = true;
    }

    /**Note that arduino should not be sending*/
    public static void endTegra( ) {
        tegraRun.set(false);
        tegraRunning = false;
    }
    
    /**Returns if the arduino should be sending*/
    public static boolean isRunningTegra( ) {
        return tegraRunning;
    }
    
    /**Get motor output for linear actuator*/
    public static double getTegra( ) {
        return tegra.getVoltage();
    }
    
    /**Get the distance to the U*/
    public static double getDistance( ) {
        return distance.getVoltage();
    }
    
    /**Arduino manual input angle utility method*/
    public static boolean angle( SpeedController sc, double angle, double max ) {
        setAngle(angle);
        start();
        if ( isInPlace() ) {
            sc.set(0);
            end();
            return true;
        }
        double d = -getMotor();
        System.out.println(d + " | " + mV + " | " + getAngle());
        double abs = d > 0 ? d : -d;
        double sig = d > 0 ? 1 : -1;
        sc.set(sig * ( abs > max ? max : abs ));
        return false;
    }
    
    /**Arduino auto-targetting angle utility method*/
    public static boolean useTegra( SpeedController cannon, double turnMax, double max ) {
        System.out.println("useTegra");
        if ( !isTargetThere() ) {
            return true;
        }
        startTegra();
        if ( isInPlace()) {
            endTegra();
            return true;
        }
        double d = -getMotor();
        double abs = d > 0 ? d : -d;
        double sig = d > 0 ? 1 : -1;
        System.out.println(d + " | " + abs + " | " + sig + "|" + mV);
        double k = sig * ( abs > max ? max : abs );
        if ( ! ( k > 0 && Arduino.getAngle() >= 65 ) && ! ( k < 0 && Arduino.getAngle() <= -20 ) )
            cannon.set(k);
        return false;
    }
    
    /**Returns if tegra can see the U*/
    public static boolean isTargetThere( ) {
        return target.get();
    }
    
    /**Start manual input*/
    public static void start( ) {
        run.set(true);
        running = true;
    }
    
    /**End manual input*/
    public static void end( ) {
        run.set(false);
        running = false;
    }
    
    /**Is in manual input*/
    public static boolean isRunning( ) {
        return running;
    }
    
    private static double anglev = 0;
    
    /**Get current angle*/
    public static double getAngle( ) {
        double[] dataAngle = new double[def_sampler_size];
        for ( int i = 0; i < def_sampler_size; i++ ) {
            dataAngle[i] = angle.getAverageVoltage();
        }
        anglev = mode(dataAngle);
        double angle = Math.round(4 * map(anglev, 0.29, 3.932, def_angle_min, def_angle_max)) / 4;
        return angle;
    }
    
    /**Send data to client*/
    public static void snoop( ) {
        SmartDashboard.putNumber("angle", getAngle());
        SmartDashboard.putBoolean("can shoot", isTargetThere());
        if(Conf.debug_smartDash){
            SmartDashboard.putNumber("angleV", anglev);
            SmartDashboard.putNumber("motor", getMotor());
            SmartDashboard.putBoolean("running", isRunning());
            SmartDashboard.putNumber("tegra", getTegra());
            SmartDashboard.putBoolean("found", isInPlace());
            SmartDashboard.putBoolean("tegraRunning", isRunningTegra());
            SmartDashboard.putNumber("distance", getDistance());
        }
    }
    private static double       mV;
                                
    /**Get averaged motor output*/
    public static double getMotor( ) {
        double[] data = new double[def_sampler_size];
        for ( int i = 0; i < def_sampler_size; i++ ) {
            data[i] = ai.getAverageVoltage();
        }
        double modedata = mode(data);
        mV = modedata;
        double motorOut = clamp(.4107787451 * modedata - 1.008054874, -0.5, 0.5);
        return motorOut;
    }
    
    /**Is arduino finished targetting*/
    public static boolean isInPlace( ) {
        return found.get();
    }
}
