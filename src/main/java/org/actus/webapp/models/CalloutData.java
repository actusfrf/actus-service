package org.actus.webapp.models;

public class CalloutData {
	private String modelID;
	private String time;       // in format yyyy:mm:ddT00:00;00 
	private String calloutType; // value AFD, MRD
	
	public CalloutData() {
	}
	public CalloutData(String modelID, String time, String calloutType) {
		this.modelID		= modelID;
		this.time    		= time;
		this.calloutType 	= calloutType;
	}
	
	public String getModelID() {
		return this.modelID;
	}
	public void setModelID(String modelID) {
		this.modelID = modelID;
	}
	
	public String getTime() {
		return this.time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getCalloutType() {
		return this.calloutType;
	}
	
	public void setCalloutType(String calloutType) {
		this.calloutType = calloutType;
	}
	
    @Override
    
    public String toString() {
        final StringBuilder sb = new StringBuilder("CalloutData{");
        sb.append("modelID'").append(modelID).append('\'');
        sb.append(", time='").append(time).append('\'');
        sb.append("calloutType='").append(calloutType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

