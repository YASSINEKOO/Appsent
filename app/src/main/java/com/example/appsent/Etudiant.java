package com.example.appsent;
import java.io.Serializable;

public class Etudiant implements Serializable {
    private int id;
    private String nom;
    private String prenom;
    private String CNE;

    public Etudiant(int id, String nom, String prenom, String CNE) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.CNE = CNE;
    }

    // Getters and no Setters
    public int getId() {
        return id;
    }

    public void setId(int newid) {
       this.id=newid;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getCNE() {
        return CNE;
    }


}
