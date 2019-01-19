package com.example.ecko.spots.model;

import android.location.LocationListener;
import android.location.LocationManager;

public class IncidentReport {

    private String spot;
    private String parque;
    private String description;
    private String type;

    public IncidentReport(String spot, String parque, String description) {
        this.spot = spot;
        this.parque = parque;
        this.description = description;
    }

    public IncidentReport(String spot, String parque, String description, String type) {
        this(spot, parque, description);
        this.type = type;
    }


    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParque() {
        return parque;
    }

    public void setParque(String parque) {
        this.parque = parque;
    }
}
