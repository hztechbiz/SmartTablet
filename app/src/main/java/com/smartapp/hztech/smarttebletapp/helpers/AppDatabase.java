package com.smartapp.hztech.smarttebletapp.helpers;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.smartapp.hztech.smarttebletapp.dao.CategoryDao;
import com.smartapp.hztech.smarttebletapp.dao.HotelDao;
import com.smartapp.hztech.smarttebletapp.dao.MediaDao;
import com.smartapp.hztech.smarttebletapp.dao.OfferDao;
import com.smartapp.hztech.smarttebletapp.dao.ServiceDao;
import com.smartapp.hztech.smarttebletapp.dao.SettingDao;
import com.smartapp.hztech.smarttebletapp.dao.TestimonialDao;
import com.smartapp.hztech.smarttebletapp.entities.Category;
import com.smartapp.hztech.smarttebletapp.entities.Hotel;
import com.smartapp.hztech.smarttebletapp.entities.Media;
import com.smartapp.hztech.smarttebletapp.entities.Offer;
import com.smartapp.hztech.smarttebletapp.entities.Service;
import com.smartapp.hztech.smarttebletapp.entities.Setting;
import com.smartapp.hztech.smarttebletapp.entities.Testimonial;

@Database(entities = {Setting.class, Hotel.class, Category.class, Service.class, Media.class, Offer.class, Testimonial.class}, version = 26, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SettingDao settingDao();

    public abstract HotelDao hotelDao();

    public abstract CategoryDao categoryDao();

    public abstract ServiceDao serviceDao();

    public abstract MediaDao mediaDao();

    public abstract OfferDao offerDao();

    public abstract TestimonialDao testimonialDao();
}