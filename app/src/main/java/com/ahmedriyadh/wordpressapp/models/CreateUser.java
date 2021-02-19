package com.ahmedriyadh.wordpressapp.models;

import com.ahmedriyadh.wordpressapp.models.commons.AvatarUrls;
import com.ahmedriyadh.wordpressapp.models.commons.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateUser {

    /*
     * if
     * response is not successful
     * */

    @SerializedName("code")
    private String code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Data data;

    /*
     * if
     * response is successful
     * */

    @SerializedName("id")
    private Integer id;

    @SerializedName("username")
    private String username;

    @SerializedName("name")
    private String name;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;

    @SerializedName("roles")
    private List<String> roles;

    @SerializedName("avatar_urls")
    private AvatarUrls avatarUrls;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }
}
