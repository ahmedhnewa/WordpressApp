package com.ahmedriyadh.wordpressapp.models.commons;

import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("rendered")
    private String rendered;

    @SerializedName("protected")
    private Boolean mProtected;

    public String getRendered() {
        return rendered;
    }

    public Boolean isProtected() {
        return mProtected;
    }
}
