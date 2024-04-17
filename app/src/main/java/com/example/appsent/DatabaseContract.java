package com.example.appsent;

public class DatabaseContract {
    // Etudiant table
    public static final String TABLE_ETUDIANTS = "ETUDIANTS";
    public static final String KEY_ID = "id";
    public static final String KEY_LAST_NAME = "nom";
    public static final String KEY_FIRST_NAME = "prenom";
    public static final String KEY_CNE = "CNE";

    // Date d'absence table
    public static final String TABLE_DATE_ABSENCE = "DATE_D_ABSENCE";
    public static final String KEY_ID_ABS = "idAbs";
    public static final String KEY_ID_ETUDIANT = "idEtudiant";
    public static final String KEY_DATE = "date";

    // Sql query

    public static final String CREATE_TABLE_ETUDIANT = "CREATE TABLE IF NOT EXISTS " + TABLE_ETUDIANTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LAST_NAME + " TEXT,"
            + KEY_FIRST_NAME + " TEXT," + KEY_CNE + " TEXT" + ")";

    public static final String CREATE_TABLE_ABSENCES = "CREATE TABLE IF NOT EXISTS " + TABLE_DATE_ABSENCE
            + "(" + KEY_ID_ABS + " INTEGER PRIMARY KEY," + KEY_ID_ETUDIANT + " INTEGER,"
            + KEY_DATE + " DATETIME DEFAULT CURRENT_DATE,"
            + " FOREIGN KEY (" + KEY_ID_ETUDIANT + ") REFERENCES " + TABLE_ETUDIANTS + "(" + KEY_ID + ")" + ")";
}
