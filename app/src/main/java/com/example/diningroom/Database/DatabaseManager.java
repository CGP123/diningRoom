package com.example.diningroom.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ProgressBar;

import com.example.diningroom.Employee.Employee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseManager {
    DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private static DatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    private Context context;
    ProgressBar progressBar;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        this.context = context;
    }

    public void openDB() {
        db = dbHelper.getWritableDatabase();
    }

    public void closeDB() {
        dbHelper.close();
    }

    //Employee table

    public void addNewEmployee(String surname, String name, String patronymic, String pass_number,
                               String organization, String active, String valid_until, String photo_pass,
                               String propusk_guid) {
        ContentValues cv = new ContentValues();
        cv.put(EmployeeTableSchema.SURNAME, surname);
        cv.put(EmployeeTableSchema.NAME, name);
        cv.put(EmployeeTableSchema.PATRONYMIC, patronymic);
        cv.put(EmployeeTableSchema.PASS_NUMBER, pass_number);
        cv.put(EmployeeTableSchema.ORGANIZATION, organization);
        cv.put(EmployeeTableSchema.ACTIVE, active);
        cv.put(EmployeeTableSchema.VALID_UNTIL, valid_until);
        cv.put(EmployeeTableSchema.PHOTO_PASS, photo_pass);
        cv.put(EmployeeTableSchema.PROPUSK_GUID, propusk_guid);
        db.insert(EmployeeTableSchema.TABLE_NAME, null, cv);
    }

    @SuppressLint("Range")
    public boolean checkEmployeeDuplicate(String propusk_guid) {
        Cursor cursor = db.rawQuery("SELECT " + EmployeeTableSchema._ID + " FROM " + EmployeeTableSchema.TABLE_NAME +
                " WHERE " + EmployeeTableSchema.PROPUSK_GUID + "=?", new String[]{propusk_guid});
        try {
            return cursor.moveToFirst();
        } catch (Exception e) {
            cursor.close();
            return false;
        }
    }

    public void updateEmployee(String surname, String name, String patronymic, String pass_number,
                               String organization, String active, String valid_until, String photo_pass,
                               String propusk_guid) {
        ContentValues cv = new ContentValues();
        cv.put(EmployeeTableSchema.SURNAME, surname);
        cv.put(EmployeeTableSchema.NAME, name);
        cv.put(EmployeeTableSchema.PATRONYMIC, patronymic);
        cv.put(EmployeeTableSchema.PASS_NUMBER, pass_number);
        cv.put(EmployeeTableSchema.ORGANIZATION, organization);
        cv.put(EmployeeTableSchema.ACTIVE, active);
        cv.put(EmployeeTableSchema.VALID_UNTIL, valid_until);
        cv.put(EmployeeTableSchema.PHOTO_PASS, photo_pass);
        db.update(EmployeeTableSchema.TABLE_NAME, cv, EmployeeTableSchema.PROPUSK_GUID + " =?", new String[]{propusk_guid});
    }

    @SuppressLint("Range")
    public boolean checkPassNumber(String passNumber) {
        Cursor cursor = db.rawQuery("SELECT " + EmployeeTableSchema._ID + " FROM " + EmployeeTableSchema.TABLE_NAME +
                " WHERE " + EmployeeTableSchema.PASS_NUMBER + "=?", new String[]{passNumber});
        return cursor.moveToFirst();
    }

    @SuppressLint("Range")
    public Employee getEmployeeByPassNumber(String passNumber) {
        Employee employee;
        Cursor cursor = db.rawQuery("SELECT " + EmployeeTableSchema.SURNAME + "," + EmployeeTableSchema.NAME +
                "," + EmployeeTableSchema.PATRONYMIC + "," + EmployeeTableSchema.ORGANIZATION +
                " FROM " + EmployeeTableSchema.TABLE_NAME + " WHERE " +
                EmployeeTableSchema.PASS_NUMBER + "=?", new String[]{passNumber});
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            String employeeSurname = cursor.getString(cursor.getColumnIndex(EmployeeTableSchema.SURNAME));
            String employeeName = cursor.getString(cursor.getColumnIndex(EmployeeTableSchema.NAME));
            String employeePatronymic = cursor.getString(cursor.getColumnIndex(EmployeeTableSchema.PATRONYMIC));
            String employeeOrganization = cursor.getString(cursor.getColumnIndex(EmployeeTableSchema.ORGANIZATION));
            String employeeFullName = employeeSurname + " " + employeeName + " " + employeePatronymic;
            Date date = new Date();
            String passedDate = dateFormat.format(date);
            String passedTime = timeFormat.format(date);
            employee = new Employee(employeeFullName, passNumber, employeeOrganization, passedDate, passedTime);
        }
        else {
            employee = null;
        }
        cursor.close();
        return employee;
    }

    public List<String> readGUIDsFromEmployeeDB() {
        List<String> guidsList = new ArrayList<>();
        Cursor cursor = db.query(EmployeeTableSchema.TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String propusk_guid = cursor.getString(cursor.getColumnIndex(EmployeeTableSchema.PROPUSK_GUID));
            guidsList.add(propusk_guid);
        }
        cursor.close();
        return guidsList;
    }

    //Document table

    public void addEmployeeToDocument(Employee employee) {
        ContentValues cv = new ContentValues();
        cv.put(DocumentTableSchema.FULL_NAME, employee.getFullName());
        cv.put(DocumentTableSchema.PASS_NUMBER, employee.getPassNumber());
        cv.put(DocumentTableSchema.ORGANIZATION, employee.getOrganization());
        cv.put(DocumentTableSchema.DATE, employee.getDate());
        cv.put(DocumentTableSchema.TIME, employee.getTime());
        db.insert(DocumentTableSchema.TABLE_NAME, null, cv);
    }

    @SuppressLint("Range")
    public boolean checkRowDuplicate(String time) {
        Cursor cursor = db.rawQuery("SELECT " + DocumentTableSchema._ID + " FROM " + DocumentTableSchema.TABLE_NAME +
                " WHERE " + DocumentTableSchema.TIME + "=?", new String[]{time});
        try {
            return cursor.moveToFirst();
        } catch (Exception e) {
            cursor.close();
            return false;
        }
    }

    public List<Employee> getEmployeeFromDocument() {
        List<Employee> employees = new ArrayList<>();
        Cursor cursor = db.query(DocumentTableSchema.TABLE_NAME, null, null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex(DocumentTableSchema.FULL_NAME));
            @SuppressLint("Range") String organization = cursor.getString(cursor.getColumnIndex(DocumentTableSchema.ORGANIZATION));
            @SuppressLint("Range") String pass_number = cursor.getString(cursor.getColumnIndex(DocumentTableSchema.PASS_NUMBER));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DocumentTableSchema.DATE));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DocumentTableSchema.TIME));
            Employee employee = new Employee(fullName, organization, pass_number, date, time);
            employees.add(employee);
        }
        cursor.close();
        return employees;
    }



    public void deleteDocumentTable() {
        db.delete(DocumentTableSchema.TABLE_NAME, null, null);
    }
}
