package com.boringbalcomb.YelpApiCacher.BusinessSearch;

import com.boringbalcomb.AtsUtilities.AtsDateTime;
import com.boringbalcomb.AtsUtilities.AtsGeography;
import com.boringbalcomb.AtsUtilities.AtsMiscellaneous;
import com.boringbalcomb.AtsUtilities.CoordinatesLatLonDecDeg;
import com.boringbalcomb.YelpApiCacher.BusinessSearch.model.*;
import com.boringbalcomb.YelpApiCacher.YelpApiCacher;
import com.boringbalcomb.YelpApiCacher.constant.YelpApiV3;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.boringbalcomb.YelpApiCacher.YelpApiCacher.*;


public class BusinessSearchService {

    /*
        MAXIMUM: Build Yelp API BusinessSearchBusiness Search End-Point URL ()
         */
    private static String buildBusinessSearchUrl(String qspAttributes, String qspCategories, Double qspLatitude, Integer qspLimit, String qspLocale, String qspLocation, Double qspLongitude, Integer qspOffset, String qspOpenAt, String qspOpenNow, String qspPrice, Integer qspRadius, String qspSortBy, String qspTerm) {

        String uriScheme = YelpApiV3.YELP_API_V3_PROTOCOL;
        String uriAuthority = YelpApiV3.YELP_API_V3_HOSTNAME;
        String uriPath = YelpApiV3.YELP_API_V3_BASE_PATH;
        String uriQuery = "";
        String uriFragment = "";

        if (qspAttributes != null) {
            uriQuery = uriQuery + "&" + "attributes" + "=" + qspAttributes;
        }
        if (qspCategories != null) {
            uriQuery = uriQuery + "&" + "categories" + "=" + qspCategories;
        }
        if (qspLatitude != null) {
            uriQuery = uriQuery + "&" + "latitude" + "=" + qspLatitude;
        }
        if (qspLimit != null) {
            uriQuery = uriQuery + "&" + "limit" + "=" + qspLimit;
        }
        if (qspLocale != null) {
            uriQuery = uriQuery + "&" + "locale" + "=" + qspLocale;
        }
        if (qspLocation != null) {
            uriQuery = uriQuery + "&" + "location" + "=" + qspLocation;
        }
        if (qspLongitude != null) {
            uriQuery = uriQuery + "&" + "longitude" + "=" + qspLongitude;
        }
        if (qspOffset != null) {
            uriQuery = uriQuery + "&" + "offset" + "=" + qspOffset;
        }
        if (qspOpenAt != null) {
            uriQuery = uriQuery + "&" + "open_at" + "=" + qspOpenAt;
        }
        if (qspOpenNow != null) {
            uriQuery = uriQuery + "&" + "open_now" + "=" + qspOpenNow;
        }
        if (qspPrice != null) {
            uriQuery = uriQuery + "&" + "price" + "=" + qspPrice;
        }
        if (qspRadius != null) {
            uriQuery = uriQuery + "&" + "radius" + "=" + qspRadius;
        }
        if (qspSortBy != null) {
            uriQuery = uriQuery + "&" + "sort_by" + "=" + qspSortBy;
        }
        if (qspTerm != null) {
            uriQuery = uriQuery + "&" + "term" + "=" + qspTerm;
        }

        String businessSearchUrl;

        businessSearchUrl = uriScheme + "://" + uriAuthority + uriPath;

        if (uriQuery.equals("")) {
            if (uriQuery.startsWith("&")) {
                uriQuery = uriQuery.substring(1);
            }
            if (uriQuery.startsWith("?")) {
                uriQuery = uriQuery.substring(1);
            }
            businessSearchUrl = businessSearchUrl + "?" + uriQuery;
        }

        if (uriFragment.equals("")) {
            if (uriFragment.startsWith("#")) {
                uriFragment = uriFragment.substring(1);
            }
            businessSearchUrl = businessSearchUrl + "#" + uriFragment;
        }

        System.out.println("businessSearchUrl: " + businessSearchUrl);

        return businessSearchUrl;

    }

    /*
        MINIMUM: Build Yelp API BusinessSearchBusiness Search End-Point URL ()
         */
    public static String buildBusinessSearchUrl(String qspCategory, double qspLatitude, Integer qspLimit, double qspLongitude, Integer qspOffset) {

        String qspAttributes = null;
        String qspCategories = null;
        // double qspLatitude = null;
        // int qspLimit = null;
        String qspLocale = null;
        String qspLocation = null;
        // double longitude = null;
        // int qspOffset = null;
        String qspOpenAt = null;
        String qspOpenNow = null;
        String qspPrice = null;
        // int qspRadius = null;
        int qspRadius = YelpApiV3.DEFAULT_RADIUS_METERS;
        String qspSortBy = null;
        String qspTerm = null;

        String businessSearchUrl = buildBusinessSearchUrl(qspAttributes, qspCategories, qspLatitude, qspLimit, qspLocale, qspLocation, qspLongitude, qspOffset, qspOpenAt, qspOpenNow, qspPrice, qspRadius, qspSortBy, qspTerm);

        return businessSearchUrl;

    }

    private static Boolean checkExistingBusinessSearches(String category, Double latitude, Double longitude) {

        boolean existingBusinessSearches = false;

        final String sqlQueryString = "SELECT " +
                "COUNT(*) " +
                "FROM yelp_api_cacher.business_search_request " +
                "WHERE " +
                "qsp_categories = ? " +
                "AND " +
                "qsp_latitude = ? " +
                "AND " +
                "qsp_longitude = ? " +
                "ORDER BY NULL" +
                ";";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, category);
            javaSqlPreparedStatement.setDouble(2, latitude);
            javaSqlPreparedStatement.setDouble(3, longitude);

            ResultSet javaSqlResultSet = javaSqlPreparedStatement.executeQuery();

            javaSqlResultSet.next();

            int recordCount = javaSqlResultSet.getInt(1);

            if (recordCount > 0) {
                existingBusinessSearches = true;
            }

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        return existingBusinessSearches;

    }

    /*
         MINIMUM: Get BusinessSearchBusiness Searches ()
          */
    private static List<BusinessSearch> getBusinessSearches(String qspCategory, Double qspLatitude, Double qspLongitude) {
        //uriScheme
        //uriAuthority
        //uriPath
        //uriQuery
        //uriFragment
        //uriHostname
        //uriComplete
        //apiEndpointName
        //qspAttributes
        //qspCategories
        //qspLatitude
        Integer qspLimit = YelpApiV3.DEFAULT_RECORD_LIMIT;
        //qspLocale
        //qspLocation
        //qspLongitude
        Integer qspOffset = YelpApiV3.DEFAULT_RECORD_OFFSET;
        //qspOpenAt
        //qspOpenNow
        //qspPrice
        Integer qspRadius = YelpApiV3.DEFAULT_RADIUS_METERS;
        //qspSortBy
        //qspTerm

        List<BusinessSearch> businessSearches = getBusinessSearches(qspCategory, qspLatitude, qspLimit, qspLongitude, qspOffset, qspRadius);

        return businessSearches;

    }

    /*
         MAXIMUM: Get BusinessSearchBusiness Searches ()
         Handles Pagination
         limit+offset must be <= 1000
          */
    private static List<BusinessSearch> getBusinessSearches(String qspCategory, Double qspLatitude, Integer qspLimit, Double qspLongitude, Integer qspOffset, Integer qspRadius) {

        List<BusinessSearch> businessSearches = new ArrayList<>();

        Integer currentPageNumber = 1;
        int firstRecordNumber = currentPageNumber * qspLimit;
        int lastRecordNumber = (currentPageNumber * qspLimit) + qspLimit - 1;
        Integer totalBusinessCount;
        Integer currentBusinessCount;
        Integer cumulativeBusinessCount = 0;
        int cumulativeBusinessSearchCount;

        // ///////////// //
        // BEGIN: Cache  //
        // ///////////// //

//        if (USE_CACHE == true) {
//            // getCachedBusinessSearch();
//        } else {
//            // getApiBusinessSearch();
//        }

        // ///////////// //
        // END: Cache //
        // //////////// //

        // ////////////// //
        //                //
        // ////////////// //

        // Do-While
        do {

            // offset = {0,50,100,150,200,250,300,350,400,450,500,550,600,650,700,750,800,850,900,950,1000}
            // limit = 50
            // currentPageNumber = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20}
            // firstRecordNumber = {0,50,100,150,200,250,300,350,400,450,500,550,600,650,700,750,800,850,900,950,1000}
            // lastRecordNumber = {49,99,149,199,249,299,349,399,449,499,549,599,649,699,749,799,849,899,949,999,1049}

//            System.out.println("offset: " + qspOffset.toString());
//            System.out.println("limit: " + qspLimit.toString());
//            System.out.println("currentPageNumber: " + currentPageNumber.toString());
//            System.out.println("firstRecordNumber: " + Integer.toString(firstRecordNumber));
//            System.out.println("lastRecordNumber: " + Integer.toString(lastRecordNumber));

            // ////////////// //
            //                //
            // ////////////// //

            BusinessSearch businessSearch = getBusinessSearch(
                    qspCategory,
                    qspLatitude,
                    qspLimit,
                    qspLongitude,
                    qspOffset,
                    qspRadius);

            businessSearches.add(businessSearch);

            System.out.println("businessSearch: " + businessSearch);

            // ////////////// //
            //                //
            // ////////////// //

            totalBusinessCount = businessSearch.getTotal();
            if (businessSearch.getBusinesses() == null) {
                currentBusinessCount = null;
                System.out.println("businessSearch.getBusinesses() == null");
                System.exit(-1);
            } else {
                    currentBusinessCount = businessSearch.getBusinesses().size();
            }
            cumulativeBusinessCount = cumulativeBusinessCount + currentBusinessCount;
            cumulativeBusinessSearchCount = businessSearches.size();

//            System.out.println("totalBusinessCount: " + totalBusinessCount);
//            System.out.println("currentBusinessCount: " + currentBusinessCount);
//            System.out.println("cumulativeBusinessCount: " + cumulativeBusinessCount);
//            System.out.println("cumulativeBusinessSearchCount: " + cumulativeBusinessSearchCount);

            currentPageNumber = currentPageNumber + 1;
            qspOffset = qspOffset + qspLimit;
            firstRecordNumber = currentPageNumber * qspLimit;
            lastRecordNumber = (currentPageNumber * qspLimit) + qspLimit - 1;

        } while (cumulativeBusinessCount < totalBusinessCount & qspLimit + qspOffset <= 1000);

//        System.out.println("FINAL: offset: " + qspOffset.toString());
//        System.out.println("FINAL: limit: " + qspLimit.toString());
//        System.out.println("FINAL: currentPageNumber: " + currentPageNumber.toString());
//        System.out.println("FINAL: firstRecordNumber: " + Integer.toString(firstRecordNumber));
//        System.out.println("FINAL: lastRecordNumber: " + Integer.toString(lastRecordNumber));
//        System.out.println("FINAL: totalBusinessCount: " + totalBusinessCount);
//        System.out.println("FINAL: currentBusinessCount: " + currentBusinessCount);
//        System.out.println("FINAL: cumulativeBusinessCount: " + cumulativeBusinessCount);
//        System.out.println("FINAL: cumulativeBusinessSearchCount: " + cumulativeBusinessSearchCount);

        // ////////////// //
        //                //
        // ////////////// //

//        System.out.println("FINAL: businessSearches: " + businessSearches);

        return businessSearches;

    }

    /*
        MINIMUM: Get BusinessSearchBusiness Search ()
        Handles individual requests
         */
    private static BusinessSearch getBusinessSearch(String qspCategory, Double qspLatitude, Integer qspLimit, Double qspLongitude, Integer qspOffset, Integer qspRadius) {

        String uriScheme = "https";
        String uriAuthority = "api.yelp.com";
        String uriPath = "/v3/businesses/search";

        String qspAttributes = "";
        String qspLocale = "";
        String qspLocation = "";
        String qspOpenAt = "";
        String qspOpenNow = "";
        String qspPrice = "";
        String qspSortBy = "";
        String qspTerm = "";

        String uriQuery =
                "?" + "attributes" + "=" + qspAttributes +
                "&" + "categories" + "=" + qspCategory +
                "&" + "latitude" + "=" + qspLatitude.toString() +
                "&" + "limit" + "=" + qspLimit.toString() +
                "&" + "locale" + "=" + qspLocale +
                "&" + "location" + "=" + qspLocation +
                "&" + "longitude" + "=" + qspLongitude.toString() +
                "&" + "offset" + "=" + qspOffset.toString() +
                "&" + "open_at" + "=" + qspOpenAt +
                "&" + "open_now" + "=" + qspOpenNow +
                "&" + "price" + "=" + qspPrice +
                "&" + "radius" + "=" + qspRadius.toString() +
                "&" + "sort_by" + "=" + qspSortBy +
                "&" + "term" + "=" + qspTerm;

        String uriFragment = "";

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(uriScheme);
        uriBuilder.setHost(uriAuthority);
        uriBuilder.setPath(uriPath);
        uriBuilder.setParameter("latitude", qspLatitude.toString());
        uriBuilder.setParameter("longitude", qspLongitude.toString());
        uriBuilder.setParameter("radius", qspRadius.toString());
        uriBuilder.setParameter("categories", qspCategory);
        uriBuilder.setParameter("offset", qspOffset.toString());
        uriBuilder.setParameter("limit", qspLimit.toString());

//        uriBuilder.getScheme();
//        uriBuilder.getHost();
//        uriBuilder.getPort();
//        uriBuilder.getPath();
        uriBuilder.getQueryParams();
//        uriBuilder.getFragment();
//        uriBuilder.getCharset();
//        uriBuilder.getUserInfo();
//        uriBuilder.getClass();

        URI uri = null;
        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException ex) {
            System.out.println("java.net.URISyntaxException");
            ex.printStackTrace();
        }

        HttpGet httpGet = new HttpGet();
        httpGet.setHeader(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT);
        httpGet.addHeader("Accept", "application/json");
        httpGet.addHeader("Authorization", "Bearer " + API_KEY);
        httpGet.setURI(uri);
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (ClientProtocolException ex) {
            System.out.println("org.apache.http.client.ClientProtocolException");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("java.io.IOException");
            ex.printStackTrace();
        }

        BufferedReader httpResponseEntityContent = null;
        try {
            if (httpResponse != null) {
                httpResponseEntityContent = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            }
        } catch (NullPointerException ex) {
            System.out.println("java.lang.NullPointerException");
            ex.printStackTrace();
        } catch (IOException ex) {
            // java.io.IOException
            System.out.println("java.io.IOException");
            ex.printStackTrace();
        }
        // TODO(JamesBalcomb) Use EntityUtils.toString() as a faster way to get entity content
        // String body = EntityUtils.toString(response.getEntity(), "UTF-8");

        StringBuilder response = new StringBuilder();
        String inputLine;
        try {
            if (httpResponseEntityContent != null) {
                while ((inputLine = httpResponseEntityContent.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        } catch (IOException ex) {
            System.out.println("java.io.IOException");
            ex.printStackTrace();
        }

        String jsonString = response.toString();
        BusinessSearch businessSearch = new BusinessSearch();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            businessSearch = objectMapper.readValue(jsonString, BusinessSearch.class);
        } catch (JsonParseException ex) {
            // com.fasterxml.jackson.core.JsonParseException
            System.out.println("com.fasterxml.jackson.core.JsonParseException");
            ex.printStackTrace();
        } catch (JsonMappingException ex) {
            // com.fasterxml.jackson.databind.JsonMappingException
            System.out.println("com.fasterxml.jackson.databind.JsonMappingException");
            ex.printStackTrace();
        } catch (IOException ex) {
            // java.io.IOException
            System.out.println("java.io.IOException");
            ex.printStackTrace();
        }

        if (SAVE_FILES == true) {

            String fileName = "saves/business_search(" + qspCategory + ")(" + qspLatitude + ")(" + qspLongitude + ")(" + qspRadius + ")(" + qspOffset + ")(" + qspLimit + ").json";
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(fileName);
            } catch (FileNotFoundException ex) {
                System.out.println("java.io.FileNotFoundException");
                ex.printStackTrace();
            }

            if (fileOutputStream != null) {
                try {
                    objectMapper.writeValue(fileOutputStream, businessSearch);
                } catch (JsonMappingException ex) {
                    System.out.println("com.fasterxml.jackson.databind.JsonMappingException");
                    ex.printStackTrace();
                } catch (JsonGenerationException ex) {
                    System.out.println("com.fasterxml.jackson.core.JsonGenerationException");
                    ex.printStackTrace();
                } catch (IOException ex) {
                    System.out.println("java.io.IOException");
                    ex.printStackTrace();
                }
            }

            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException ex) {
                System.out.println("java.io.IOException");
                ex.printStackTrace();
            }

        }


        saveBusinessSearchRequest(
                uriScheme,
                uriAuthority,
                uriPath,
                uriQuery,
                uriFragment,
                qspAttributes,
                qspCategory,
                qspLatitude,
                qspLimit,
                qspLocale,
                qspLocation,
                qspLongitude,
                qspOffset,
                qspOpenAt,
                qspOpenNow,
                qspPrice,
                qspRadius,
                qspSortBy,
                qspTerm);

        return businessSearch;
    }

    private static void saveBusinessSearchRequest(String uriScheme, String uriAuthority, String uriPath, String uriQuery, String uriFragment, String queryAttributes, String queryCategory, Double queryLatitude, Integer queryLimit, String queryLocale, String queryLocation, Double queryLongitude, Integer queryOffset, String queryOpenAt, String queryOpenNow, String queryPrice, Integer queryRadius, String querySortBy, String queryTerm) {

        if (uriScheme.endsWith("://")) {
            uriScheme = uriScheme.substring(0, uriScheme.length() - 3);
        }

        if (uriAuthority.endsWith("/")) {
            uriAuthority = uriAuthority.substring(0, uriAuthority.length() - 1);
        }

//        if(uriPath.startsWith("/")){
//            uriPath = uriPath.substring(1, uriPath.length());
//        }

        if (uriPath.endsWith("/")) {
            uriPath = uriPath.substring(0, uriPath.length() - 1);
        }

        if (uriQuery.startsWith("?")) {
            uriQuery = uriQuery.substring(1);
        }

        if (uriFragment.startsWith("#")) {
            uriFragment = uriFragment.substring(1);
        }

        String uriComplete = uriScheme + "://" + uriAuthority + "/" + uriPath + "?" + uriQuery + "#" + uriFragment;

        String apiEndpointName = "business-search";

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_request " +
                "(" +
                "uri_scheme, " +
                "uri_authority, " +
                "uri_path, " +
                "uri_query, " +
                "uri_fragment, " +
                "uri_hostname, " +
                "uri_complete, " +
                "api_endpoint_name, " +
                "qsp_attributes, " +
                "qsp_categories, " +
                "qsp_latitude, " +
                "qsp_limit, " +
                "qsp_locale, " +
                "qsp_location, " +
                "qsp_longitude, " +
                "qsp_offset, " +
                "qsp_open_at, " +
                "qsp_open_now, " +
                "qsp_price, " +
                "qsp_radius, " +
                "qsp_sort_by, " +
                "qsp_term, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, uriScheme);
            javaSqlPreparedStatement.setString(2, uriAuthority);
            javaSqlPreparedStatement.setString(3, uriPath);
            javaSqlPreparedStatement.setString(4, uriQuery);
            javaSqlPreparedStatement.setString(5, uriFragment);
            javaSqlPreparedStatement.setString(6, uriAuthority);
            javaSqlPreparedStatement.setString(7, uriComplete);
            javaSqlPreparedStatement.setString(8, apiEndpointName);
            javaSqlPreparedStatement.setString(9, queryAttributes);
            javaSqlPreparedStatement.setString(10, queryCategory);
            javaSqlPreparedStatement.setDouble(11, queryLatitude);
            javaSqlPreparedStatement.setInt(12, queryLimit);
            javaSqlPreparedStatement.setString(13, queryLocale);
            javaSqlPreparedStatement.setString(14, queryLocation);
            javaSqlPreparedStatement.setDouble(15, queryLongitude);
            javaSqlPreparedStatement.setInt(16, queryOffset);
            javaSqlPreparedStatement.setString(17, queryOpenAt);
            javaSqlPreparedStatement.setString(18, queryOpenNow);
            javaSqlPreparedStatement.setString(19, queryPrice);
            javaSqlPreparedStatement.setInt(20, queryRadius);
            javaSqlPreparedStatement.setString(21, querySortBy);
            javaSqlPreparedStatement.setString(22, queryTerm);
            javaSqlPreparedStatement.setTimestamp(23, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            // java.sql.SQLException
            System.out.println("java.sql.SQLException");
            // handle any errors
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    private static void saveBusinessSearches(List<BusinessSearch> businessSearches) {

        for (BusinessSearch businessSearch : businessSearches) {

            saveBusinessSearch(businessSearch);

        }

    }

    private static void saveBusinessSearch(BusinessSearch businessSearch) {

        List<BusinessSearchBusiness> businessSearchBusinesses = businessSearch.getBusinesses();
        Integer businessSearchTotal = businessSearch.getTotal();
        BusinessSearchRegion businessSearchRegion = businessSearch.getRegion();
        BusinessSearchError businessSearchError = businessSearch.getError();

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search " +
                "(" +
                "business_search_businesses, " +
                "business_search_total, " +
                "business_search_region, " +
                "business_search_error, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessSearch.getBusinesses().toString());
            javaSqlPreparedStatement.setInt(2, businessSearch.getTotal());
            javaSqlPreparedStatement.setString(3, businessSearch.getRegion().toString());
            if (businessSearch.getError() == null) {
                javaSqlPreparedStatement.setString(4, "NULL");
            } else {
                javaSqlPreparedStatement.setString(4, businessSearch.getError().toString());
            }
            javaSqlPreparedStatement.setTimestamp(5, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        saveBusinessSearchBusinesses(businessSearchBusinesses);
        saveBusinessSearchTotal(businessSearchTotal);
        saveBusinessSearchRegion(businessSearchRegion);
        saveBusinessSearchError(businessSearchError);

    }

    private static void saveBusinessSearchBusinesses(List<BusinessSearchBusiness> businessSearchBusinesses) {

        for (BusinessSearchBusiness businessSearchBusiness : businessSearchBusinesses) {
            saveBusinessSearchBusiness(businessSearchBusiness);
        }

    }

    private static void saveBusinessSearchBusiness(BusinessSearchBusiness businessSearchBusiness) {

        List<BusinessSearchBusinessCategory> businessSearchBusinessCategories = businessSearchBusiness.getCategories();
        BusinessSearchBusinessCoordinates businessSearchBusinessCoordinates = businessSearchBusiness.getCoordinates();
        BusinessSearchBusinessLocation businessSearchBusinessLocation = businessSearchBusiness.getLocation();

        String businessId = businessSearchBusiness.getId();

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_business " +
                "(" +
                "business_id, " +
                "alias, " +
                "business_name, " +
                "image_url, " +
                "is_closed, " +
                "url, " +
                "review_count, " +
                "categories, " +
                "rating, " +
                "coordinates, " +
                "transactions, " +
                "price, " +
                "location, " +
                "phone, " +
                "display_phone, " +
                "getDistanceBetweenCoordinates, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, businessSearchBusiness.getAlias());
            javaSqlPreparedStatement.setString(3, businessSearchBusiness.getName());
            javaSqlPreparedStatement.setString(4, businessSearchBusiness.getImageUrl());
            javaSqlPreparedStatement.setBoolean(5, businessSearchBusiness.getIsClosed());
            javaSqlPreparedStatement.setString(6, businessSearchBusiness.getUrl());
            javaSqlPreparedStatement.setInt(7, businessSearchBusiness.getReviewCount());
            javaSqlPreparedStatement.setString(8, businessSearchBusiness.getCategories().toString());
            javaSqlPreparedStatement.setDouble(9, businessSearchBusiness.getRating());
            javaSqlPreparedStatement.setString(10, businessSearchBusiness.getCoordinates().toString());
            javaSqlPreparedStatement.setString(11, businessSearchBusiness.getTransactions().toString());
            javaSqlPreparedStatement.setString(12, businessSearchBusiness.getPrice());
            javaSqlPreparedStatement.setString(13, businessSearchBusiness.getLocation().toString());
            javaSqlPreparedStatement.setString(14, businessSearchBusiness.getPhone());
            javaSqlPreparedStatement.setString(15, businessSearchBusiness.getDisplayPhone());
            javaSqlPreparedStatement.setDouble(16, businessSearchBusiness.getDistance());
            javaSqlPreparedStatement.setTimestamp(17, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            // java.sql.SQLException
            System.out.println("java.sql.SQLException");
            // handle any errors
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        saveBusinessSearchBusinessCategories(businessId, businessSearchBusinessCategories);
        saveBusinessSearchBusinessCoordinates(businessId, businessSearchBusinessCoordinates);
        saveBusinessSearchBusinessLocation(businessId, businessSearchBusinessLocation);

    }

    private static void saveBusinessSearchBusinessCategories(String businessId, List<BusinessSearchBusinessCategory> businessSearchBusinessCategories) {

        for (BusinessSearchBusinessCategory businessSearchBusinessCategory : businessSearchBusinessCategories) {

            saveBusinessSearchBusinessCategory(businessId, businessSearchBusinessCategory);

        }

    }

    private static void saveBusinessSearchBusinessCategory(String businessId, BusinessSearchBusinessCategory businessSearchBusinessCategory) {

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_business_category " +
                "(" +
                "business_id, " +
                "alias, " +
                "title, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, businessSearchBusinessCategory.getAlias());
            javaSqlPreparedStatement.setString(3, businessSearchBusinessCategory.getTitle());
            javaSqlPreparedStatement.setTimestamp(4, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    private static void saveBusinessSearchBusinessCoordinates(String businessId, BusinessSearchBusinessCoordinates businessSearchBusinessCategories) {

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_business_coordinates " +
                "(" +
                "business_id, " +
                "latitude, " +
                "longitude, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setObject(2, businessSearchBusinessCategories.getLatitude(), Types.DECIMAL);
            javaSqlPreparedStatement.setObject(3, businessSearchBusinessCategories.getLongitude(), Types.DECIMAL);
            javaSqlPreparedStatement.setTimestamp(4, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    private static void saveBusinessSearchBusinessLocation(String businessId, BusinessSearchBusinessLocation businessSearchBusinessLocation) {

        List<String> businessSearchBusinessLocationDisplayAddress = businessSearchBusinessLocation.getDisplayAddress();

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_business_location " +
                "(" +
                "business_id, " +
                "address1, " +
                "address2, " +
                "address3, " +
                "city, " +
                "state, " +
                "zip_code, " +
                "country, " +
                "display_address, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, businessSearchBusinessLocation.getAddress1());
            javaSqlPreparedStatement.setString(3, businessSearchBusinessLocation.getAddress2());
            javaSqlPreparedStatement.setString(4, businessSearchBusinessLocation.getAddress3());
            javaSqlPreparedStatement.setString(5, businessSearchBusinessLocation.getCity());
            javaSqlPreparedStatement.setString(6, businessSearchBusinessLocation.getState());
            javaSqlPreparedStatement.setString(7, businessSearchBusinessLocation.getZipCode());
            javaSqlPreparedStatement.setString(8, businessSearchBusinessLocation.getCountry());
            javaSqlPreparedStatement.setString(9, businessSearchBusinessLocation.getDisplayAddress().toString());
            javaSqlPreparedStatement.setTimestamp(10, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        saveBusinessSearchBusinessLocationDisplayAddress(businessId, businessSearchBusinessLocationDisplayAddress);

    }

    private static void saveBusinessSearchBusinessLocationDisplayAddress(String businessId, List<String> businessSearchBusinessLocationDisplayAddress) {

        // TODO(JamesBalcomb): refactor this to use PreparedStatement.setObject(int parameterIndex, Object x, int targetSqlType)

        String displayAddress01;
        String displayAddress02;
        String displayAddress03;
        String displayAddress04;
        String displayAddress05;
        String displayAddress06;
        String displayAddress07;
        String displayAddress08;
        String displayAddress09;

        if (businessSearchBusinessLocationDisplayAddress.size() >= 1) {
            displayAddress01 = businessSearchBusinessLocationDisplayAddress.get(0);
        } else {
            displayAddress01 = "NULL";
        }
        if (businessSearchBusinessLocationDisplayAddress.size() >= 2) {
            displayAddress02 = businessSearchBusinessLocationDisplayAddress.get(1);
        } else {
            displayAddress02 = "NULL";
        }
        if (businessSearchBusinessLocationDisplayAddress.size() >= 3) {
            displayAddress03 = businessSearchBusinessLocationDisplayAddress.get(2);
        } else {
            displayAddress03 = "NULL";
        }
        if (businessSearchBusinessLocationDisplayAddress.size() >= 4) {
            displayAddress04 = businessSearchBusinessLocationDisplayAddress.get(3);
        } else {
            displayAddress04 = "NULL";
        }
        if (businessSearchBusinessLocationDisplayAddress.size() >= 5) {
            displayAddress05 = businessSearchBusinessLocationDisplayAddress.get(4);
        } else {
            displayAddress05 = "NULL";
        }
        if (businessSearchBusinessLocationDisplayAddress.size() >= 6) {
            displayAddress06 = businessSearchBusinessLocationDisplayAddress.get(5);
        } else {
            displayAddress06 = "NULL";
        }
        if (businessSearchBusinessLocationDisplayAddress.size() >= 7) {
            displayAddress07 = businessSearchBusinessLocationDisplayAddress.get(6);
        } else {
            displayAddress07 = "NULL";
        }
        if (businessSearchBusinessLocationDisplayAddress.size() >= 8) {
            displayAddress08 = businessSearchBusinessLocationDisplayAddress.get(7);
        } else {
            displayAddress08 = "NULL";
        }
        if (businessSearchBusinessLocationDisplayAddress.size() >= 9) {
            displayAddress09 = businessSearchBusinessLocationDisplayAddress.get(8);
        } else {
            displayAddress09 = "NULL";
        }

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_business_location_display_address " +
                "(" +
                "business_id, " +
                "display_address_01, " +
                "display_address_02, " +
                "display_address_03, " +
                "display_address_04, " +
                "display_address_05, " +
                "display_address_06, " +
                "display_address_07, " +
                "display_address_08, " +
                "display_address_09, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, displayAddress01);
            javaSqlPreparedStatement.setString(3, displayAddress02);
            javaSqlPreparedStatement.setString(4, displayAddress03);
            javaSqlPreparedStatement.setString(5, displayAddress04);
            javaSqlPreparedStatement.setString(6, displayAddress05);
            javaSqlPreparedStatement.setString(7, displayAddress06);
            javaSqlPreparedStatement.setString(8, displayAddress07);
            javaSqlPreparedStatement.setString(9, displayAddress08);
            javaSqlPreparedStatement.setString(10, displayAddress09);
            javaSqlPreparedStatement.setTimestamp(11, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    private static void saveBusinessSearchError(BusinessSearchError businessSearchError) {

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_error " +
                "(" +
                "error_code, " +
                "error_description, " +
                "error_field, " +
                "error_instance, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            if (businessSearchError != null) {
                javaSqlPreparedStatement.setString(1, businessSearchError.getCode());
                javaSqlPreparedStatement.setString(2, businessSearchError.getDescription());
                javaSqlPreparedStatement.setString(3, businessSearchError.getField());
                javaSqlPreparedStatement.setString(4, businessSearchError.getInstance());
                javaSqlPreparedStatement.setTimestamp(5, AtsDateTime.getCurrentTimeStamp());
            } else {
                javaSqlPreparedStatement.setString(1, "NULL");
                javaSqlPreparedStatement.setString(2, "NULL");
                javaSqlPreparedStatement.setString(3, "NULL");
                javaSqlPreparedStatement.setString(4, "NULL");
                javaSqlPreparedStatement.setTimestamp(5, AtsDateTime.getCurrentTimeStamp());
            }

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    private static void saveBusinessSearchRegion(BusinessSearchRegion businessSearchRegion) {

        BusinessSearchRegionCenter businessSearchRegionCenter = businessSearchRegion.getCenter();

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_region " +
                "(" +
                "center, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessSearchRegion.getCenter().toString());
            javaSqlPreparedStatement.setTimestamp(2, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        saveBusinessSearchRegionCenter(businessSearchRegionCenter);

    }

    private static void saveBusinessSearchRegionCenter(BusinessSearchRegionCenter businessSearchRegionCenter) {

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_region_center " +
                "(" +
                "latitude, " +
                "longitude, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessSearchRegionCenter.getLatitude().toString());
            javaSqlPreparedStatement.setString(2, businessSearchRegionCenter.getLongitude().toString());
            javaSqlPreparedStatement.setTimestamp(3, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    private static void saveBusinessSearchTotal(Integer businessSearchTotal) {

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_search_total " +
                "(" +
                "total, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessSearchTotal.toString());
            javaSqlPreparedStatement.setTimestamp(2, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    public static void processBusinessSearch() {

        // //////////////////////// //
        // BEGIN: For Each Category //
        // //////////////////////// //

        List<String> categories = Arrays.asList("arts", "nightlife");

        int current_loop_count = 0;

        for (String category : categories) {

            // ////////////////////////// //
            // BEGIN: For Each Coordinate //
            // ////////////////////////// //

            List<CoordinatesLatLonDecDeg> listCoordinatesLatLonDecDeg = AtsGeography.getLatitudesLongitudes(LATITUDES, LONGITUDES);

            for (CoordinatesLatLonDecDeg coordinatesLatLonDecDeg : listCoordinatesLatLonDecDeg) {

                double latitude = coordinatesLatLonDecDeg.getLatitude();
                double longitude = coordinatesLatLonDecDeg.getLongitude();

                // ////////////////////////////////////// //
                // BEGIN: Process Business Search Request //
                // ////////////////////////////////////// //

                Boolean existingBusinessSearches = checkExistingBusinessSearches(category, latitude, longitude);

                if (existingBusinessSearches == true) {
                    System.out.println("Already Exists: (" + category + ", " + latitude + ", " + longitude + ")");
                    continue;
                }

                // TODO(JamesBalcomb): Add check for error={}
                List<BusinessSearch> businessSearches = getBusinessSearches(category, latitude, longitude);

                saveBusinessSearches(businessSearches);

                // //////////////////////////////////// //
                // END: Process Business Search Request //
                // //////////////////////////////////// //

                current_loop_count = current_loop_count + 1;
                if (current_loop_count >= YelpApiCacher.MAXIMUM_LOOP_COUNT) {
                    // java.lang.System.exit() method
                    // System.out.println("Le Fin.");
                    // System.exit(0);
                    break;
                }

                AtsMiscellaneous.pause();

            }

            // //////////////////////// //
            // END: For Each Coordinate //
            // //////////////////////// //

            // ////////////////////// //
            // END: For Each Category //
            // ////////////////////// //

        }
    }
}
