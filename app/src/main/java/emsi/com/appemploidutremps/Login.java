package emsi.com.appemploidutremps;

import android.Manifest;
import android.opengl.ETC1Util;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.UserDataConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emsi.com.appemploidutremps.dao.UserDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Etudiant;
import emsi.com.appemploidutremps.models.User;


public class Login extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private CollectionReference classeDAO=FirebaseFirestore.getInstance().collection("Cities");
    private FirebaseAuth userAuth=FirebaseAuth.getInstance();
    String email ="test@gmail.com";
    String password ="123456";
    private String TAG="UserSingedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = userAuth.getCurrentUser();
        if(currentUser==null)
            Log.w("UserSingedIn",currentUser+"");
        else{
            userAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = userAuth.getCurrentUser();
                                UserDAO.getInstance().getUserDAO()
                                        .whereEqualTo("id",user.getUid()).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                               Log.w(TAG,"UserIS=>"+document.toObject(Etudiant.class).toString());
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                            }

                        }
                    });
        }

     /*   userAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = userAuth.getCurrentUser();
                            User etudiant=new Etudiant(user.getUid(),"MANNANE","Ibrahim","Adresse",26,1);
                            userDAO.document().set(etudiant);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }
                    }
                });*/

       /* Map<String,Etudiant> listEtudiant=new HashMap<>();
        listEtudiant.put("1",new Etudiant("1","Mannen","ibr","adresse",26,1));
        listEtudiant.put("2",new Etudiant("1","Hachimi","abir","adresse",22,2));

        Classe classe= new Classe("MIAGE4",null,listEtudiant);

        classeDAO.document().set(classe);
        */

       /*
       classeDAO.whereEqualTo("etudiants","1")

               .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                   Log.w(TAG,"UserIS=>");
                   for (QueryDocumentSnapshot document : task.getResult()) {
                       Log.w(TAG,"User=>");
                   }
               } else {
                   Log.d(TAG, "Error getting documents: ", task.getException());
               }
           }
       });*/


        UserDAO.getInstance().register(new User("Mannane.Ibrahim1@gmail.com","Administrateur"));



    }



}
