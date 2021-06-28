package com.example.buffetrestaurent.Utils;

import com.example.buffetrestaurent.Model.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CustomerService {
    @GET("{email}/{password}/login")
    Call<Integer> checkLogin(@Path("email") String email,@Path("password") String pass);

    @POST("insert")
    Call<Customer>addCustomer(@Body Customer customer);

    @GET("{email}/email")
    Call<Boolean> checkDuplicateEmail(@Path("email") String email);

    @GET("{email}/userinfor")
    Call<Customer> getUserInfor(@Path("email") String email);

    @PUT("updateInfor")
    Call<Customer> updateCusInfor(@Body Customer customer);

    @PUT("{password}/{email}/updateCusPassword")
    Call<Boolean> updateCusPassword(@Path("password")String password,@Path("email")String email);

    @GET("{password}/{email}/checkPassword")
    Call<Boolean> checkPassword(@Path("password")String password,@Path("email")String email);
}
