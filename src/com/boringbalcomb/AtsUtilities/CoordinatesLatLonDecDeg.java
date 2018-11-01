package com.boringbalcomb.AtsUtilities;

public class CoordinatesLatLonDecDeg {

    private double latitude;
    private double longitude;

    CoordinatesLatLonDecDeg(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "CoordinatesLatLonDecDeg{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
