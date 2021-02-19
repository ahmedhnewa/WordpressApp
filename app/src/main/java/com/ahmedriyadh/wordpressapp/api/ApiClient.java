package com.ahmedriyadh.wordpressapp.api;

import com.ahmedriyadh.wordpressapp.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * By Ahmed Riyadh
 * Note : Please Don't Re-Post This Project Source Code on Social Media etc..
 * */

public class ApiClient {
    private static Retrofit retrofit = null;

    public static ApiInterface getApiInterface() {
      if (retrofit == null) {
          retrofit = new Retrofit.Builder()
                  .baseUrl(Constants.DEFAULT_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
      }
      return retrofit.create(ApiInterface.class);
    }
}
