package com.smart.tablet.models;

import com.smart.tablet.entities.Hotel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HotelModel {
    private int id;

    private String name;

    private String country;

    private int group_id;

    private String timezone;

    private HashMap<String, String> meta;

    public HotelModel(Hotel entity) throws JSONException {
        setId(entity.getId());
        setCountry(entity.getCountry());
        setGroup_id(entity.getGroup_id());
        setName(entity.getName());
        setTimezone(entity.getTimezone());

        meta = new HashMap<>();

        if (entity.getMeta() != null) {
            JSONArray metas_array = new JSONArray(entity.getMeta());

            for (int i = 0; i < metas_array.length(); i++) {
                JSONObject meta_obj = (JSONObject) metas_array.get(i);

                meta.put(meta_obj.getString("meta_key"), meta_obj.getString("meta_value"));
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public HashMap<String, String> getMeta() {
        return meta;
    }

    public void setMeta(HashMap<String, String> meta) {
        this.meta = meta;
    }
}
