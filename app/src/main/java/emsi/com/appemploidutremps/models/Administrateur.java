package emsi.com.appemploidutremps.models;

public class Administrateur extends User {

    public Administrateur() {
    }

    public Administrateur(String id, String nom, String prenom, String adresse, int age, String role) {
        super(id, nom, prenom, adresse, age, role);
    }
}
