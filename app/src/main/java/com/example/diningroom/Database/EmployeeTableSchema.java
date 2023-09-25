package com.example.diningroom.Database;

public class EmployeeTableSchema {
    public static final String TABLE_NAME = "employee_table";
    public static final String _ID = "_id";
    public static final String SURNAME = "surname";
    public static final String NAME = "name";
    public static final String PATRONYMIC = "patronymic";
    public static final String PASS_NUMBER = "pass_number";
    public static final String ORGANIZATION = "organization";
    public static final String ACTIVE = "active";
    public static final String VALID_UNTIL ="valid_until";
    public static final String PHOTO_PASS= "photo_pass";
    public static final String PROPUSK_GUID = "propusk_guid";
    public static final String TABLE_SCHEMA = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + SURNAME + " TEXT," +
            NAME + " TEXT," + PATRONYMIC + " TEXT," + PASS_NUMBER + " TEXT," + ORGANIZATION + " TEXT," +
            ACTIVE + " TEXT," + VALID_UNTIL + " TEXT," + PHOTO_PASS + " TEXT," + PROPUSK_GUID +" TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
