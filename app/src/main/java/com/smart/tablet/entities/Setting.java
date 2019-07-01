package com.smart.tablet.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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

    @Ignore
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
