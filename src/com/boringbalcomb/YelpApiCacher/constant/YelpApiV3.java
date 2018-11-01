package com.boringbalcomb.YelpApiCacher.constant;

public class YelpApiV3 {

    public static final Integer TIME_TO_LIVE = 2880; // Seconds in a Day: 24 Hours * 60 Minutes * 60 Seconds
    public static final String DEFAULT_EXPIRATION_TIMESTAMP_FORMAT_STRING = "%Y-%m-%d 23:59:59";

    public static final String YELP_API_V3_PROTOCOL = "https";
    public static final String YELP_API_V3_HOSTNAME = "api.yelp.com";
    public static final String YELP_API_V3_BASE_PATH = "/api.yelp.com/v3";

    public static final Integer DEFAULT_RECORD_LIMIT = 50;
    public static final Integer DEFAULT_RECORD_OFFSET = 0;
    public static final Integer DEFAULT_RADIUS_METERS = 202; // 1/8 || 0.125 mile =  201.168 meters

}
