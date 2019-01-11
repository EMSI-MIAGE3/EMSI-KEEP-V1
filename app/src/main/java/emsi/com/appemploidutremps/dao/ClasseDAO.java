package emsi.com.appemploidutremps.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import emsi.com.appemploidutremps.models.Classe;

public class ClasseDAO {


    private static final String TAG ="ClasseDAO" ;
    private CollectionReference classeDAO=FirebaseFirestore.getInstance().collection("Classe");



    private static final ClasseDAO ourInstance = new ClasseDAO();

    public static ClasseDAO getInstance() {
        return ourInstance;
    }

    private ClasseDAO() {
    }


    public CollectionReference getClasseDAO() {
        return classeDAO;
    }

    public void setClasseDAO(CollectionReference classeDAO) {
        this.classeDAO = classeDAO;
    }

    public void addClasse(Classe classe){
        classeDAO.document().set(classe);
    }


}
