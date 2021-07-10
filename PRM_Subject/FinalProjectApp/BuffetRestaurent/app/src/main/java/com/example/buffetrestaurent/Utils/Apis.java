package com.example.buffetrestaurent.Utils;

import com.example.buffetrestaurent.Model.Desk;

public class Apis {

    public static final String URL_001="http://192.168.0.103:8080/customer/api/";
    public static final String URL_002="http://192.168.0.103:8080/reservation/api/";
    public static final String URL_003="http://192.168.0.103:8080/desk/api/";

    public static CustomerService getCustomerService(){
        return  Client.getClient(URL_001).create(CustomerService.class);
    }
    public static ReservationService getReservationService(){
        return  Client.getClient(URL_002).create(ReservationService.class);
    }
    public static DeskService getDeskService(){
        return  Client.getClient(URL_003).create(DeskService.class);
    }

}
