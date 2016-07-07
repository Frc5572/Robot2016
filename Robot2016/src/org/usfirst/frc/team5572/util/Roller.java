
package org.usfirst.frc.team5572.util;

import edu.wpi.first.wpilibj.SpeedController;



public class Roller {
    private SpeedController sc;
    /** forward */
    private double          f, b, z;
                            
    public Roller( SpeedController sc, double forward, double backwards, double zero ) {
        this.sc = sc;
        f = forward;
        b = backwards;
        z = zero;
    }
    
    /** Sets to forward */
    public void forward( ) {
        sc.set(f);
    }
    
    /** Sets to backwards */
    public void backwards( ) {
        sc.set(b);
    }
    
    /** Sets to interpolated value (0 is backwards, 1 is forwards) */
    public void interpolate( float val ) {
        sc.set(f * val + b * ( 1 - val ));
    }
    
    /** Sets to zero */
    public void zero( ) {
        sc.set(z);
    }
}
