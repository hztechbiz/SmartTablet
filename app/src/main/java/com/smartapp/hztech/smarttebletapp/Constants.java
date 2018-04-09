package com.smartapp.hztech.smarttebletapp;

public class Constants {
    public static Boolean DEBUG = false;
    public static String APP_KEY = "smart-$2y$10$RdYWP.Z6T1DFDjSSunimzOUcMDGIBmyqCQ11/Vof.idVxCY14h8ky-api";
    public static String URL = DEBUG ? "http://localhost:2202/api/v1/" : "http://18.218.244.52/api/v1/";

    public static String GetApiUrl(String path) {
        return Constants.URL + path;
    }
}
