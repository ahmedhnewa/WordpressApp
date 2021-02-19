package com.ahmedriyadh.wordpressapp.models.commons;

import com.google.gson.annotations.SerializedName;

public class FeaturedMedia {
    @SerializedName("source_url")
    private String sourceUrl;

    public String getSourceUrl() {
        return sourceUrl;
    }

}
