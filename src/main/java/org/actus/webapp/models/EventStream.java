package org.actus.webapp.models;

import java.util.List;

public class EventStream {

    private String contractID;
    private String status;
    private String message;
    private List<Event> events;

    public EventStream() {
    }

    public EventStream(String contractID, String status, String message, List<Event> events) {
        this.contractID = contractID;
        this.status = status;
        this.message = message;
        this.events = events;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventStream{");
        sb.append("contractID='").append(contractID).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", events='").append(events).append('\'');
        sb.append('}');
        return sb.toString();
    }
}