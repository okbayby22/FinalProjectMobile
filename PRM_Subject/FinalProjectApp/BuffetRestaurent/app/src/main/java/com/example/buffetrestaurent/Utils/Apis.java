package com.example.buffetrestaurent.Utils;

public class Apis {
    public static final String URL_001="http://192.168.1.49:8080/customer/api/";
    public static final String URL_002="http://192.168.1.49:8080/reservation/api/";

    public static CustomerService getCustomerService(){
        return  Client.getClient(URL_001).create(CustomerService.class);
    }
    public static ReservationService getReservationService(){
        return  Client.getClient(URL_002).create(ReservationService.class);
    }
}
