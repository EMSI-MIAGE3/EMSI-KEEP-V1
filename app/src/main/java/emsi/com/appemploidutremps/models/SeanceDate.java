package emsi.com.appemploidutremps.models;

import java.io.Serializable;
import java.util.Date;

public class SeanceDate  implements Serializable {

    private int jour;
    private Date date;

    public SeanceDate(int jour, Date date) {
        this.jour = jour;
        this.date = date;
    }

    public SeanceDate() {
    }

    public int getJour() {
        return jour;
    }

    public void setJour(int jour) {
        this.jour = jour;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SeanceDate{" +
                "jour=" + jour +
                ", date=" + date +
                '}';
    }
}
