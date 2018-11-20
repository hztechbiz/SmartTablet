package com.smart.tablet.helpers;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.smart.tablet.dao.AnalyticsDao;
import com.smart.tablet.dao.ArrivalDao;
import com.smart.tablet.dao.CategoryDao;
import com.smart.tablet.dao.DeviceDao;
import com.smart.tablet.dao.HotelDao;
import com.smart.tablet.dao.MediaDao;
import com.smart.tablet.dao.OfferDao;
import com.smart.tablet.dao.SaleDao;
import com.smart.tablet.dao.ServiceDao;
import com.smart.tablet.dao.SettingDao;
import com.smart.tablet.dao.TestimonialDao;
import com.smart.tablet.entities.Analytics;
import com.smart.tablet.entities.Arrival;
import com.smart.tablet.entities.Category;
import com.smart.tablet.entities.Device;
import com.smart.tablet.entities.Hotel;
import com.smart.tablet.entities.Media;
import com.smart.tablet.entities.Offer;
import com.smart.tablet.entities.Sale;
import com.smart.tablet.entities.Service;
import com.smart.tablet.entities.Setting;
import com.smart.tablet.entities.Testimonial;

@Database(entities = {Setting.class, Hotel.class, Category.class, Service.class, Media.class, Offer.class, Testimonial.class, Arrival.class, Sale.class, Device.class, Analytics.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SettingDao settingDao();

    public abstract HotelDao hotelDao();

    public abstract CategoryDao categoryDao();

    public abstract ServiceDao serviceDao();

    public abstract MediaDao mediaDao();

    public abstract OfferDao offerDao();

    public abstract TestimonialDao testimonialDao();

    public abstract ArrivalDao arrivalDao();

    public abstract SaleDao saleDao();

    public abstract DeviceDao deviceDao();

    public abstract AnalyticsDao analyticsDao();
}