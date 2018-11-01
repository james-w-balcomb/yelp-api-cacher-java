package com.boringbalcomb.AtsUtilities;

import java.util.ArrayList;
import java.util.List;

public class AtsGeography {

    public static final Double PI = 3.141592653589793; // Math.PI

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

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::                                                                         :*/
    /*::  This routine calculates the getDistanceBetweenCoordinates between two points (given the     :*/
    /*::  latitude/longitude of those points). It is being used to calculate     :*/
    /*::  the getDistanceBetweenCoordinates between two locations using GeoDataSource (TM) products  :*/
    /*::                                                                         :*/
    /*::  Definitions:                                                           :*/
    /*::    South latitudes are negative, east longitudes are positive           :*/
    /*::                                                                         :*/
    /*::  Passed to function:                                                    :*/
    /*::    lat1, lon1 = Latitude and Longitude of point 1 (in decimal degrees)  :*/
    /*::    lat2, lon2 = Latitude and Longitude of point 2 (in decimal degrees)  :*/
    /*::    unit = the unit you desire for results                               :*/
    /*::           where: 'M' is statute miles (default)                         :*/
    /*::                  'K' is kilometers                                      :*/
    /*::                  'N' is nautical miles                                  :*/
    /*::  Worldwide cities and other features databases with latitude longitude  :*/
    /*::  are available at https://www.geodatasource.com                          :*/
    /*::                                                                         :*/
    /*::  For enquiries, please contact sales@geodatasource.com                  :*/
    /*::                                                                         :*/
    /*::  Official Web site: https://www.geodatasource.com                        :*/
    /*::                                                                         :*/
    /*::           GeoDataSource.com (C) All Rights Reserved 2017                :*/
    /*::                                                                         :*/
    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public static double getDistanceBetweenCoordinates(double firstLatitude, double firstLongitude, double secondLatitude, double secondLongitude, char units) {

        double distance;

        if (firstLatitude == secondLatitude && firstLongitude == secondLongitude) {

            distance = 0;

        } else {

            double theta = firstLongitude - secondLongitude;

            distance = Math.sin(convertDegreesToRadians(firstLatitude)) * Math.sin(convertDegreesToRadians(secondLatitude)) + Math.cos(convertDegreesToRadians(firstLatitude)) * Math.cos(convertDegreesToRadians(secondLatitude)) * Math.cos(convertDegreesToRadians(theta));

            distance = Math.acos(distance);

            distance = convertRadiansToDegrees(distance);

            distance = distance * 60 * 1.1515;

            if (units == 'K') {

                distance = distance * 1.609344;

            } else if (units == 'N') {

                distance = distance * 0.8684;

            }


        }

        return (distance);

    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double convertDegreesToRadians(double degrees) {

        double radians = (degrees * PI / 180.0);

        return radians;
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double convertRadiansToDegrees(double radians) {

        double degrees = (radians * 180 / PI);

        return degrees;

    }

}
