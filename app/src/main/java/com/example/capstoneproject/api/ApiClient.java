//package com.example.capstoneproject.api;
//
//import android.content.Context;
//import android.util.Log;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import java.util.concurrent.TimeUnit;
//
//public class ApiClient {
//    private static Retrofit retrofit = null;
//
//    private static final String BASE_URL = "https://uep-smartfarming-backend.onrender.com/api/sensor/model/";
//
//    public static Retrofit getClient(Context context) {
//        if (retrofit == null) {
//            // Logging interceptor
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.d("RETROFIT_DEBUG", message));
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .connectTimeout(30, TimeUnit.SECONDS)
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(30, TimeUnit.SECONDS)
//                    .addInterceptor(chain -> {
//                        Request original = chain.request();
//                        Request.Builder requestBuilder = original.newBuilder()
//                                .header("Accept", "application/json");
//
//                        // Add token if available
//                        String token = MySessionManager.getToken(context); // Implement this to retrieve stored token
//                        if (token != null) {
//                            requestBuilder.header("Authorization", "Bearer " + token);
//                        }
//
//                        Request request = requestBuilder.build();
//                        return chain.proceed(request);
//                    })
//                    .addInterceptor(logging)
//                    .build();
//
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(okHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//}









package com.example.capstoneproject.api;

import android.content.Context;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiClient {

    private static Retrofit retrofit;

    // ✅ BASE URL MUST END HERE
    private static final String BASE_URL =
            "https://uep-smartfarming-backend.onrender.com/";

    public static Retrofit getClient(Context context) {

        if (retrofit == null) {

            HttpLoggingInterceptor logging =
                    new HttpLoggingInterceptor(message ->
                            Log.d("RETROFIT_DEBUG", message));

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request original = chain.request();

                        Request.Builder builder = original.newBuilder()
                                .header("Accept", "application/json");

                        String token = MySessionManager.getToken(context);
                        if (token != null && !token.isEmpty()) {
                            builder.header("Authorization", "Bearer " + token);
                        }

                        return chain.proceed(builder.build());
                    })
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
