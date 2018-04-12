package com.smartapp.hztech.smarttebletapp.helpers;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.smartapp.hztech.smarttebletapp.dao.CategoryDao;
import com.smartapp.hztech.smarttebletapp.dao.HotelDao;
import com.smartapp.hztech.smarttebletapp.dao.ServiceDao;
import com.smartapp.hztech.smarttebletapp.dao.SettingDao;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Hotel;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.entities.Setting;

@Database(entities = {Setting.class, Hotel.class, Category.class, Service.class}, version = 17)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SettingDao settingDao();

    public abstract HotelDao hotelDao();

    public abstract CategoryDao categoryDao();

    public abstract ServiceDao serviceDao();
}