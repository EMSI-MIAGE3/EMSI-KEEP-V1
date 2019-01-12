package emsi.com.appemploidutremps.models;

import java.util.Calendar;
import java.util.Date;

public class Seance {

    private String matiere;
    private String description;
    private Date jour;
    private Date dateDebut;
    private Date dateFin;
    private Type type;
    private String note;
    private String classeId;
    private Professeur professeur;
    private int groupe;

    public Seance() {
    }

    public int getGroupe() {
        return groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getJour() {
        return jour;
    }

    public void setJour(Date jour) {
        this.jour = jour;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getClasseId() {
        return classeId;
    }

    public void setClasseId(String classeId) {
        this.classeId = classeId;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "matiere='" + matiere + '\'' +
                ", description='" + description + '\'' +
                ", jour=" + jour +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", type=" + type +
                ", note='" + note + '\'' +
                ", classeId='" + classeId + '\'' +
                ", professeur=" + professeur +
                ", groupe=" + groupe +
                '}';
    }
}
