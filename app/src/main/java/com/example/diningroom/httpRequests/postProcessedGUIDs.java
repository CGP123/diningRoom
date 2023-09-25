package com.example.diningroom.httpRequests;

import android.content.Context;
import android.util.Log;

import com.example.diningroom.Database.DatabaseManager;
import com.example.diningroom.client.ProcessedGUIDs;
import com.example.diningroom.client.UserClient;
import com.example.diningroom.config;
import com.example.diningroom.report.ReportParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class postProcessedGUIDs {
    Context context;
    private DatabaseManager dbManager;
    ReportParams reportParams;

    public postProcessedGUIDs(Context context) {
        this.context = context;
        dbManager = new DatabaseManager(context);
        reportParams = new ReportParams(context);
    }
    public void postData(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.postDataPath)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        UserClient userClient = retrofit.create(UserClient.class);
        dbManager.openDB();
        List<String> propuskGuids = dbManager.readGUIDsFromEmployeeDB();
        ProcessedGUIDs processedGUIDs = new ProcessedGUIDs(reportParams.getImei(),propuskGuids);
        Log.d("MyLog", ""+ propuskGuids);
        Call<Void> call = userClient.postProcessedGUIDs(processedGUIDs);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.d("MyLog", "Code: " + response.code());
                }
                else {
                    Log.d("MyLog", "Успешная отправка данных о пропусках");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("MyLog", t.getMessage());
            }
        });
    }

}
