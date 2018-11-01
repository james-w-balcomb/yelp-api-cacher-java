package com.boringbalcomb.YelpApiCacher.BusinessDetails;

import com.boringbalcomb.AtsUtilities.AtsDateTime;
import com.boringbalcomb.AtsUtilities.AtsMiscellaneous;
import com.boringbalcomb.YelpApiCacher.BusinessDetails.model.*;
import com.boringbalcomb.YelpApiCacher.YelpApiCacher;
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
import java.util.List;

import static com.boringbalcomb.YelpApiCacher.YelpApiCacher.API_KEY;
import static com.boringbalcomb.YelpApiCacher.YelpApiCacher.MYSQL_CONNECTION_STRING;
import static com.boringbalcomb.YelpApiCacher.YelpApiCacher.SAVE_FILES;

public class BusinessDetailsService {

    private static List<String> getBusinessIdsForDetails() {

        List<String> businessIds = new ArrayList<>();

        String sqlQueryString =
                "SELECT DISTINCT business_id " +
                        "FROM yelp_api_cacher.business_search_business " +
                        "WHERE NOT EXISTS(" +
                        "SELECT 'nothing' FROM yelp_api_cacher.business_details " +
                        "WHERE business_search_business.business_id = business_details.business_id ORDER BY NULL) " +
                        "ORDER BY NULL;";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString);
             ResultSet javaSqlResultSet = javaSqlPreparedStatement.executeQuery()) {

            while (javaSqlResultSet.next()) {
                businessIds.add(javaSqlResultSet.getString(1));
            }

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        return businessIds;

    }

    private static BusinessDetails getBusinessDetails(String businessId) {

        String uriScheme = "https";
        String uriAuthority = "api.yelp.com";
        String uriPath = "/v3/businesses";

        uriPath = uriPath + "/" + businessId;

        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(uriScheme);
        uriBuilder.setHost(uriAuthority);
        uriBuilder.setPath(uriPath);

        URI uri = null;
        try {
            uri = uriBuilder.build();
        } catch (URISyntaxException ex) {
            System.out.println("java.net.URISyntaxException");
            ex.printStackTrace();
        }

        // System.out.println("uri: " + uri);

        HttpGet httpGet = new HttpGet();
        httpGet.setHeader(HttpHeaders.USER_AGENT, HttpHeaders.USER_AGENT);
        httpGet.addHeader("Accept" , "application/json");
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
            System.out.println("java.io.IOException");
            ex.printStackTrace();
        }
        // TODO(JamesBalcomb): Use EntityUtils.toString() as a faster way to get entity content
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

        BusinessDetails businessDetails = new BusinessDetails();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            businessDetails = objectMapper.readValue(jsonString, BusinessDetails.class);
        }  catch (JsonParseException ex) {
            System.out.println("com.fasterxml.jackson.core.JsonParseException");
            ex.printStackTrace();
        } catch (JsonMappingException ex) {
            System.out.println("com.fasterxml.jackson.databind.JsonMappingException");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("java.io.IOException");
            ex.printStackTrace();
        }

        if (SAVE_FILES == true) {

            String fileName = "saves/business_details(" + businessId + ").json";
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(fileName);
            } catch (FileNotFoundException ex) {
                System.out.println("java.io.FileNotFoundException");
                ex.printStackTrace();
            }

            if (fileOutputStream != null) {
                try {
                    objectMapper.writeValue(fileOutputStream, businessDetails);
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


        return businessDetails;
    }

    private static void saveBusinessDetails(BusinessDetails businessDetails) {

        List<BusinessDetailsCategory> businessDetailsCategories = businessDetails.getCategories();
        BusinessDetailsCoordinates businessDetailsCoordinates = businessDetails.getCoordinates();
        BusinessDetailsError businessDetailsError = businessDetails.getError();
        List<BusinessDetailsHour> businessDetailsHours = businessDetails.getHours();
        BusinessDetailsLocation businessDetailsLocation = businessDetails.getLocation();

        String businessId = businessDetails.getId();

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_details " +
                "(" +
                "business_id, " +
                "business_name, " +
                "business_alias, " +
                "categories, " +
                "coordinates, " +
                "display_phone, " +
                "hours, " +
                "image_url, " +
                "is_claimed, " +
                "is_closed, " +
                "location, " +
                "phone, " +
                "photos, " +
                "price, " +
                "rating, " +
                "review_count, " +
                "url, " +
                "transactions, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, businessDetails.getName());
            javaSqlPreparedStatement.setString(3, businessDetails.getAlias());
            javaSqlPreparedStatement.setString(4, businessDetails.getCategories().toString());
            javaSqlPreparedStatement.setString(5, businessDetails.getCoordinates().toString());
            javaSqlPreparedStatement.setString(6, businessDetails.getDisplayPhone());
            if (businessDetails.getHours() == null) {
                javaSqlPreparedStatement.setString(7, "NULL");
            } else {
                javaSqlPreparedStatement.setString(7, businessDetails.getHours().toString());
            }
            javaSqlPreparedStatement.setString(8, businessDetails.getImageUrl());
            javaSqlPreparedStatement.setBoolean(9, businessDetails.getIsClaimed());
            javaSqlPreparedStatement.setBoolean(10, businessDetails.getIsClosed());
            javaSqlPreparedStatement.setString(11, businessDetails.getLocation().toString());
            javaSqlPreparedStatement.setString(12, businessDetails.getPhone());
            javaSqlPreparedStatement.setString(13, businessDetails.getPhotos().toString());
            javaSqlPreparedStatement.setString(14, businessDetails.getPrice());
            javaSqlPreparedStatement.setDouble(15, businessDetails.getRating());
            javaSqlPreparedStatement.setInt(16, businessDetails.getReviewCount());
            javaSqlPreparedStatement.setString(17, businessDetails.getUrl());
            javaSqlPreparedStatement.setString(18, businessDetails.getTransactions().toString());
            javaSqlPreparedStatement.setTimestamp(19, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        saveBusinessDetailsCategories(businessId, businessDetailsCategories);
        saveBusinessDetailsCoordinates(businessId, businessDetailsCoordinates);
        saveBusinessDetailsError(businessId, businessDetailsError);
        saveBusinessDetailsHours(businessId, businessDetailsHours);
        saveBusinessDetailsLocation(businessId, businessDetailsLocation);

    }

    private static void saveBusinessDetailsCategories(String businessId, List<BusinessDetailsCategory> businessDetailsCategories) {

        for (BusinessDetailsCategory businessDetailsCategory : businessDetailsCategories) {

            saveBusinessDetailsCategory(businessId, businessDetailsCategory);

        }

    }

    private static void saveBusinessDetailsCategory(String businessId, BusinessDetailsCategory businessDetailsCategory) {

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_details_category " +
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
            javaSqlPreparedStatement.setString(2, businessDetailsCategory.getAlias());
            javaSqlPreparedStatement.setString(3, businessDetailsCategory.getTitle());
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

    private static void saveBusinessDetailsCoordinates(String businessId, BusinessDetailsCoordinates businessDetailsCoordinates) {

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_details_coordinates " +
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
            javaSqlPreparedStatement.setObject(2, businessDetailsCoordinates.getLatitude(), Types.DOUBLE);
            javaSqlPreparedStatement.setObject(3, businessDetailsCoordinates.getLongitude(), Types.DOUBLE);
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

    private static void saveBusinessDetailsError(String businessId, BusinessDetailsError businessDetailsError) {

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_details_error " +
                "(" +
                "business_id, " +
                "error_code, " +
                "error_description, " +
                "error_field, " +
                "error_instance, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            if (businessDetailsError != null) {
                javaSqlPreparedStatement.setString(1, businessId);
                javaSqlPreparedStatement.setString(2, businessDetailsError.getCode());
                javaSqlPreparedStatement.setString(3, businessDetailsError.getDescription());
                javaSqlPreparedStatement.setString(4, businessDetailsError.getField());
                javaSqlPreparedStatement.setString(5, businessDetailsError.getInstance());
                javaSqlPreparedStatement.setTimestamp(6, AtsDateTime.getCurrentTimeStamp());
            } else {
                javaSqlPreparedStatement.setString(1, businessId);
                javaSqlPreparedStatement.setString(2, "NULL");
                javaSqlPreparedStatement.setString(3, "NULL");
                javaSqlPreparedStatement.setString(4, "NULL");
                javaSqlPreparedStatement.setString(5, "NULL");
                javaSqlPreparedStatement.setTimestamp(6, AtsDateTime.getCurrentTimeStamp());
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

    private static void saveBusinessDetailsHours(String businessId, List<BusinessDetailsHour> businessDetailsHours) {

        if (businessDetailsHours != null) {

            for (BusinessDetailsHour businessDetailsHour : businessDetailsHours) {

                saveBusinessDetailsHour(businessId, businessDetailsHour);

            }

        } else {
            BusinessDetailsHour businessDetailsHour = new BusinessDetailsHour();
            saveBusinessDetailsHour(businessId, businessDetailsHour);
        }

    }

    private static void saveBusinessDetailsHour(String businessId, BusinessDetailsHour businessDetailsHour) {

        String hoursType = businessDetailsHour.getHoursType();
        Boolean isOpenNow = businessDetailsHour.getIsOpenNow();
        List<BusinessDetailsHourOpen> businessDetailsHourOpenList = businessDetailsHour.getOpen();

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_details_hour " +
                "(" +
                "business_id, " +
                "hours_type, " +
                "is_open_now, " +
                "open, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, hoursType);
            javaSqlPreparedStatement.setObject(3, isOpenNow, Types.TINYINT);
            if (businessDetailsHourOpenList != null) {
                javaSqlPreparedStatement.setObject(4, businessDetailsHourOpenList.toString(), Types.VARCHAR);
            } else {
                javaSqlPreparedStatement.setNull(4, Types.VARCHAR);
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

        saveBusinessDetailsHourOpen(businessId, hoursType, isOpenNow, businessDetailsHourOpenList);

    }

    private static void saveBusinessDetailsHourOpen(String businessId, String hoursType, Boolean isOpenNow, List<BusinessDetailsHourOpen> businessDetailsHourOpenList) {

        String dayOneStart = null;
        String dayOneEnd = null;
        Boolean dayOneIsOvernight = null;
        String dayTwoStart = null;
        String dayTwoEnd = null;
        Boolean dayTwoIsOvernight = null;
        String dayThreeStart = null;
        String dayThreeEnd = null;
        Boolean dayThreeIsOvernight = null;
        String dayFourStart = null;
        String dayFourEnd = null;
        Boolean dayFourIsOvernight = null;
        String dayFiveStart = null;
        String dayFiveEnd = null;
        Boolean dayFiveIsOvernight = null;
        String daySixStart = null;
        String daySixEnd = null;
        Boolean daySixIsOvernight = null;
        String daySevenStart = null;
        String daySevenEnd = null;
        Boolean daySevenIsOvernight = null;

        if (businessDetailsHourOpenList != null) {
            for (BusinessDetailsHourOpen businessDetailsHourOpen : businessDetailsHourOpenList) {

                int day = businessDetailsHourOpen.getDay();

                String start = businessDetailsHourOpen.getStart();
                String end = businessDetailsHourOpen.getEnd();
                boolean isOvernight = businessDetailsHourOpen.getIsOvernight();

                if (day == 1) {
                    dayOneStart = start;
                    dayOneEnd = end;
                    dayOneIsOvernight = isOvernight;
                } else if (day == 2) {
                    dayTwoStart = start;
                    dayTwoEnd = end;
                    dayTwoIsOvernight = isOvernight;
                } else if (day == 3) {
                    dayThreeStart = start;
                    dayThreeEnd = end;
                    dayThreeIsOvernight = isOvernight;
                } else if (day == 4) {
                    dayFourStart = start;
                    dayFourEnd = end;
                    dayFourIsOvernight = isOvernight;
                } else if (day == 5) {
                    dayFiveStart = start;
                    dayFiveEnd = end;
                    dayFiveIsOvernight = isOvernight;
                } else if (day == 6) {
                    daySixStart = start;
                    daySixEnd = end;
                    daySixIsOvernight = isOvernight;
                } else if (day == 7) {
                    daySevenStart = start;
                    daySevenEnd = end;
                    daySevenIsOvernight = isOvernight;
                }

            }
        }

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_details_hour_open " +
                "(" +
                "business_id, " +
                "hours_hours_type, " +
                "hours_is_open_now, " +
                "day_1_is_overnight, " +
                "day_1_start, " +
                "day_1_end, " +
                "day_2_is_overnight, " +
                "day_2_start, " +
                "day_2_end, " +
                "day_3_is_overnight, " +
                "day_3_start, " +
                "day_3_end, " +
                "day_4_is_overnight, " +
                "day_4_start, " +
                "day_4_end, " +
                "day_5_is_overnight, " +
                "day_5_start, " +
                "day_5_end, " +
                "day_6_is_overnight, " +
                "day_6_start, " +
                "day_6_end, " +
                "day_7_is_overnight, " +
                "day_7_start, " +
                "day_7_end, " +
                "record_ts " +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, hoursType);
            javaSqlPreparedStatement.setObject(3, isOpenNow, Types.TINYINT);

//            if (dayOneStart != null) {
//                javaSqlPreparedStatement.setString(4, dayOneStart);
//            } else {
//                javaSqlPreparedStatement.setNull(4, Types.VARCHAR);
//            }
//            if (dayOneEnd != null) {
//                javaSqlPreparedStatement.setString(5, dayOneEnd);
//            } else {
//                javaSqlPreparedStatement.setNull(5, Types.VARCHAR);
//            }
//            if (dayOneIsOvernight != null) {
//                javaSqlPreparedStatement.setBoolean(6, dayOneIsOvernight);
//            } else {
//                javaSqlPreparedStatement.setNull(6, Types.TINYINT);
//            }

            javaSqlPreparedStatement.setObject(4, dayOneStart, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(5, dayOneEnd, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(6, dayOneIsOvernight, Types.TINYINT);
            javaSqlPreparedStatement.setObject(7, dayTwoStart, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(8, dayTwoEnd, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(9, dayTwoIsOvernight, Types.TINYINT);
            javaSqlPreparedStatement.setObject(10, dayThreeStart, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(11, dayThreeEnd, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(12, dayThreeIsOvernight, Types.TINYINT);
            javaSqlPreparedStatement.setObject(13, dayFourStart, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(14, dayFourEnd, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(15, dayFourIsOvernight, Types.TINYINT);
            javaSqlPreparedStatement.setObject(16, dayFiveStart, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(17, dayFiveEnd, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(18, dayFiveIsOvernight, Types.TINYINT);
            javaSqlPreparedStatement.setObject(19, daySixStart, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(20, daySixEnd, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(21, daySixIsOvernight, Types.TINYINT);
            javaSqlPreparedStatement.setObject(22, daySevenStart, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(23, daySevenEnd, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(24, daySevenIsOvernight, Types.TINYINT);
            javaSqlPreparedStatement.setTimestamp(25, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    private static void saveBusinessDetailsLocation(String businessId, BusinessDetailsLocation businessDetailsLocation) {

        List<String> businessDetailsLocationDisplayAddress = businessDetailsLocation.getDisplayAddress();

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_details_location " +
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
                "cross_streets, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, businessDetailsLocation.getAddress1());
            javaSqlPreparedStatement.setString(3, businessDetailsLocation.getAddress2());
            javaSqlPreparedStatement.setString(4, businessDetailsLocation.getAddress3());
            javaSqlPreparedStatement.setString(5, businessDetailsLocation.getCity());
            javaSqlPreparedStatement.setString(6, businessDetailsLocation.getState());
            javaSqlPreparedStatement.setString(7, businessDetailsLocation.getZipCode());
            javaSqlPreparedStatement.setString(8, businessDetailsLocation.getCountry());
            javaSqlPreparedStatement.setString(9, businessDetailsLocation.getDisplayAddress().toString());
            javaSqlPreparedStatement.setString(10, businessDetailsLocation.getCrossStreets());
            javaSqlPreparedStatement.setTimestamp(11, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        saveBusinessDetailsLocationDisplayAddress(businessId, businessDetailsLocationDisplayAddress);

    }

    private static void saveBusinessDetailsLocationDisplayAddress(String businessId, List<String> businessDetailsLocationDisplayAddress) {

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

        if (businessDetailsLocationDisplayAddress.size() >= 1) {
            displayAddress01 = businessDetailsLocationDisplayAddress.get(0);
        } else {
            displayAddress01 = "NULL";
        }
        if (businessDetailsLocationDisplayAddress.size() >= 2) {
            displayAddress02 = businessDetailsLocationDisplayAddress.get(1);
        } else {
            displayAddress02 = "NULL";
        }
        if (businessDetailsLocationDisplayAddress.size() >= 3) {
            displayAddress03 = businessDetailsLocationDisplayAddress.get(2);
        } else {
            displayAddress03 = "NULL";
        }
        if (businessDetailsLocationDisplayAddress.size() >= 4) {
            displayAddress04 = businessDetailsLocationDisplayAddress.get(3);
        } else {
            displayAddress04 = "NULL";
        }
        if (businessDetailsLocationDisplayAddress.size() >= 5) {
            displayAddress05 = businessDetailsLocationDisplayAddress.get(4);
        } else {
            displayAddress05 = "NULL";
        }
        if (businessDetailsLocationDisplayAddress.size() >= 6) {
            displayAddress06 = businessDetailsLocationDisplayAddress.get(5);
        } else {
            displayAddress06 = "NULL";
        }
        if (businessDetailsLocationDisplayAddress.size() >= 7) {
            displayAddress07 = businessDetailsLocationDisplayAddress.get(6);
        } else {
            displayAddress07 = "NULL";
        }
        if (businessDetailsLocationDisplayAddress.size() >= 8) {
            displayAddress08 = businessDetailsLocationDisplayAddress.get(7);
        } else {
            displayAddress08 = "NULL";
        }
        if (businessDetailsLocationDisplayAddress.size() >= 9) {
            displayAddress09 = businessDetailsLocationDisplayAddress.get(8);
        } else {
            displayAddress09 = "NULL";
        }

        final String sqlQueryString = "INSERT INTO yelp_api_cacher.business_details_location_display_address " +
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

    public static void processBusinessDetails() {

        List<String> businessIdsForDetails = getBusinessIdsForDetails();

        System.out.println("businessIds: " + businessIdsForDetails.toString());

        int current_loop_count = 0;

        // For Each BusinessSearchBusiness ID
        for (String businessId : businessIdsForDetails) {

            System.out.println("businessId: " + businessId);

            // TODO(JamesBalcomb): Add check for error={}
            BusinessDetails businessDetails = getBusinessDetails(businessId);

            System.out.println("businessDetails: " + businessDetails.toString());

            saveBusinessDetails(businessDetails);

            current_loop_count = current_loop_count + 1;
            if (current_loop_count >= YelpApiCacher.MAXIMUM_LOOP_COUNT) {
                // java.lang.System.exit() method
                // System.out.println("Le Fin.");
                // System.exit(0);
                break;
            }

            AtsMiscellaneous.pause();

        }

    }
}
