package com.smart.tablet.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class MapMarker implements Parcelable {
    public static final Creator<com.smart.tablet.models.MapMarker> CREATOR = new Creator<com.smart.tablet.models.MapMarker>() {
        @Override
        public com.smart.tablet.models.MapMarker createFromParcel(Parcel source) {
            return new com.smart.tablet.models.MapMarker(source);
        }

        @Override
        public com.smart.tablet.models.MapMarker[] newArray(int size) {
            return new com.smart.tablet.models.MapMarker[size];
        }
    };
    private LatLng latLng;
    private String address;

    public MapMarker(LatLng latLng, String address) {
        this.latLng = latLng;
        this.address = address;
    }

    protected MapMarker(Parcel in) {
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.address = in.readString();
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.latLng, flags);
        dest.writeString(this.address);
    }

    @Override
    public String toString() {
        return "MapMarker{" +
                "latLng=" + latLng +
                ", address='" + address + '\'' +
                '}';
    }
}
