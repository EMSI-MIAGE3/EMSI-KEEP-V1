package emsi.com.appemploidutremps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import emsi.com.appemploidutremps.dao.ClasseDAO;
import emsi.com.appemploidutremps.dao.UserDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Etudiant;
import emsi.com.appemploidutremps.models.User;

public class LoginUser extends AppCompatActivity {


    private EditText email ;
    private EditText pw;
    private Button login;
    private String TAG = "LoginUserWkhalil";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        email = (EditText) findViewById(R.id.emailText);
        pw = (EditText) findViewById(R.id.pwText);
        login=(Button) findViewById(R.id.valider);

    }


    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseAuth userAuth = UserDAO.getInstance().getUserAuth();
        final FirebaseUser currentUser = userAuth.getCurrentUser();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    userAuth.signInWithEmailAndPassword(email.getText().toString(), pw.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = userAuth.getCurrentUser();

                                        Log.w(TAG, "UserIS=>" + user.getEmail());
                                        UserDAO.getInstance().getUserDAO()
                                                .whereEqualTo("id", user.getUid())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent intent=null;
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                User connectedUser=(User)document.toObject(User.class);
                                                                switch (connectedUser.getRole()){
                                                                    case "Etudiant" :intent=new Intent(LoginUser.this,CalendarTimeTible.class);
                                                                    break;
                                                                    case "Professeur" :intent=new Intent(LoginUser.this,CalendarTimeTible.class);
                                                                    break;
                                                                    case "Administrateur" :intent=new Intent(LoginUser.this,AdminCP.class);
                                                                    break;
                                                                }
                                                                intent.putExtra("ConnectedUser",connectedUser);
                                                                startActivity(intent);
                                                                break;
                                                            }
                                                        } else {
                                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });
                                    } else {

                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    }

                                }

                            });

                }

        });
    }
}
