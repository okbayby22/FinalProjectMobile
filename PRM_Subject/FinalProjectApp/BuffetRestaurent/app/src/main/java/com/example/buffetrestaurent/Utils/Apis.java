package com.example.buffetrestaurent.Utils;

public class Apis {
    public static final String URL_001="http://10.66.168.52:8080/customer/api/";

    public static CustomerService getCustomerService(){
        return  Client.getClient(URL_001).create(CustomerService.class);
    }
}
