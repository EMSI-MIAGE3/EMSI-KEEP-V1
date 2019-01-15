package emsi.com.appemploidutremps.models;

import java.io.Serializable;

public class SeanceTime  implements Serializable {

    private int heure,minute;

    public SeanceTime(int heure, int minute) {
        this.heure = heure;
        this.minute = minute;
    }

    public SeanceTime() {
    }

    public int getHeure() {
        return heure;
    }

    public void setHeure(int heure) {
        this.heure = heure;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "SeanceTime{" +
                "heure=" + heure +
                ", minute=" + minute +
                '}';
    }
}
