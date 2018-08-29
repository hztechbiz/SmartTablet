package com.smart.tablet.helpers;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.smart.tablet.helpers.AppDatabase;

public class DatabaseHelper {

    private static com.smart.tablet.helpers.DatabaseHelper _instance;
    private static String DB_NAME = "002_SmartTablet";
    private AppDatabase _db;
    private Context _context;

    private DatabaseHelper(Context context) {
        _context = context;
        _db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public static com.smart.tablet.helpers.DatabaseHelper getInstance(Context context) {
        if (_instance == null)
            _instance = new com.smart.tablet.helpers.DatabaseHelper(context);

        _instance._context = context;

        return _instance;
    }

    public AppDatabase getAppDatabase() {
        return _db;
    }
}
