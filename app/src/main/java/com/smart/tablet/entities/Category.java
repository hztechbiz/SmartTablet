package com.smart.tablet.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey
    private int id;
    private String name;
    @ColumnInfo(typeAffinity = 2)
    private String description;
    private int parent_id;
    @Ignore
    private int children_count;
    private boolean is_marketing_partner;
    private String embed_url;
    private int display_order;
    @ColumnInfo(typeAffinity = 2)
    private String meta;

    public Category() {
    }

    public Category(int id, String name, String description, int parent_id, int children_count, boolean is_marketing_partner, String embed_url, int display_order, String meta) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parent_id = parent_id;
        this.children_count = children_count;
        this.is_marketing_partner = is_marketing_partner;
        this.embed_url = embed_url;
        this.display_order = display_order;
        this.meta = meta;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getChildren_count() {
        return children_count;
    }

    public void setChildren_count(int children_count) {
        this.children_count = children_count;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parent_id=" + parent_id +
                ", children_count=" + children_count +
                ", display_order=" + display_order +
                ", meta=" + meta +
                '}';
    }

    public boolean isIs_marketing_partner() {
        return is_marketing_partner;
    }

    public void setIs_marketing_partner(boolean is_marketing_partner) {
        this.is_marketing_partner = is_marketing_partner;
    }

    public String getEmbed_url() {
        return embed_url;
    }

    public void setEmbed_url(String embed_url) {
        this.embed_url = embed_url;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }
}
