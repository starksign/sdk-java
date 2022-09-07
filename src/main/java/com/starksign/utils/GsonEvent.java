package com.starksign.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


class GsonEvent {
     private static Gson instance;

     private GsonEvent() {}

     public static synchronized Gson getInstance()
     {
         if(instance == null)
             instance = new GsonBuilder()
                     .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
                     .create();
         return instance;
     }
}
