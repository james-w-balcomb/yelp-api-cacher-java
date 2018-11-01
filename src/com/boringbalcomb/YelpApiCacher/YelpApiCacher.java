package com.boringbalcomb.YelpApiCacher;

import com.boringbalcomb.YelpApiCacher.BusinessDetails.BusinessDetailsService;
import com.boringbalcomb.YelpApiCacher.BusinessReviews.BusinessReviewsService;
import com.boringbalcomb.YelpApiCacher.BusinessSearch.BusinessSearchService;

import java.util.*;

public class YelpApiCacher {

    // private static final Logger LOGGER = LogManager.getLogger(YelpApiCacher.class);
    public static final int MAXIMUM_LOOP_COUNT = 2820; // 3 ~= 1%; 282 ~= 10%; 2820 = 100%
    public static final boolean USE_CACHE = true;
    public static final boolean SAVE_FILES = true;
    public static final List<Double> LATITUDES = Arrays.asList(41.902852, 41.901037, 41.899222, 41.897407, 41.895592, 41.893777, 41.891962, 41.890147, 41.888332, 41.886517, 41.884702, 41.882887, 41.881072, 41.879257, 41.877442, 41.875627, 41.873812, 41.871997, 41.870182, 41.868367, 41.866552, 41.864737, 41.862922, 41.861107, 41.859292, 41.857477, 41.855662, 41.853847, 41.852032, 41.850217);
    public static final List<Double> LONGITUDES = Arrays.asList(-87.709340, -87.706907, -87.704474, -87.702041, -87.699608, -87.697175, -87.694742, -87.692309, -87.689876, -87.687443, -87.685010, -87.682577, -87.680144, -87.677711, -87.675278, -87.672845, -87.670412, -87.667979, -87.665546, -87.663113, -87.660680, -87.658247, -87.655814, -87.653381, -87.650948, -87.648515, -87.646082, -87.643649, -87.641216, -87.638783, -87.636350, -87.633917, -87.631484, -87.629051, -87.626618, -87.624185, -87.621752, -87.619319, -87.616886, -87.614453, -87.612020, -87.609587, -87.607154, -87.604721, -87.602288, -87.599855, -87.597422);
    public static String API_KEY;
    public static String MYSQL_CONNECTION_STRING;

    public static void main(String[] args) {

        // //////////////////// //
        // BEGIN: Configuration //
        // //////////////////// //

        String MYSQL_DRIVER;
        String MYSQL_URL;
        String MYSQL_USERNAME;
        String MYSQL_PASSWORD;

        API_KEY = System.getenv("YELP_API_CACHER_API_KEY");
        MYSQL_DRIVER = System.getenv("YELP_API_CACHER_MYSQL_DRIVER");
        MYSQL_URL = System.getenv("YELP_API_CACHER_MYSQL_URL");
        MYSQL_USERNAME = System.getenv("YELP_API_CACHER_MYSQL_USERNAME");
        MYSQL_PASSWORD = System.getenv("YELP_API_CACHER_MYSQL_PASSWORD");
        if (API_KEY == null | MYSQL_DRIVER == null | MYSQL_URL == null | MYSQL_USERNAME == null | MYSQL_PASSWORD == null) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("YelpApiCacher");
            if (API_KEY == null) {
                API_KEY = resourceBundle.getString("yelp.api.key");
            }
            if (MYSQL_DRIVER == null) {
                MYSQL_DRIVER = resourceBundle.getString("jdbc.driver");
            }
            if (MYSQL_URL == null) {
                MYSQL_URL = resourceBundle.getString("jdbc.url");
            }
            if (MYSQL_USERNAME == null) {
                MYSQL_USERNAME = resourceBundle.getString("jdbc.user");
            }
            if (MYSQL_PASSWORD == null) {
                MYSQL_PASSWORD = resourceBundle.getString("jdbc.password");
            }
        }
        MYSQL_CONNECTION_STRING = MYSQL_URL + "&user=" + MYSQL_USERNAME + "&password=" + MYSQL_PASSWORD;
        
        // //////////////////// //
        // END: Configuration //
        // //////////////////// //

        BusinessSearchService.processBusinessSearch();

        BusinessDetailsService.processBusinessDetails();

        BusinessReviewsService.processBusinessReviews();

    }

}
