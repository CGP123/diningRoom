package com.example.diningroom.report;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.diningroom.config;

public class ReportParams {
    Context context;
    SharedPreferences DBSharedPreferences;
    String imeiValue;
    String imei = config.defaultImei;
    public ReportParams(Context context) {
        this.context = context;
        DBSharedPreferences = context.getSharedPreferences("lastDBUpdate", Context.MODE_PRIVATE);
        imei = DBSharedPreferences.getString("imeiValue", imeiValue);
    }

    public String getImei() {
        return imei;
    }
    public static boolean dataChanged = false;
    public static boolean employeeInDistribution = false;
    public static int selectedPosition = 0;

}
