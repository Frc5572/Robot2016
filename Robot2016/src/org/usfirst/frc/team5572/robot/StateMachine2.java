package org.usfirst.frc.team5572.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StateMachine2 {
	
	public static abstract class State{
		private boolean ended = false;
		public abstract void init();
		public abstract void update();
		public abstract void cleanup();
		protected void setStateOver(){
			ended = true;
		}
	}
	
	private static List<State[]> stateLists = new ArrayList<State[]>();
	
	public static void run(State[] states){
		stateLists.add(states);
	}
	
	private static <T> T[] shorten(T[] t){
		return Arrays.copyOfRange(t, 1, t.length);
	}
	
	public static void update(){
		boolean k[] = new boolean[stateLists.size()];
		for(int i = 0; i < stateLists.size(); i++){
			State[] s = stateLists.get(i);
			s[0].update();
			if(s[0].ended){
				k[i] = true;
			}else{
				k[i] = false;
			}
		}
		for(int i = 0; i < k.length; i++){
			if(k[i]){
				stateLists.get(i)[0].cleanup();
				stateLists.set(i, shorten(stateLists.get(i)));
				stateLists.get(i)[0].init();
			}
		}
	}
	
}
