package com.smartapp.hztech.smarttebletapp.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "testimonials")
public class Testimonial {
    @PrimaryKey
    private int id;

    private String cite;

    @ColumnInfo(typeAffinity = 2)
    private String content;

    private int service_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCite() {
        return cite;
    }

    public void setCite(String cite) {
        this.cite = cite;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    @Override
    public String toString() {
        return "Testimonial{" +
                "id=" + id +
                ", cite='" + cite + '\'' +
                ", content='" + content + '\'' +
                ", service_id=" + service_id +
                '}';
    }
}
