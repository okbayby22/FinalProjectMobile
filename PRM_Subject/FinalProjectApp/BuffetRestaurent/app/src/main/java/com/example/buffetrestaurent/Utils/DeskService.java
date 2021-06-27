package com.example.buffetrestaurent.Utils;

import com.example.buffetrestaurent.Model.Desk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeskService {

    @GET("deskList")
    Call <List<Desk>> loaddeskList();
}
