package com.example.diningroom.RFIDReader;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.example.diningroom.config;

import java.io.IOException;

public class BluetoothConnection {
    private final Context context;
    private final BluetoothAdapter btAdapter;
    private BluetoothConnectionThread bluetoothConnectionThread;
    RFIDReader rfidReader;
    public BluetoothConnection(Context context) {
        this.context = context;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        rfidReader = new RFIDReader(context);
    }

    public void connect() {
        String mac = "";
        try {
            mac = rfidReader.getMAC_KEY();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (mac == null){
            mac = config.defaultMac;
        }
        if (!btAdapter.isEnabled()) return;
        BluetoothDevice device = btAdapter.getRemoteDevice(mac);
        if (device == null) return;
        bluetoothConnectionThread = new BluetoothConnectionThread(context, btAdapter, device);
        bluetoothConnectionThread.start();
    }

    public void disconnect() {
        if (bluetoothConnectionThread != null) {
            bluetoothConnectionThread.closeConnection();
        }
    }


}
