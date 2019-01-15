package emsi.com.appemploidutremps.models;

import java.io.Serializable;

public enum Jour implements Serializable {
    DIMANCHE(0),
    LUNDI(1),
    MARDI(2),
    MERCREDI(3),
    JEUDI(4),
    VENDREDI(5),
    SAMEDI(6);

    private int nbrJour;
    Jour(int i) {
        nbrJour=i;
    }

    public int getNbrJour() {
        return nbrJour;
    }

    public void setNbrJour(int nbrJour) {
        this.nbrJour = nbrJour;
    }
}
