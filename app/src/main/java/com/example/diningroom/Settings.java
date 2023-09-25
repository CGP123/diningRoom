package com.example.diningroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.diningroom.Database.DatabaseManager;
import com.example.diningroom.httpRequests.getPassData;
import com.example.diningroom.httpRequests.postProcessedGUIDs;
import com.example.diningroom.report.ReportParams;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    Date currentDate = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    getPassData getPassData;
    ProgressBar progressBar;
    postProcessedGUIDs processedGUIDs;
    private DatabaseManager dbManager;
    SharedPreferences DBSharedPreferences;
    SharedPreferences.Editor DBSharedPreferencesEditor;
    String lastEmployeeDBUpdate;
    String imeiValue;
    String macValue;
    TextView employeeDBLastUpdateText;
    EditText imeiText;
    EditText macText;

    ReportParams reportParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        employeeDBLastUpdateText = findViewById(R.id.employeeDBLastUpdateText);
        findViewById(R.id.settingsBackButton).setOnClickListener(this);
        findViewById(R.id.updateEmployeeDBButton).setOnClickListener(this);
        findViewById(R.id.saveImeiAndMACButton).setOnClickListener(this);
        imeiText = findViewById(R.id.imeiText);
        macText = findViewById(R.id.macText);
        dbManager = new DatabaseManager(Settings.this);
        DBSharedPreferences = getSharedPreferences("lastDBUpdate", Context.MODE_PRIVATE);
        DBSharedPreferencesEditor = DBSharedPreferences.edit();
        getPassData = new getPassData(this);
        employeeDBLastUpdateText.setText(DBSharedPreferences.getString("employeeDBLastUpdate", lastEmployeeDBUpdate));
        imeiText.setText(DBSharedPreferences.getString("imeiValue", imeiValue));
        macText.setText(DBSharedPreferences.getString("macValue", macValue));
        progressBar = findViewById(R.id.progressBar);
        processedGUIDs = new postProcessedGUIDs(Settings.this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.settingsBackButton) {
            Settings.super.finish();
        } else if (view.getId() == R.id.updateEmployeeDBButton) {
            findViewById(R.id.progressBarContainer).setVisibility(View.VISIBLE);
            init();
        }
        else if (view.getId() == R.id.saveImeiAndMACButton){
            imeiValue = imeiText.getText().toString();
            macValue = macText.getText().toString();
            DBSharedPreferencesEditor.putString("imeiValue", imeiValue);
            DBSharedPreferencesEditor.putString("macValue", macValue);
            DBSharedPreferencesEditor.apply();
        }
    }

    private void init() {
        Runnable runnable = () -> {
            getPassData.getJSON();
            int size = getPassData.getLength();
            if (size > 0) {
                float progress = 0;
                int finalProgress = 0;
                dbManager.openDB();
                Log.d("MyLog", size+"");
                for (int i = 0; i < size; i++) {
                    progress = progress + (float) 100 / size;
                    if (progress >= 1) {
                        progress = 0;
                        finalProgress += 1;
                        int currentProgress = finalProgress;
                        ContextCompat.getMainExecutor(this).execute(() -> {
                                progressBar.setProgress(currentProgress);
                        });
                    }
                    if (!getPassData.getSurname(i).equals("") && !getPassData.getName(i).equals("")
                            && !getPassData.getPassNumber(i).equals("")) {
                        if (dbManager.checkEmployeeDuplicate(getPassData.getPropuskGuid(i))) {
                            String passNumber = getPassData.getPassNumber(i).trim();
                            StringBuilder passNumberBuilder = new StringBuilder(passNumber);
                            while (passNumberBuilder.length() < 10) {
                                passNumberBuilder.insert(0, "0");
                            }
                            passNumber = passNumberBuilder.toString();
                            dbManager.updateEmployee(getPassData.getSurname(i).trim(), getPassData.getName(i).trim(),
                                    getPassData.getPatronymic(i).trim(), passNumber,
                                    getPassData.getOrganization(i).trim(), getPassData.getActive(i).trim(),
                                    getPassData.getValidUntil(i).trim(), getPassData.getPhotoPath(i).trim(),
                                    getPassData.getPropuskGuid(i).trim());
                        } else {
                            String passNumber = getPassData.getPassNumber(i).trim();
                            StringBuilder passNumberBuilder = new StringBuilder(passNumber);
                            while (passNumberBuilder.length() < 10) {
                                passNumberBuilder.insert(0, "0");
                            }
                            passNumber = passNumberBuilder.toString();
                            Log.d("MyLog", "" + i);
                            dbManager.addNewEmployee(getPassData.getSurname(i).trim(), getPassData.getName(i).trim(),
                                    getPassData.getPatronymic(i).trim(), passNumber,
                                    getPassData.getOrganization(i).trim(), getPassData.getActive(i).trim(),
                                    getPassData.getValidUntil(i).trim(), getPassData.getPhotoPath(i).trim(),
                                    getPassData.getPropuskGuid(i).trim());
                        }
                    }
                }
                processedGUIDs.postData();
                ContextCompat.getMainExecutor(Settings.this).execute(() -> {
                    lastEmployeeDBUpdate = "Последнее обновление: " + dateFormat.format(currentDate);
                    employeeDBLastUpdateText.setText(lastEmployeeDBUpdate);
                    DBSharedPreferencesEditor.putString("employeeDBLastUpdate", lastEmployeeDBUpdate);
                    DBSharedPreferencesEditor.apply();

                });
            }
            ContextCompat.getMainExecutor(Settings.this).execute(() -> {
                findViewById(R.id.progressBarContainer).setVisibility(View.INVISIBLE);

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}