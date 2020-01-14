package com.smart.tablet.models;

public class LanguageModel {
    private String name;
    private String icon;
    private String value;

    public LanguageModel(String name, String icon, String value) {
        this.name = name;
        this.icon = icon;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
