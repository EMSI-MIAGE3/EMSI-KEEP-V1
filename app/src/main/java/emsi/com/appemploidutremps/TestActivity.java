package emsi.com.appemploidutremps;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import emsi.com.appemploidutremps.dao.ClasseDAO;
import emsi.com.appemploidutremps.dao.SeanceDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Seance;
import emsi.com.appemploidutremps.models.User;


public class TestActivity extends AppCompatActivity {

    private User user;
    private Calendar calendar;
    private Classe classe;
    static ArrayList<Seance> seances = new ArrayList<>();


    static Classe cl = new Classe();
    static String idClass = "";

    TextView entrance;
    TextView exit;
    TextView cours;
    TextView clssRoom;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        user = (User) getIntent().getSerializableExtra("UserToTimetable");
        calendar = (Calendar) getIntent().getSerializableExtra("ChosenDate");
        classe=(Classe) getIntent().getSerializableExtra("ClasseToTimtable");

        seances.add(new Seance());

        cours =(TextView) findViewById(R.id.course);


        ClasseDAO.getInstance().getClasseDAO().whereArrayContains("etudiants", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    idClass = document.getId();
                    cl = (Classe) document.toObject(Classe.class);
                    Log.w("ClaaaaaaaaseWsslat","cl*******"+cl);
                    //Log.w("TimeTable", cl.getNom()+"");
                }

                SeanceDAO.getInstance().getSeanceDAO().whereEqualTo("classeId", idClass)
                        .whereArrayContains("groupe", user.getGroupe())
                        .whereEqualTo("jour.jour", calendar.getTime().getDay())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        seances.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Seance sc = document.toObject(Seance.class);
                            if (sc.getJour().getDate().getDate() == calendar.getTime().getDate()
                                    && sc.getJour().getDate().getMonth() == calendar.getTime().getMonth()) {
                                seances.add(sc);
                                Log.w("ClaaaaaaaaseWsslat","*******"+seances.size());
                            }
                        }
                    }
                });
            }
        });




        ListView lv=(ListView) findViewById(R.id.TestList);
        ViewAdapter viewAdapter=new ViewAdapter();
        lv.setAdapter(viewAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    class ViewAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return seances.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView= getLayoutInflater().inflate(R.layout.session,null);
            //cours.setText(seances.get(position).getMatiere());
            return convertView;
        }
    }
}
