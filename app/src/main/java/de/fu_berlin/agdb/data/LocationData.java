package de.fu_berlin.agdb.data;

import java.io.Serializable;

/**
 * Created by Riva on 16.06.2016.
 */
public class LocationData implements Serializable {

    Double latitude;
    Double longitude;
    String placeDescription;

    public LocationData(String placeDescription, Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeDescription = placeDescription;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }
}
