package com.ahmedriyadh.wordpressapp.models.commons;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("status")
    private Integer status;

    public Integer getStatus() {
        return status;
    }
}
