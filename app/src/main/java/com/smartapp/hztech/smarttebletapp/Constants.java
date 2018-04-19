package com.smartapp.hztech.smarttebletapp;

public class Constants {
    public static final String TOKEN_KEY = "ST@TOKEN";
    public static final String API_KEY = "ST@API_KEY";
    public static final String FILE_PATH_KEY = "ST@FILE_PATH";
    public static String APP_KEY = "smart-$2y$10$RdYWP.Z6T1DFDjSSunimzOUcMDGIBmyqCQ11/Vof.idVxCY14h8ky-api";
    public static String URL = "http://13.58.82.76/api/v1/";

    public static String GetApiUrl(String path) {
        return Constants.URL + path;
    }
}
