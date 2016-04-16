
package org.usfirst.frc.team5572.util;

import static java.lang.Math.*;



public class NumberUtils {
    public static final double clamp( double a, double min, double max ) {
        return a < min ? min : ( a > max ? max : a );
    }
    
    public static final double clampMotor( double a ) {
        return clamp(a, -1, 1);
    }
    
    public static final double clampPositive( double a ) {
        return clamp(a, 0, 1);
    }
    
    public static final double signum( double a ) {
        return a == 0 ? 0 : a > 0 ? 1 : -1;
    }
    
    public static double max( double a, double b ) {
        return a > b ? a : b;
    }
    
    public static double map( double x, double in_min, double in_max, double out_min, double out_max ) {
        return ( x - in_min ) * ( out_max - out_min ) / ( in_max - in_min ) + out_min;
    }
    
    public static double pow_interp( double x, double min, double max, double p ) {
        double imx = 1 - x;
        return min + ( max - min ) * ( 1 - imx ) / pow(1 + imx, p);
    }
    
    public static double lin_interp( double x, double min, double max ) {
        double imx = 1 - x;
        return min + ( max - min ) * (1 - imx);
    }
    
    public static double mode( double[] m ) {
        int mindex = 0;
        int count = 0;
        for ( int i = 0; i < m.length; i++ ) {
            int altCount = 0;
            for ( int j = i; j < m.length; j++ ) {
                if ( m[i] == m[j] )
                    altCount++;
            }
            if ( altCount > count ) {
                mindex = i;
                count = altCount;
            }
        }
        return m[mindex];
    }
}
