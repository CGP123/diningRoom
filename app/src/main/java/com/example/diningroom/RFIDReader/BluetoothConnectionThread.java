package com.example.diningroom.RFIDReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.diningroom.R;

import java.io.IOException;

public class BluetoothConnectionThread extends Thread {
    private final Context context;
    private final BluetoothAdapter btAdapter;
    private final BluetoothDevice device;
    private BluetoothSocket mSocket;
    private static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

    @SuppressLint("MissingPermission")
    public BluetoothConnectionThread(Context context, BluetoothAdapter btAdapter, BluetoothDevice device) {
        this.context = context;
        this.btAdapter = btAdapter;
        this.device = device;
        try {
            mSocket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void run() {
        btAdapter.startDiscovery();
        try {
            if (!RFIDReader.IsConnected) {
                closeConnection();
                try {
                    mSocket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mSocket.connect();
            BluetoothReceiverThread receiveThread = new BluetoothReceiverThread(mSocket);
            receiveThread.start();
            RFIDReader.IsConnected = true;
            Log.d("MyLog", "Подключено");
            ContextCompat.getMainExecutor(context).execute(() -> {
                ((Activity) context).findViewById(R.id.IV_rfidOn).setVisibility(View.VISIBLE);
                ((Activity) context).findViewById(R.id.IV_rfidOff).setVisibility(View.INVISIBLE);
                ((Activity) context).findViewById(R.id.IV_rfidOn).bringToFront();
            });
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
            if (!RFIDReader.IsConnected) {
                Log.d("MyLog", "Ошибка подключения");
            }
        }
    }

    public void closeConnection() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
