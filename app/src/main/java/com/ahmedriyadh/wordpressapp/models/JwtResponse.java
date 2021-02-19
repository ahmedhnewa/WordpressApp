package com.ahmedriyadh.wordpressapp.models;

import com.ahmedriyadh.wordpressapp.models.commons.Data;
import com.google.gson.annotations.SerializedName;

public class JwtResponse {

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

    @SerializedName("token")
    private String token;

    @SerializedName("user_email")
    private String userEmail;

    @SerializedName("user_nicename")
    private String userNiceName;

    @SerializedName("user_display_name")
    private String userDisplayName;


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public String getToken() {
        return token;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserNiceName() {
        return userNiceName;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }
}
