package com.smartapp.hztech.smarttebletapp.models;

import android.graphics.Bitmap;

public class ServiceModel {
    private int id;

    private String title;

    private String description;

    private int category_id;

    private int status;

    private int hotel_id;

    private String meta;

    private boolean is_marketing_partner;

    private Bitmap image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public boolean isIs_marketing_partner() {
        return is_marketing_partner;
    }

    public void setIs_marketing_partner(boolean is_marketing_partner) {
        this.is_marketing_partner = is_marketing_partner;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
