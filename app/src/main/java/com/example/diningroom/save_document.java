package com.example.diningroom;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.diningroom.Database.DatabaseManager;
import com.example.diningroom.Employee.Employee;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class save_document extends AppCompatActivity implements View.OnClickListener {

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_document);
        findViewById(R.id.BTN_saveAndDelete).setOnClickListener(this);
        findViewById(R.id.BTN_simpleSave).setOnClickListener(this);
        findViewById(R.id.BTN_back).setOnClickListener(this);
        dbManager = new DatabaseManager(this);
        dbManager.openDB();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.BTN_saveAndDelete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.confirm_window, null);
            builder.setView(cl);
            Button yesButton = cl.findViewById(R.id.yesButton);
            Button noButton = cl.findViewById(R.id.noButton);
            TextView text = cl.findViewById(R.id.informationText);
            text.setText("Вы уверены? После нажатия кнопки ДА следующий отчёт начнётся заново");
            Dialog dialog = builder.create();
            dialog.show();
            noButton.setOnClickListener(view1 -> {
                dialog.dismiss();
            });
            yesButton.setOnClickListener(view1 -> {
                try {
                    saveToExcelDocument("Отчёт.xls", dbManager.getEmployeeFromDocument());
                    Toast.makeText(this, "Отчёт сохранён", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Отчёт не сохранён", Toast.LENGTH_SHORT).show();
                }

                dbManager.deleteDocumentTable();
                dialog.dismiss();
            });

        }
        else if (view.getId() == R.id.BTN_simpleSave) {
            try {
                saveToExcelDocument("Отчёт.xls", dbManager.getEmployeeFromDocument());
                Toast.makeText(this, "Отчёт сохранён", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Отчёт не сохранён", Toast.LENGTH_SHORT).show();
            }

        }
        else if (view.getId() == R.id.BTN_back){
            save_document.super.finish();
        }
    }

    private void saveToExcelDocument(String fileName, List<Employee> employees) throws Exception {
        Workbook workbook = null;
        if (fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        }
        else if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        }
        else {
            throw new Exception("invalid file name, should be xls or xlsx");
        }
        Sheet sheet = workbook.createSheet("Отчёт");
        int rowIndex = 0;
        for (Employee employee : employees) {
            Row row = sheet.createRow(rowIndex++);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(employee.getFullName());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(employee.getOrganization());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(employee.getPassNumber());
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(employee.getDate());
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(employee.getTime());
        }

        @SuppressLint("SdCardPath") FileOutputStream fos = new FileOutputStream(new File("/sdcard/Отчёты/", fileName));
        workbook.write(fos);
        fos.close();
        Log.d("MyLog", "Файл сохранён");
    }
}