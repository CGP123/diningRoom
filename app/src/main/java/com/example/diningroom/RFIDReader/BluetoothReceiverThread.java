package com.example.diningroom.RFIDReader;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothReceiverThread extends Thread {
    private InputStream inputStream;

    public BluetoothReceiverThread(BluetoothSocket socket) {
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MyLog", "Ошибка создания входящего потока");
        }
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[40];
        while (true) {
            try {
                int size = inputStream.read(receiveBuffer);
                if (!new String(receiveBuffer, 0, size).trim().equals("")) {
                    String passNumber = new String(receiveBuffer, 0, size);
                    if (RFIDReader.passNumber.isEmpty() || !RFIDReader.passNumber.get(0).equals(passNumber)){
                        RFIDReader.passNumber.add(passNumber);
                    }

                    Log.d("MyLog", RFIDReader.passNumber.get(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
