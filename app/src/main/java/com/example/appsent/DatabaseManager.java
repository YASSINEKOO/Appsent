package com.example.appsent;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseManager {
    private final DatabaseHelper dbHelper;
    private final SimpleDateFormat dateFormat;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    // TODO etudiant

    public void addEtudiant(Etudiant etudiant) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.KEY_LAST_NAME, etudiant.getNom());
        values.put(DatabaseContract.KEY_FIRST_NAME, etudiant.getPrenom());
        values.put(DatabaseContract.KEY_CNE, etudiant.getCNE());
        db.insert(DatabaseContract.TABLE_ETUDIANTS, null, values);
        db.close();
    }

    public Etudiant getEtudiant(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.TABLE_ETUDIANTS, new String[]{DatabaseContract.KEY_ID,
                        DatabaseContract.KEY_LAST_NAME, DatabaseContract.KEY_FIRST_NAME, DatabaseContract.KEY_CNE},
                DatabaseContract.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        Etudiant etudiant = null;
        if (cursor != null && cursor.moveToFirst()) {
            etudiant = new Etudiant(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3));
            cursor.close();
        }
        db.close();
        return etudiant;
    }


    public List<Etudiant> getAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.TABLE_ETUDIANTS + " ORDER BY " + DatabaseContract.KEY_ID, null);
        if (cursor.moveToFirst()) {
            do {
                Etudiant etudiant = new Etudiant(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3));
                etudiants.add(etudiant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return etudiants;
    }

    public void updateEtudiant(int id, Etudiant etudiant) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.KEY_FIRST_NAME, etudiant.getPrenom());
        values.put(DatabaseContract.KEY_LAST_NAME, etudiant.getNom());
        values.put(DatabaseContract.KEY_CNE, etudiant.getCNE());
        int rowsAffected = db.update(DatabaseContract.TABLE_ETUDIANTS, values,
                DatabaseContract.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean deleteEtudiant(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete(DatabaseContract.TABLE_ETUDIANTS, DatabaseContract.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }


    public List<Date> getAbsencesForStudent(Etudiant etudiant) {
        List<Date> absences = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.TABLE_DATE_ABSENCE, new String[]{DatabaseContract.KEY_DATE},
                DatabaseContract.KEY_ID_ETUDIANT + "=?", new String[]{String.valueOf(etudiant.getId())},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                try {
                    absences.add(dateFormat.parse(cursor.getString(0)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return absences;
    }

    // TODO absent

    public void markAbsent(Etudiant etudiant, Date date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        DateAbsence absence = new DateAbsence(0, etudiant.getId(), date);

        // Inserting data into the table
        values.put(DatabaseContract.KEY_ID_ETUDIANT, absence.getIdEtudiant());
        values.put(DatabaseContract.KEY_DATE, dateFormat.format(absence.getDate()));
        db.insert(DatabaseContract.TABLE_DATE_ABSENCE, null, values);
        long insertedRowId = db.insert(DatabaseContract.TABLE_DATE_ABSENCE, null, values);
        Log.d("Database", "Inserted absence record with ID: " + insertedRowId);
        Log.d("Database", "Inserted student ID: " + absence.getIdEtudiant() + ", Date: " + dateFormat.format(absence.getDate()));
        db.close();
    }

    // Function to get absences for a student on a specific day

    public void unmarkAbsent(Etudiant etudiant, Date date) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Convert the Date object to a formatted string
        String dateString = dateFormat.format(date);

        DateAbsence absence = new DateAbsence(0, etudiant.getId(), date);

        db.delete(DatabaseContract.TABLE_DATE_ABSENCE,
                DatabaseContract.KEY_ID_ETUDIANT + "=? AND " + DatabaseContract.KEY_DATE + "=?",
                new String[]{String.valueOf(absence.getIdEtudiant()), dateString});

        db.close();
    }

    public boolean isStudentAbsentOnDate(Etudiant etudiant, Date date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Convert the Date object to a formatted string
        String dateString = dateFormat.format(date);

        // Query the database to check if there is an absence record for the student on the given date
        Cursor cursor = db.query(DatabaseContract.TABLE_DATE_ABSENCE, null,
                DatabaseContract.KEY_ID_ETUDIANT + "=? AND " + DatabaseContract.KEY_DATE + "=?",
                new String[]{String.valueOf(etudiant.getId()), dateString}, null, null, null);

        boolean isAbsent = cursor.getCount() > 0;
        Log.d("Database", "Cursor count for student " + etudiant.getId() + " on date " + dateString + ": " + cursor.getCount());

        cursor.close();
        db.close();

        return isAbsent;
    }

    public List<Etudiant> getStudentsAbsentOnDate(Date date) {
        List<Etudiant> absentStudents = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String dateString = dateFormat.format(date);

        Cursor cursor = db.query(DatabaseContract.TABLE_DATE_ABSENCE, new String[]{DatabaseContract.KEY_ID_ETUDIANT},
                DatabaseContract.KEY_DATE + "=?", new String[]{dateString}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int studentId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.KEY_ID_ETUDIANT));
                Etudiant student = getEtudiant(studentId);
                if (student != null) {
                    absentStudents.add(student);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return absentStudents;
    }

}