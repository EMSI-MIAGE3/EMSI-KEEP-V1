package emsi.com.appemploidutremps.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Classe implements Serializable {


    private String id;
    private String nom;
    private Filiere filiere;
    private List<String> etudiants;

    public Classe(String nom, Filiere filiere, List<String> etudiants) {
        this.nom = nom;
        this.filiere = filiere;
        this.etudiants = etudiants;
    }

    public Classe() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public List<String> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<String> etudiants) {
        this.etudiants = etudiants;
    }


    @Override
    public String toString() {
        return filiere+" "+nom ;

    }
}
