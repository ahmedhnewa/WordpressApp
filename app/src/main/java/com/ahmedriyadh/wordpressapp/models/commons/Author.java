package com.ahmedriyadh.wordpressapp.models.commons;

import com.google.gson.annotations.SerializedName;

public class Author {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}
