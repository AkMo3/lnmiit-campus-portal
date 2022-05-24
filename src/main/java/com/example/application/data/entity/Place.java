package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Place {

    @NotEmpty
    @Id
    private String name;

    private String googleMapsIdentifier;

    private String minDistance;

    public Place(String name) {
        this.name = name;
    }

    public Place() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoogleMapsIdentifier() {
        return googleMapsIdentifier;
    }

    public void setGoogleMapsIdentifier(String googleMapsIdentifier) {
        this.googleMapsIdentifier = googleMapsIdentifier;
    }

    public String getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(String minDistance) {
        this.minDistance = minDistance;
    }
}
