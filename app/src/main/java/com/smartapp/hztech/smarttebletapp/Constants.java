package com.smartapp.hztech.smarttebletapp;

public class Constants {
    /*
     * Device setting keys
     */
    public static final String TOKEN_KEY = "ST@TOKEN";
    public static final String API_KEY = "ST@API_KEY";
    public static final String FILE_PATH_KEY = "ST@FILE_PATH";
    /*
     * Setting keys
     */
    public static final String SETTING_SYNC_DONE = "ST@SYNC_DONE";
    public static final String SETTING_LOGO = "logo";
    public static final String SETTING_BACKGROUND = "background";
    public static final String SETTING_LATITUDE = "hotel_latitude";
    public static final String SETTING_LONGITUDE = "hotel_longitude";
    public static final String SETTING_ADDRESS = "hotel_address";
    public static final String SETTING_EMAIL = "hotel_email";
    public static final String SETTING_PHONE = "hotel_phone";
    public static final String TOP_GUEST_CATEGORIES = "top_menu_item_guest_categories";
    public static final String TOP_MENU_SHOW_WELCOME = "show_welcome";
    public static final String TOP_MENU_WELCOME_TEXT = "welcome_text";
    public static final String WELCOME_HEADING = "welcome_heading";
    public static final String TOP_MP_CATEGORIES = "top_menu_item_mp_categories";

    /*
     * Meta keys
     */
    public static final String TOP_MENU_SHOW_ABOUT = "show_about";
    public static final String TOP_MENU_SHOW_LOCATION = "show_location";
    public static final String TOP_MENU_SHOW_VIDEO = "show_video";
    public static final String TOP_MENU_SHOW_GALLERY = "show_gallery";
    public static final String TOP_MENU_SHOW_MENU = "show_menu";
    public static final String TOP_MENU_SHOW_BOOK = "show_booking";
    public static final String TOP_MENU_SHOW_OFFERS = "show_offers";
    public static final String TOP_MENU_SHOW_TESTIMONIALS = "show_testimonials";
    public static final String META_LOCATION_TITLE = "location_title";
    public static final String META_LOCATION_DESCRIPTION = "location_short_description";
    public static final String META_LOCATION_ADDRESS = "location_address";
    public static final String META_LOCATION_EMAIL = "location_email";
    public static final String META_LOCATION_PHONE = "location_phone";
    public static final String META_LOCATION_LATITUDE = "location_latitude";
    public static final String META_LOCATION_LONGITUDE = "location_longitude";
    public static final String SYNC_SERVICE_RUNNING = "sync_service_running";
    /*
     * Numeric Constants
     */
    public static final long SYNC_WAIT = 5 * 60 * 1000;
    public static final long BACK_TO_HOME_WAIT = 5 * 60 * 1000;
    public static final long SCREEN_WAKEUP_WAIT = 60 * 1000;
    /*
     * Constants
     */
    public static String APP_KEY = "smart-$2y$10$RdYWP.Z6T1DFDjSSunimzOUcMDGIBmyqCQ11/Vof.idVxCY14h8ky-api";
    public static String URL = "http://13.58.82.76/api/v1/";

    public static String GetApiUrl(String path) {
        return Constants.URL + path;
    }
}
