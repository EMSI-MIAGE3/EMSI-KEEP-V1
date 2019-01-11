package emsi.com.appemploidutremps.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import emsi.com.appemploidutremps.models.User;

public class UserDAO {

    private static final String TAG ="UserDAO" ;
    private CollectionReference userDAO=FirebaseFirestore.getInstance().collection("User");
    private FirebaseAuth userAuth=FirebaseAuth.getInstance();

    private static final UserDAO ourInstance = new UserDAO();

    public static UserDAO getInstance() {
        return ourInstance;
    }

    private UserDAO() {
    }

    public CollectionReference getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(CollectionReference userDAO) {
        this.userDAO = userDAO;
    }

    public FirebaseAuth getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(FirebaseAuth userAuth) {
        this.userAuth = userAuth;
    }

    public User register(final User userToAdd){
        String password="123456";
        userAuth.createUserWithEmailAndPassword(userToAdd.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            userToAdd.setId(user.getUid());
                            userDAO.document().set(userToAdd);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }
                    }
                });

        return null;
    }
}
