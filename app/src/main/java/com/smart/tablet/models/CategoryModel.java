package com.smart.tablet.models;

import com.smart.tablet.entities.Category;

public class CategoryModel extends Category {
    private String image;

    public CategoryModel(Category c) {
        super(c.getId(), c.getName(), c.getDescription(), c.getParent_id(), c.getChildren_count(), c.isIs_marketing_partner(), c.getEmbed_url(), c.getDisplay_order(), c.getMeta());
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "image='" + image + '\'' +
                '}';
    }
}
