package com.smart.tablet;

public class Constants {
    /*
     * Device setting keys
     */
    public static final String TOKEN_KEY = "ST@TOKEN";
    public static final String API_KEY = "ST@API_KEY";
    public static final String FILE_PATH_KEY = "ST@FILE_PATH";
    public static final String DEVICE_ID = "ST@DEVICE_ID";
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
    public static final String SETTING_HAS_ENTRY_PAGE = "entry_page_enable";
    public static final String SETTING_ENTRY_PAGE_START_TIME = "entry_page_start_time";
    public static final String SETTING_ENTRY_PAGE_END_TIME = "entry_page_end_time";
    public static final String SETTING_PASSWORD = "pwd";
    public static final String SETTING_SLEEP_TIME = "sleep_time";
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
    public static final String TOP_MENU_SHOW_ARRIVALS = "show_new_arrivals";
    public static final String TOP_MENU_SHOW_SALES = "show_sales";
    public static final String TOP_MENU_SHOW_TESTIMONIALS = "show_testimonials";
    public static final String TOP_MENU_SHOW_SERVICES = "show_services";
    public static final String TOP_MENU_SHOW_PRODUCTS = "show_products";
    public static final String TOP_MENU_SHOW_PRICE_LIST = "show_price_list";
    public static final String TOP_MENU_SHOW_WEBSITE = "show_website";
    public static final String TOP_MENU_SHOW_FEATURED = "show_featured";
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
    public static final long SYNC_WAIT = 10 * 60 * 1000;
    public static final long BACK_TO_HOME_WAIT = 3 * 60 * 1000;
    public static final long BACK_TO_ENTRY_PAGE = 2 * 60 * 1000;
    public static final long SCREEN_WAKEUP_WAIT = 60 * 1000;
    public static final String CITY_SYDNEY = "nsw/sydney";
    public static final String CITY_MELBOURNE = "vic/melbourne";
    public static final String CITY_BRISBANE = "qld/brisbane";
    public static final String CITY_CANBERRA = "act/canberra";
    public static final String CITY_DARWIN = "nt/darwin";
    public static final String CITY_HOBART = "tas/hobart";
    public static final String CITY_GOLD_COAST = "qld/gold-coast";
    public static final String CITY_PERTH = "wa/perth";
    /*
     * Commands
     */
    public static final String COMMAND_EXECUTE_SEND_REPORT = "execute_send_report_task";
    public static final String COMMAND_PING = "ping_device";
    /*
     * Constants
     */
    public static String APP_KEY = "smart-$2y$10$RdYWP.Z6T1DFDjSSunimzOUcMDGIBmyqCQ11/Vof.idVxCY14h8ky-api";
    public static String URL = "http://api.ask-me.com.au/api/v1/";
    public static String DEFAULT_TIMEZONE = "Australia/Sydney";

    public static String GetApiUrl(String path) {
        return com.smart.tablet.Constants.URL + path;
    }
}
