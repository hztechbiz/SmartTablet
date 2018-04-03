package com.smartapp.hztech.smarttebletapp.helpers;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseHelper {

    private static DatabaseHelper _instance;
    private static String DB_NAME = "001_SmartTablet";
    private AppDatabase _db;
    private Context _context;

    private DatabaseHelper(Context context) {
        _context = context;
        _db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public static DatabaseHelper getInstance(Context context) {
        if (_instance == null)
            _instance = new DatabaseHelper(context);

        _instance._context = context;

        return _instance;
    }

    public AppDatabase getAppDatabase() {
        return _db;
    }
}