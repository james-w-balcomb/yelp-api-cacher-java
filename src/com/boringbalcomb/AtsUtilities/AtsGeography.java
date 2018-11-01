package com.boringbalcomb.AtsUtilities;

import java.util.ArrayList;
import java.util.List;

public class AtsGeography {

    public static List<CoordinatesLatLonDecDeg> getLatitudesLongitudes(List<Double> latitudes, List<Double> longitudes) {
        // List<CoordinatesLatLonDecDeg> listCoordinatesLatLonDecDeg = new ArrayList<>();
        // Example of valid ship coordinates off the coast of California :-)
        // shipCoordinates.add(new ShipCoordinates(36.385913, -127.441406));

        List<CoordinatesLatLonDecDeg> listCoordinatesLatLonDecDeg = new ArrayList<>();

        for(Double latitude : latitudes) {
            for(Double longitude : longitudes) {
                listCoordinatesLatLonDecDeg.add(new CoordinatesLatLonDecDeg(latitude, longitude));
            }
        }

        return listCoordinatesLatLonDecDeg;
    }

}
