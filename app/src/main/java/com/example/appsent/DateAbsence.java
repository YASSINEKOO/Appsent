package com.example.appsent;

import java.util.Date;

public class DateAbsence {
    private  int idAbs;
    private  int idEtudiant;
    private  Date date;

    public DateAbsence(int idAbs, int idEtudiant, Date date) {
        this.idAbs = idAbs;
        this.idEtudiant = idEtudiant;
        this.date = date;
    }

    public int getIdAbs() {
        return idAbs;
    }

    public void setIdAbs(int idAbs) {
        this.idAbs = idAbs;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}