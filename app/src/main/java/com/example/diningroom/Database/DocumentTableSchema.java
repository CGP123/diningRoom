package com.example.diningroom.Database;

public class DocumentTableSchema {
    public static final String TABLE_NAME = "document_table";
    public static final String _ID = "_id";
    public static final String FULL_NAME = "full_name";
    public static final String PASS_NUMBER = "pass_number";
    public static final String ORGANIZATION = "organization";
    public static final String DATE = "date";
    public static final String TIME ="time";
    public static final String TABLE_SCHEMA = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + FULL_NAME + " TEXT," +
            " TEXT," + PASS_NUMBER + " TEXT," + ORGANIZATION + " TEXT," +
            DATE + " TEXT," + TIME +" TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
