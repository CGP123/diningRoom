package com.example.diningroom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.diningroom.Database.DatabaseManager;
import com.example.diningroom.Employee.Employee;
import com.example.diningroom.RFIDReader.BluetoothConnection;
import com.example.diningroom.RFIDReader.RFIDReader;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothConnection btConnection;
    private boolean btPermission = false;
    private final int BT_REQ_PERM = 1101;
    private static final int REQUEST_ENABLE_BT = 1;
    private DatabaseManager dbManager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RFIDReader.IsConnected = false;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter f2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bReceiver, f2);
        getBTPermission();
        requestEnableBT();
        checkBTConnection();
        getDataFromPassReader();
        checkFilePermissions();
        dbManager = new DatabaseManager(this);
        dbManager.openDB();
        btConnection = new BluetoothConnection(this);

        findViewById(R.id.BTN_save).setOnClickListener(this);
        findViewById(R.id.BTN_settings).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter f2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(bReceiver, f2);
        RFIDReader.IsConnected = false;
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(bReceiver);
    }

    private void checkBTConnection() {
        Runnable runnable = () -> {
            while (true) {
                if (!RFIDReader.IsConnected) {
                    Log.d("MyLog", "Идёт подключение к Bluetooth модулю");
                    btConnection = new BluetoothConnection(this);
                    btConnection.connect();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (RFIDReader.restartReader) {
                        RFIDReader.restartReader = false;
                        break;
                    }
                }
            }
        };
        Thread btThread = new Thread(runnable);
        btThread.start();
    }

    private void getDataFromPassReader() {
        Runnable runnable = () -> {
            while (true) {
                if (!RFIDReader.passNumber.isEmpty()) {
                    try {
                        StringBuilder passNumberBuilder = new StringBuilder(RFIDReader.passNumber.get(0).trim());
                        while (passNumberBuilder.length() < 10) {
                            passNumberBuilder.insert(0, "0");
                        }
                        addEmployeeToDocument(passNumberBuilder.toString());
                        RFIDReader.passNumber.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void addEmployeeToDocument(String passNumber) {
        Date date = new Date();
        if (dbManager.checkPassNumber(passNumber) && !dbManager.checkRowDuplicate(timeFormat.format(date))) {
            Employee employee = dbManager.getEmployeeByPassNumber(passNumber);
            Log.d("MyLog", employee + "");
            dbManager.addEmployeeToDocument(employee);
            showCustomToast(true, employee.getFullName());
        }
        else {
            showCustomToast(false, null);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showCustomToast(boolean result, String fullName) {
        ContextCompat.getMainExecutor(this).execute(() -> {
            LayoutInflater layoutInflater = getLayoutInflater();
            View layout = layoutInflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));
            TextView resultText = layout.findViewById(R.id.toast_text);
            if (result) {
                resultText.setText(fullName + " успешно");
                resultText.setBackgroundColor(Color.GREEN);
            }
            else {
                resultText.setText("Сотрудник не найден");
                resultText.setBackgroundColor(Color.RED);
            }
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }
            else {
                Log.d("MyLog", "Проверьте разрешения на запись/чтение");
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected void requestEnableBT() {
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, "Это устройство не поддерживает соединение по Bluetooth", Toast.LENGTH_SHORT).show();
        }
        else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == BT_REQ_PERM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                btPermission = true;
            }
        }
        else super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getBTPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, BT_REQ_PERM);
        }
        else btPermission = true;
    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    if (device.getName().equals("ESP32")) {
                        Log.d("MyLog", "Bluetooth модуль отключен");
                        ContextCompat.getMainExecutor(MainActivity.this).execute(() -> {
                            findViewById(R.id.IV_rfidOff).setVisibility(View.VISIBLE);
                            findViewById(R.id.IV_rfidOn).setVisibility(View.INVISIBLE);
                            findViewById(R.id.IV_rfidOff).bringToFront();
                            RFIDReader.IsConnected = false;
                        });
                        btConnection.disconnect();
                        device = null;
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.BTN_save) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.password_window, null);
            builder.setView(cl);
            Button enterButton = cl.findViewById(R.id.enterInSettingsButton);
            TextView password = cl.findViewById(R.id.settingsPassword);
            Dialog dialog = builder.create();
            dialog.show();
            enterButton.setOnClickListener(view1 -> {
                if (password.getText().toString().equals(config.userPassword)) {
                    dialog.dismiss();
                    Intent intent = new Intent(this, save_document.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (view.getId() == R.id.BTN_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.password_window, null);
            builder.setView(cl);
            Button enterButton = cl.findViewById(R.id.enterInSettingsButton);
            TextView password = cl.findViewById(R.id.settingsPassword);
            Dialog dialog = builder.create();
            dialog.show();
            enterButton.setOnClickListener(view1 -> {
                if (password.getText().toString().equals(config.userPassword)) {
                    dialog.dismiss();
                    Intent intent = new Intent(this, Settings.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}