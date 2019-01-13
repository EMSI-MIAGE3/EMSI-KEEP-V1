package emsi.com.appemploidutremps.models;

import java.io.Serializable;

public class Filiere implements Serializable {

    private String nom;
    private int annee;

    public Filiere() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    @Override
    public String toString() {
        return
                annee + ""+ nom;
    }
}
