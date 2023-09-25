package com.example.diningroom.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EmployeeTableSchema.TABLE_SCHEMA);
        sqLiteDatabase.execSQL(DocumentTableSchema.TABLE_SCHEMA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(EmployeeTableSchema.DROP_TABLE);
        sqLiteDatabase.execSQL(DocumentTableSchema.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
