package org.usfirst.frc.team5572.alamo;

import org.usfirst.frc.team5572.alamo.StateMachine;

public abstract class StateMachine {
	
	boolean isRunning = false;
	boolean isDone = false;
	
	@SuppressWarnings("unused")
	private StateMachine[] machines = null;
	
	StateMachine nextState = null;
	StateMachine PreviousState = null;
	
	String machineName = "ZeberPupin";
	
	public StateMachine(String machineName) {
		this.machineName = machineName;
	}
	
	public void initMachine() {
		
	}
	
	public void cleanupMachine() {
		
	}	
	
	public void startMachine() {
		isRunning = true;
		isDone = false;
		initMachine();
	}
	
	public void startMachine(StateMachine[] nextStateMachines) {
		isRunning = true;
		isDone = false;
		machines = nextStateMachines;
		initMachine();
	}
	
	public void stopMachine() {
		isRunning = false;
	}

	public void setMachineDone() {
		isRunning = false;
		isDone = true;
		cleanupMachine();
		
	}
	
	public void reset() {
		isRunning = false;
		isDone = false;		
		
	}
	
	/**
	 * Main function, overload this one...
	 */
	public abstract void tick();
	
/****************************** Getter/Setter ************************/	
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public StateMachine getNextState() {
		return nextState;
	}

	public void setNextState(StateMachine nextState) {
		this.nextState = nextState;
	}

	public StateMachine getPreviousState() {
		return PreviousState;
	}

	public void setPreviousState(StateMachine previousState) {
		PreviousState = previousState;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	
}
