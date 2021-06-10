package com.example.outsidedb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSonPlaceHolderAPI {

    @GET("posts")
    Call<List<Post>> getPosts();
}
