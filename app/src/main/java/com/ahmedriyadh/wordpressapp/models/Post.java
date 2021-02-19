package com.ahmedriyadh.wordpressapp.models;

import com.ahmedriyadh.wordpressapp.models.commons.Content;
import com.ahmedriyadh.wordpressapp.models.commons.Embedded;
import com.ahmedriyadh.wordpressapp.models.commons.Title;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("id")
    private Integer id;

    @SerializedName("date_gmt")
    private String date;

    @SerializedName("modified_gmt")
    private String modifiedGmt;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private Title title;

    @SerializedName("content")
    private Content content;

    @SerializedName("_embedded")
    private Embedded embedded;

    public Integer getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getModifiedGmt() {
        return modifiedGmt;
    }

    public String getType() {
        return type;
    }

    public Title getTitle() {
        return title;
    }

    public Content getContent() {
        return content;
    }

    public Embedded getEmbedded() {
        return embedded;
    }
}
