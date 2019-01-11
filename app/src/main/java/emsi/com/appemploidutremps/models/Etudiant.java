package emsi.com.appemploidutremps.models;

public class Etudiant extends User{

    private int groupe;

    public Etudiant(String id, String nom, String prenom, String adresse, int age, String role, int groupe) {
        super(id, nom, prenom, adresse, age, role);
        this.groupe = groupe;
    }

    public Etudiant() {
    }

}
