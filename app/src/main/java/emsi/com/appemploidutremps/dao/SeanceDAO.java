package emsi.com.appemploidutremps.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import emsi.com.appemploidutremps.models.Seance;

public class SeanceDAO {

    private static final String TAG ="SeanceDAO" ;
    private CollectionReference seanceDAO=FirebaseFirestore.getInstance().collection("Seance");

    private static final SeanceDAO ourInstance = new SeanceDAO();

    public static SeanceDAO getInstance() {
        return ourInstance;
    }

    private SeanceDAO() {
    }

    public CollectionReference getSeanceDAO() {
        return seanceDAO;
    }

    public void setSeanceDAO(CollectionReference seanceDAO) {
        this.seanceDAO = seanceDAO;
    }

    public void addSeance(Seance seance){
        seanceDAO.document().set(seance);
    }
}
