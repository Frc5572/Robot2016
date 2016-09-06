package org.usfirst.frc.team5572.util;

import java.util.HashSet;
import java.util.Set;

public class TimerSystem {
    
    public static final long SECOND = ( long ) 1e9;
    
    public static abstract class Time{
        
        private long timer;
        
        public abstract boolean run(long time);
        
    }
    
    private static Set< Time > times = new HashSet< TimerSystem.Time >();
    
    public static void execute(Time t){
        t.timer = System.nanoTime();
        t.run(0);
        times.add(t);
    }
    
    public static void update(){
        Set< Time > end = new HashSet< TimerSystem.Time >();
        for(Time t : times){
            if(t.run(System.nanoTime() - t.timer)) end.add(t);
        }
       times.removeAll(end);
    }
    
    public static int times(){
        return times.size();
    }

    public static void clear( ) {
        times.clear();
    }
    
}
