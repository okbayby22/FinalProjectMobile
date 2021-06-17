package com.example.buffetrestaurent.Utils;

import com.example.buffetrestaurent.Model.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CustomerService {
    @GET("{email}/{password}/login")
    Call<Integer> checkLogin(@Path("email") String email,@Path("password") String pass);

    @POST("insert")
    Call<Customer>addCustomer(@Body Customer customer);

    @GET("{email}/email")
    Call<Boolean> checkDuplicateEmail(@Path("email") String email);
}
