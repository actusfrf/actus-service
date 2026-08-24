package org.actus.webapp.models;

import java.util.Map;

public class ObservedEvent {

    private String time;
    private String type;
    private Double value;
    private String contractId;
    private Map<String,Object> states;

    public ObservedEvent() {
    }

    public ObservedEvent(String time, String type, Double value, String contractId, Map<String,Object> states) {
        this.time = time;
        this.type = type;
        this.value = value;
        this.contractId = contractId;
        this.states = states;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public Map<String,Object> getStates() {
        return states;
    }

    public void setStates(Map<String,Object> states) {
        this.states = states;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ObservedEvent{");
        sb.append("time='").append(time).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", contractId='").append(contractId).append('\'');
        sb.append(", states='").append(states).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
