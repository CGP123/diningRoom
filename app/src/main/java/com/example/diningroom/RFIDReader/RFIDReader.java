package com.example.diningroom.RFIDReader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class RFIDReader {
    public static ArrayList<String> passNumber = new ArrayList<String>();
    public static boolean IsConnected = false;
    public static boolean restartReader = false;
    Context context;
    SharedPreferences DBSharedPreferences;
    String MAC_KEY;
    String macValue;
    public RFIDReader(Context context) {
        this.context = context;
        DBSharedPreferences = context.getSharedPreferences("lastDBUpdate", Context.MODE_PRIVATE);
        MAC_KEY = DBSharedPreferences.getString("macValue", macValue);
    }

    public String getMAC_KEY() {
        return MAC_KEY;
    }

}
