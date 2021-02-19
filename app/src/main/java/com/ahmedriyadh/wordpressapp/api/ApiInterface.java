package com.ahmedriyadh.wordpressapp.api;

import com.ahmedriyadh.wordpressapp.models.CreateUser;
import com.ahmedriyadh.wordpressapp.models.JwtResponse;
import com.ahmedriyadh.wordpressapp.models.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/*
 * By Ahmed Riyadh
 * Note : Please Don't Re-Post This Project Source Code on Social Media etc..
 * */

public interface ApiInterface {

    @POST("jwt-auth/v1/token")
    Call<JwtResponse> getToken(
            @Query("username") String username,
            @Query("password") String password
    );

    @POST("wp/v2/users")
    Call<CreateUser> createUser(
            @Header("Authorization") String token,
            @Query("username") String username,
            @Query("email") String email,
            @Query("password") String password,
            @Query("first_name") String firstName,
            @Query("last_name") String lastName
    );

    @GET("wp/v2/posts")
    Call<List<Post>> getPosts(
            @Query("per_page") int perPage,
            @Query("_embed") boolean embed

    );
}
