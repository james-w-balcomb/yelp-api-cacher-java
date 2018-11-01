package com.boringbalcomb.YelpApiCacher.BusinessReviews;

import com.boringbalcomb.AtsUtilities.AtsDateTime;
import com.boringbalcomb.AtsUtilities.AtsMiscellaneous;
import com.boringbalcomb.YelpApiCacher.BusinessReviews.model.BusinessReviews;
import com.boringbalcomb.YelpApiCacher.BusinessReviews.model.BusinessReviewsError;
import com.boringbalcomb.YelpApiCacher.BusinessReviews.model.BusinessReviewsReview;
import com.boringbalcomb.YelpApiCacher.BusinessReviews.model.BusinessReviewsReviewUser;
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

public class BusinessReviewsService {

    private static List<String> getBusinessIdsForReviews() {

        List<String> businessIds = new ArrayList<>();

        String sqlQueryString =
                "SELECT DISTINCT business_id " +
                        "FROM yelp_api_cacher.business_search_business " +
                        "WHERE NOT EXISTS(" +
                        "SELECT 'nothing' FROM yelp_api_cacher.business_reviews " +
                        "WHERE business_search_business.business_id = business_reviews.business_id ORDER BY NULL) " +
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

    private static BusinessReviews getBusinessReviews(String businessId) {

        String uriScheme = "https";
        String uriAuthority = "api.yelp.com";
        String uriPath = "/v3/businesses";
        uriPath = uriPath + "/" + businessId;
        uriPath = uriPath + "/reviews";

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
        BusinessReviews businessReviews = new BusinessReviews();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            businessReviews = objectMapper.readValue(jsonString, BusinessReviews.class);
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

            String fileName = "saves/business_reviews(" + businessId + ").json";
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(fileName);
            } catch (FileNotFoundException ex) {
                System.out.println("java.io.FileNotFoundException");
                ex.printStackTrace();
            }

            if (fileOutputStream != null) {
                try {
                    objectMapper.writeValue(fileOutputStream, businessReviews);
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

        return businessReviews;

    }

    private static void saveBusinessReviews(String businessId, BusinessReviews businessReviews) {
        
        List<BusinessReviewsReview> businessReviewsReviews = businessReviews.getReviews();
        BusinessReviewsError businessReviewsError = businessReviews.getError();

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_reviews " +
                "(" +
                "business_id, " +
                "reviews, " +
                "total, " +
                "possible_languages, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setObject(1, businessId, Types.VARCHAR);
            javaSqlPreparedStatement.setObject(2, businessReviews.getReviews().toString(), Types.VARCHAR);
            javaSqlPreparedStatement.setObject(3, businessReviews.getTotal(), Types.INTEGER);
            javaSqlPreparedStatement.setObject(4, businessReviews.getPossibleLanguages().toString(), Types.VARCHAR);
            javaSqlPreparedStatement.setTimestamp(5, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        saveBusinessReviewsReviews(businessId, businessReviewsReviews);
        saveBusinessReviewsError(businessId, businessReviewsError);
    }

    private static void saveBusinessReviewsReviews(String businessId, List<BusinessReviewsReview> businessReviewsReviews) {

        for (BusinessReviewsReview businessReviewsReview : businessReviewsReviews) {

            saveBusinessReviewsReview(businessId, businessReviewsReview);

        }

    }

    private static void saveBusinessReviewsReview(String businessId, BusinessReviewsReview businessReviewsReview) {

        BusinessReviewsReviewUser businessReviewsReviewUser = businessReviewsReview.getUser();

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_reviews_review " +
                "(" +
                "business_id, " +
                "review_id, " +
                "review_url, " +
                "review_text, " +
                "review_rating, " +
                "review_time_created, " +
                "review_user, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);

            javaSqlPreparedStatement.setString(2, businessReviewsReview.getId());
            javaSqlPreparedStatement.setString(3, businessReviewsReview.getUrl());
            javaSqlPreparedStatement.setString(4, businessReviewsReview.getText());
            javaSqlPreparedStatement.setInt(5, businessReviewsReview.getRating());
            javaSqlPreparedStatement.setString(6, businessReviewsReview.getTimeCreated());
            javaSqlPreparedStatement.setString(7, businessReviewsReview.getUser().toString());
            javaSqlPreparedStatement.setTimestamp(8, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

        saveBusinessReviewsReviewUser(businessId, businessReviewsReviewUser);

    }

    private static void saveBusinessReviewsReviewUser(String businessId, BusinessReviewsReviewUser businessReviewsReviewUser) {

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_reviews_review_user " +
                "(" +
                "business_id, " +
                "user_id, " +
                "user_profile_url, " +
                "user_image_url, " +
                "user_name, " +
                "record_ts" +
                ") VALUES (" +
                "?, ?, ?, ?, ?, ?" +
                ");";

        try (Connection javaSqlConnection = DriverManager.getConnection(MYSQL_CONNECTION_STRING);
             PreparedStatement javaSqlPreparedStatement = javaSqlConnection.prepareStatement(sqlQueryString)) {

            javaSqlPreparedStatement.setString(1, businessId);
            javaSqlPreparedStatement.setString(2, businessReviewsReviewUser.getId());
            javaSqlPreparedStatement.setString(3, businessReviewsReviewUser.getProfileUrl());
            javaSqlPreparedStatement.setString(4, businessReviewsReviewUser.getImageUrl());
            javaSqlPreparedStatement.setString(5, businessReviewsReviewUser.getName());
            javaSqlPreparedStatement.setTimestamp(6, AtsDateTime.getCurrentTimeStamp());

            javaSqlPreparedStatement.executeUpdate();

        } catch (SQLException sqlEx) {
            System.out.println("java.sql.SQLException");
            System.out.println("SQLException: " + sqlEx.getMessage());
            System.out.println("SQLState: " + sqlEx.getSQLState());
            System.out.println("VendorError: " + sqlEx.getErrorCode());
            sqlEx.printStackTrace();
        }

    }

    private static void saveBusinessReviewsError(String businessId, BusinessReviewsError businessReviewsError) {

        String sqlQueryString = "INSERT INTO yelp_api_cacher.business_reviews_error " +
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

            if (businessReviewsError == null) {

                javaSqlPreparedStatement.setString(1, businessId);
                javaSqlPreparedStatement.setString(2, "NULL");
                javaSqlPreparedStatement.setString(3, "NULL");
                javaSqlPreparedStatement.setString(4, "NULL");
                javaSqlPreparedStatement.setString(5, "NULL");
                javaSqlPreparedStatement.setTimestamp(6, AtsDateTime.getCurrentTimeStamp());

            } else {

                javaSqlPreparedStatement.setString(1, businessId);
                javaSqlPreparedStatement.setString(2, businessReviewsError.getCode());
                javaSqlPreparedStatement.setString(3, businessReviewsError.getDescription());
                javaSqlPreparedStatement.setString(4, businessReviewsError.getField());
                javaSqlPreparedStatement.setString(5, businessReviewsError.getInstance());
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

    public static void processBusinessReviews() {

        List<String> businessIdsForReviews = getBusinessIdsForReviews();

        System.out.println("businessIdsForReviews: " + businessIdsForReviews.toString());

        int current_loop_count = 0;

        for (String businessId : businessIdsForReviews) {

            System.out.println("businessId: " + businessId);

            // TODO(JamesBalcomb): Add check for error={}
            BusinessReviews businessReviews = getBusinessReviews(businessId);

            System.out.println("businessReviews: " + businessReviews.toString());

            saveBusinessReviews(businessId, businessReviews);
            // Save Business Reviews -

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
