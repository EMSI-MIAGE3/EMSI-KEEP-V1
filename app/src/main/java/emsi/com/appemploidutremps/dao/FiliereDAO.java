package emsi.com.appemploidutremps.dao;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Filiere;

public class FiliereDAO {

    private static final String TAG ="FiliereDAO" ;
    private CollectionReference filiereDAO=FirebaseFirestore.getInstance().collection("Filiere");


    private static final FiliereDAO ourInstance = new FiliereDAO();

    public static FiliereDAO getInstance() {
        return ourInstance;
    }

    private FiliereDAO() {
    }

    public CollectionReference getFiliereDAO() {
        return filiereDAO;
    }

    public void addFiliere(Filiere filiere){
        filiereDAO.document().set(filiere);
    }


}
