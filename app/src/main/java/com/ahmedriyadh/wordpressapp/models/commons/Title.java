package com.ahmedriyadh.wordpressapp.models.commons;

import com.google.gson.annotations.SerializedName;

public class Title {
    @SerializedName("rendered")
    private String rendered;

    public String getRendered() {
        return rendered;
    }
}
