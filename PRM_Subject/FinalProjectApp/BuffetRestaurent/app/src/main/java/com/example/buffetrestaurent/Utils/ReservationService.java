package com.example.buffetrestaurent.Utils;

import com.example.buffetrestaurent.Model.Reservation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationService {

    @POST("{date}/add")
    Call<Reservation> addReservation(@Body Reservation reservation, @Path("date")String date);

    @PUT("{id}/{date}/updatecustomer")
    Call<Reservation> updateReservation(@Path("id")int id, @Path("date")String date,@Body Reservation reservation);
}
