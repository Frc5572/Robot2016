
package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import static org.usfirst.frc.team5572.robot.Configuration.*;



public class newLauncher {
    private static CANTalon        intake;
    private static DoubleSolenoid  primer;
    private static SpeedController angle_actual;
    private static boolean useSwitchboard=false;
    private static double dx=0;
    public static void init() {
        PowerDistributionPanel.setDefaultSolenoidModule(def_sol_mod);
        intake = new CANTalon(can_wheel_intake);
        primer = new DoubleSolenoid(def_sol_mod, sol_primer_f, sol_primer_r);
        angle_actual = new VictorSP(pwm_wheel_cannon);
    }
    
    private static Value rollingIn = Value.kOff;
    public static void cock(int val){
    	Value sa;
    	switch(val){
    	case(0):
    		sa=Value.kOff;
    		break;
    	case(1):
    		sa=Value.kForward;
    		break;
    	case(-1):
    		sa=Value.kReverse;
    		break;
    	default:
    		sa=Value.kOff;
    	}
    	primer.set(sa);
    }
    public static void roll(int val){
    	intake.set(val);
    }
    public static void fire(){
    	new Thread(new Runnable(){
    		public void run(){
    	    	intake.set(-1);
    	    	try {
					Thread.sleep(125);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    	primer.set(Value.kForward);
    	    	
    	    	try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    	intake.set(0);
    	    	primer.set(Value.kReverse);
    		}
    	});
    	
    }
    public static void setDx(double sx){
    	dx=sx;
    }
    public static void lift(double hi){
    	angle_actual.set(hi-dx);
    	dx=hi;
    }
    public static void setSpeed(double speed){
    	angle_actual.set(speed);
    }
    public static void setLift(double speed){
    	angle_actual.set(speed-dx);
    }
    public static void update( ) {
        if ( rollingIn.equals(Value.kForward) ) {
            intake.set(1);
        } else if ( rollingIn.equals(Value.kReverse) ) {
            intake.set(-1);
        } else {
            intake.set(0);
        }
        if(!useSwitchboard){
        	angle_actual.set(DriveStation.b_getThrottle() * 0.6 * -DriveStation.b_y());
            primer.set(DriveStation.b_getKey(-1) ? Value.kForward : Value.kReverse);
	        if ( DriveStation.b_getKey(bind_ctr_outtake) ) {
	            rollingIn = Value.kForward;
	        } else if ( DriveStation.b_getKey(bind_ctr_intake) ) {
	            rollingIn = Value.kReverse;
	        } else {
	            rollingIn = Value.kOff;
	        }
        }
    }
}
