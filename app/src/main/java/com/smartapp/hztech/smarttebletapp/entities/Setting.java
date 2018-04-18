package com.smartapp.hztech.smarttebletapp.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Setting {
    @PrimaryKey
    @NonNull
    private String name;

    private String value;

    public Setting(@NonNull String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Setting() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        if (value.equals("null"))
            return null;

        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
