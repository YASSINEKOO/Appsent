package com.example.appsent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "base-appsent";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(DatabaseContract.CREATE_TABLE_ETUDIANT);
        db.execSQL(DatabaseContract.CREATE_TABLE_ABSENCES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed and recreate
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_ETUDIANTS);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_DATE_ABSENCE);
        onCreate(db);
    }
}
