package com.ahmedriyadh.wordpressapp.models.commons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Embedded {
    @SerializedName("author")
    private List<Author> authorList;

    @SerializedName("wp:featuredmedia")
    private List<FeaturedMedia> featuredMedia;

    public List<Author> getAuthorList() {
        return authorList;
    }

    public List<FeaturedMedia> getFeaturedMedia() {
        return featuredMedia;
    }
}
