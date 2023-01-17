package com.rupesh.customeventlog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rupesh.customeventlog.model.Event;
import com.rupesh.customeventlog.model.EventType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Alerts")
public class Alert {
    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private EventType type;

    @JsonProperty("host")
    private String host;
    
    @JsonProperty("duration")
    private int duration;
    
    @JsonProperty("alert")
    private Boolean alert;

    public Alert() {
    }

    public Alert(Event event, int duration) {
        this.id = event.getId();
        this.type = event.getType();
        this.host = event.getHost();
        this.duration = duration;
        this.alert = Boolean.FALSE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Boolean getAlert() {
        return alert;
    }

    public void setAlert(Boolean alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
    
}
