
package org.usfirst.frc.team5572.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class i2c extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
	// Init I2C and look for address 55.
	I2C i2c_Master = new I2C(Port.kOnboard, 55);
	
    public void robotInit() {
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
       
    	byte[] data = new byte[1];
    	byte[] dat2a = new byte[2];
        while (isOperatorControl() && isEnabled()) {
        	 i2c_Master.writeBulk(new byte[]{12,78,89,90});
        	// Send 34 on Address 12
          //  i2c_Master.write(12, 34);
            Timer.delay(1);		/* wait a second     */
            // Request a byte from address 64.
            i2c_Master.read(64, 1, data);
            System.out.println( "Address 64: " + data[0]);
            Timer.delay(1);		/* wait a second     */
         // Send 78 on Address 56
            i2c_Master.write(56, 78);
            Timer.delay(1);		/* wait half a second     */
         // Request a byte from address 55.
            i2c_Master.read(55, 2, dat2a);
            System.out.println( "Address 55: " + (char)(dat2a[1]));
            System.out.println( "Address 55: " + (char)(dat2a[0]));
            Timer.delay(1);		/* wait a second     */
            
         }    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
