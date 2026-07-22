package org.actus.webapp.models;

import java.util.List;
import java.util.Map;

public class InputData {

    private Map<String,Object> contract;
    private List<ObservedData> riskFactors;
    private List<ObservedEvent> eventsObserved;

    public InputData() {
    }

    public InputData(Map<String,Object> contract, List<ObservedData> riskFactors, List<ObservedEvent> eventsObserved) {
        this.contract = contract;
        this.riskFactors = riskFactors;
        this.eventsObserved = eventsObserved;
    }

    public Map<String,Object> getContract() {
        return contract;
    }

    public void setContract(Map<String,Object> contract) {
        this.contract = contract;
    }

    public List<ObservedData> getRiskFactors() {
        return riskFactors;
    }

    public void setRiskFactors(List<ObservedData> riskFactors) {
        this.riskFactors = riskFactors;
    }

    public List<ObservedEvent> getEventsObserved() {
        return eventsObserved;
    }

    public void setEventsObserved(List<ObservedEvent> eventsObserved) {
        this.eventsObserved = eventsObserved;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InputData{");
        sb.append("contract='").append(contract).append('\'');
        sb.append(", riskFactors='").append(riskFactors).append('\'');
        sb.append(", eventsObserved='").append(eventsObserved).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
