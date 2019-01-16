package emsi.com.appemploidutremps.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Seance implements Comparable<Seance>, Serializable {

    private String id;
    private String matiere;
    private String description;
    private SeanceDate jour;
    private SeanceTime dateDebut;
    private SeanceTime dateFin;
    private String type;
    private String note;
    private String classeId;
    private Classe classe;
    private Professeur professeur;
    private List<Integer> groupe;
    private String salle;



    public Seance() {
    }

    public Seance(String matiere, String description, SeanceDate jour, SeanceTime dateDebut, SeanceTime dateFin,
                  String type, String note, String classeId, Professeur professeur, List<Integer> groupe, String salle) {
        this.matiere = matiere;
        this.description = description;
        this.jour = jour;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
        this.note = note;
        this.classeId = classeId;
        this.professeur = professeur;
        this.groupe = groupe;
        this.salle = salle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public SeanceDate getJour() {
        return jour;
    }

    public void setJour(SeanceDate jour) {
        this.jour = jour;
    }

    public SeanceTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(SeanceTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public SeanceTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(SeanceTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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

    public List<Integer> getGroupe() {
        return groupe;
    }

    public void setGroupe(List<Integer> groupe) {
        this.groupe = groupe;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "Seance{" +
                "id='" + id + '\'' +
                ", matiere='" + matiere + '\'' +
                ", description='" + description + '\'' +
                ", jour=" + jour +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", type='" + type + '\'' +
                ", note='" + note + '\'' +
                ", classeId='" + classeId + '\'' +
                ", classe=" + classe +
                ", professeur=" + professeur +
                ", groupe=" + groupe +
                ", salle='" + salle + '\'' +
                '}';
    }

    @Override
    public int compareTo(Seance s) {
        if(this.dateDebut.getHeure() >  s.dateDebut.getHeure()) return 1;
        else if(this.dateDebut.getHeure() <  s.dateDebut.getHeure()) return -1;
        return 0;
    }
}
