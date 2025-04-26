package org.actus.webapp.models;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

import org.actus.states.StateSpace;
public class BehaviorStateAtInput {
	private String riskFactorId ;
	private LocalDateTime time;
	private StateSpace  states;
	
	public BehaviorStateAtInput() {		
	}
	public BehaviorStateAtInput(String riskFactorId, LocalDateTime time, StateSpace states) {
		this.riskFactorId = riskFactorId;
		this.time=time;
		this.states = states;
	}
	
	public String getRiskFactorId() {
		return this.riskFactorId;
	}
	public void setRiskFactorId(String riskFactorId) {
		this.riskFactorId = riskFactorId;
	}	
	public LocalDateTime getTime()  {
		return this.time;	
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public StateSpace getStates() {
		return this.states;
	}
	public void setStates(StateSpace states) {
		this.states = states;
	}
	 @Override
	public String toString() {
	final StringBuilder sb = new StringBuilder("BehaviorStateAtInput{");
	        sb.append("riskFactorId='").append(riskFactorId).append('\'');
	        sb.append("time='").append(time).append('\'');
	        sb.append(", states='").append(states).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }  
}
