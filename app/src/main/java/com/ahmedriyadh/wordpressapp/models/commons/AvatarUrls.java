package com.ahmedriyadh.wordpressapp.models.commons;

import com.google.gson.annotations.SerializedName;

public class AvatarUrls {
    @SerializedName("24")
    private String imageOne;

    @SerializedName("48")
    private String imageTow;

    @SerializedName("96")
    private String  imageThere;

    public String getImageOne() {
        return imageOne;
    }

    public String getImageTow() {
        return imageTow;
    }

    public String getImageThere() {
        return imageThere;
    }
}
