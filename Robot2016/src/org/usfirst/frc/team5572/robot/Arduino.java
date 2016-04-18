
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
    private static boolean         running;
    private static DigitalOutput   tegraRun;
    private static boolean         tegraRunning;
    private static AnalogInput     tegra;
    private static AnalogInput     distance;
                                            
    public static void init( ) {
        ai = new AnalogInput(an_power_in);
        angle = new AnalogInput(an_angle_in);
        ao = new VictorSP(an_angle_out);
        run = new DigitalOutput(dio_angle_run);
        found = new DigitalInput(dio_angle_found);
        tegraRun = new DigitalOutput(dio_tegra_run);
        tegra = new AnalogInput(an_tegra_in);
        distance = new AnalogInput(an_dist_in);
        AnalogInput.setGlobalSampleRate(31250);
    }
    
    public static void setAngle( double angle ) {
        ao.set(map(angle, def_angle_min_out, def_angle_max_out, -1, 1));
    }
    
    public static void startTegra( ) {
        tegraRun.set(true);
        tegraRunning = true;
    }
    
    public static void endTegra( ) {
        tegraRun.set(false);
        tegraRunning = false;
    }
    
    public static boolean isRunningTegra( ) {
        return tegraRunning;
    }
    
    public static double getTegra( ) {
        return tegra.getVoltage();
    }
    
    public static double getDistance( ) {
        return distance.getVoltage();
    }
    
    public static boolean angle( SpeedController sc, double angle, double max) {
        start();
        setAngle(angle);
        if ( isInPlace() ) {
            end();
            return true;
        }
        double d = getMotor();
        double abs = d > 0 ? d : -d;
        double sig = d > 0 ? 1 : -1;
        sc.set(sig * (abs > max ? max : abs));
        System.out.println(sig * (abs > max ? max : abs));
        return false;
    }
    
    public static void start( ) {
        run.set(true);
        running = true;
    }
    
    public static void end( ) {
        run.set(false);
        running = false;
    }
    
    public static boolean isRunning( ) {
        return running;
    }
    
    private static double anglev = 0;
    
    public static double getAngle( ) {
        double[] dataAngle = new double[def_sampler_size];
        for ( int i = 0; i < def_sampler_size; i++ ) {
            dataAngle[i] = angle.getAverageVoltage();
        }
        anglev = mode(dataAngle);
        double angle = Math.round(4 * map(anglev, 0.29, 3.932, def_angle_min, def_angle_max)) / 4;
        return angle;
    }
    
    public static void snoop( ) {
        SmartDashboard.putNumber("angle", getAngle());
        SmartDashboard.putNumber("angleV", anglev);
        SmartDashboard.putNumber("motor", getMotor());
        SmartDashboard.putBoolean("running", isRunning());
        SmartDashboard.putNumber("tegra", getTegra());
        SmartDashboard.putNumber("distance", getDistance());
    }
    
    private static final double mid = 3.482665777206421;
    private static final double min = 0.004882812034338713;
    private static final double max = 4.873046398162842;
    
    public static double getMotor( ) {
        double[] data = new double[def_sampler_size];
        for ( int i = 0; i < def_sampler_size; i++ ) {
            data[i] = ai.getAverageVoltage();
            System.out.println("  -" + i + ":" + data[i]);
        }
        double modedata = mode(data);
        System.out.println("Motor:" + modedata);
        double motorOut = clamp(modedata < mid ? map(modedata, min, mid, -0.5, 0)
                : ( modedata > mid ? map(modedata, max, mid, 0.5, 0) : 0 ), -0.5, 0.5);
        return motorOut;
    }
    
    public static boolean isInPlace( ) {
        return found.get();
    }
}
