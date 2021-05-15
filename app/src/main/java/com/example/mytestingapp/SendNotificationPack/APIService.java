package com.example.mytestingapp.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAoDdONxk:APA91bEAurfz8PYiwAt9BX9VMSeR2kMfjaODT55xwwBQPYZN4RVzKwkBYejidN9T7YQbPMHAGUlRjuxgLQdY09bwHA5uaK2NoyDeCFuCxg8kQKlWoD_Sk8YxEbGTXp4_n9s_peTgtIkJ" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
