package emsi.com.appemploidutremps.models;

import java.io.Serializable;

public class User implements Serializable {

    protected String id;
    protected String email;
    protected String nom;
    protected String prenom;
    protected String adresse;
    protected int groupe;
    protected String role;



    public User(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public User() {
    }

    public User(String email, String nom, String prenom, String adresse, int groupe, String role) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.groupe = groupe;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getGroupe() {
        return groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", groupe=" + groupe +
                ", role='" + role + '\'' +
                '}';
    }
}

