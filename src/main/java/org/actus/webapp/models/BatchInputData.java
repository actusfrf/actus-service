package org.actus.webapp.models;

import java.util.List;
import java.util.Map;

public class BatchInputData {

    private List<Map<String,Object>> contracts;
    private List<ObservedData> riskFactors;
    private List<ObservedEvent> eventsObserved;

    public BatchInputData() {
    }

    public BatchInputData(List<Map<String,Object>> contracts, List<ObservedData> riskFactors, List<ObservedEvent> eventsObserved) {
        this.contracts = contracts;
        this.riskFactors = riskFactors;
        this.eventsObserved = eventsObserved;
    }

    public List<Map<String,Object>> getContracts() {
        return contracts;
    }

    public void setContracts(List<Map<String,Object>> contracts) {
        this.contracts = contracts;
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
        final StringBuilder sb = new StringBuilder("ActusData{");
        sb.append("contracts='").append(contracts).append('\'');
        sb.append(", riskFactors='").append(riskFactors).append('\'');
        sb.append(", eventsObserved='").append(eventsObserved).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
