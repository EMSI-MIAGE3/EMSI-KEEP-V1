package emsi.com.appemploidutremps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import emsi.com.appemploidutremps.dao.ClasseDAO;
import emsi.com.appemploidutremps.dao.SeanceDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Seance;
import emsi.com.appemploidutremps.models.User;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TimeTable extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;
    private User user;
    private Calendar calendar;
    private Classe classe;
    static ArrayList<Seance> seances = new ArrayList<>();
    static Map<Seance, Classe> seanceClasses = new HashMap<>();
    Seance sc;


    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    static Classe cl = new Classe();
    static String idClass = "";


    TextView entrance;
    TextView exit;
    TextView cours;
    TextView clssRoom, supprimer1, ajouterNote1,
            supprimer2, ajouterNote2,
            supprimer3, ajouterNote3,
            supprimer4, ajouterNote4,
            dateOfDay;
    SurfaceView surfaceView;
    Dialog noteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_table);

        noteDialog =new Dialog(this);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });


        user = (User) getIntent().getSerializableExtra("UserToTimetable");
        calendar = (Calendar) getIntent().getSerializableExtra("ChosenDate");
        classe = (Classe) getIntent().getSerializableExtra("ClasseToTimtable");

        dateOfDay = findViewById(R.id.dateOfDay);
        dateOfDay.setText(calendar.get(Calendar.DAY_OF_MONTH)+" - "+(calendar.get(Calendar.MONTH)+1)+" - "+calendar.get(Calendar.YEAR));

        if ("Professeur".equals(user.getRole())) {

            SeanceDAO.getInstance().getSeanceDAO().whereEqualTo("professeur.id", user.getId())
                    .whereEqualTo("jour.jour", calendar.getTime().getDay())
                    // .orderBy("dateDebut.heure", Query.Direction.DESCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    seances.clear();
                    seanceClasses.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        sc = document.toObject(Seance.class);
                        if (sc.getJour().getDate().getDate() == calendar.getTime().getDate()
                                && sc.getJour().getDate().getMonth() == calendar.getTime().getMonth()) {
                            seances.add(sc);
                           /* ClasseDAO.getInstance().getClasseDAO()
                                    .whereEqualTo("id",sc.getClasseId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot document : task.getResult()){
                                            Log.w("gtClaaaaaas",document.toObject(Classe.class)+"");
                                            seanceClasses.put(sc,document.toObject(Classe.class));
                                            }
                                        }
                                    });*/
                        }
                    }

                    Collections.sort(seances);


                    Log.w("TimeTableSeanca", "====" + seances.size());
                    switch (seances.size()) {
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        case 4: {
                            entrance = (TextView) findViewById(R.id.entrance4);
                            exit = (TextView) findViewById(R.id.exit4);
                            cours = (TextView) findViewById(R.id.course4);
                            clssRoom = (TextView) findViewById(R.id.classRoom4);

                            surfaceView = (SurfaceView) findViewById(R.id.surface4);
                            surfaceView.setBackground(ContextCompat.getDrawable(TimeTable.this, R.color.common_google_signin_btn_text_light_default));
                            surfaceView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(v,seances.get(3).getNote()+"");
                                }
                            });

                            entrance.setText(seances.get(3).getDateDebut().getHeure() + "h " + seances.get(3).getDateDebut().getMinute());
                            exit.setText(seances.get(3).getDateFin().getHeure() + "h " + seances.get(3).getDateFin().getMinute());
                            cours.setText(seances.get(3).getMatiere());
                            clssRoom.setText(seances.get(3).getSalle() + "");
                        }
                        case 3: {
                            entrance = (TextView) findViewById(R.id.entrance3);
                            exit = (TextView) findViewById(R.id.exit3);
                            cours = (TextView) findViewById(R.id.course3);
                            clssRoom = (TextView) findViewById(R.id.classRoom3);

                            surfaceView = (SurfaceView) findViewById(R.id.surface3);
                            surfaceView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(v,seances.get(2).getNote()+"");
                                }
                            });


                            entrance.setText(seances.get(2).getDateDebut().getHeure() + "h " + seances.get(2).getDateDebut().getMinute());
                            exit.setText(seances.get(2).getDateFin().getHeure() + "h " + seances.get(2).getDateFin().getMinute());
                            cours.setText(seances.get(2).getMatiere());
                            clssRoom.setText(seances.get(2).getSalle() + "");
                        }
                        case 2: {
                            entrance = (TextView) findViewById(R.id.entrance2);
                            exit = (TextView) findViewById(R.id.exit2);
                            cours = (TextView) findViewById(R.id.course2);
                            clssRoom = (TextView) findViewById(R.id.classRoom2);

                            surfaceView = (SurfaceView) findViewById(R.id.surface2);
                            surfaceView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(v,seances.get(1).getNote()+"");
                                }
                            });

                            entrance.setText(seances.get(1).getDateDebut().getHeure() + "h " + seances.get(1).getDateDebut().getMinute());
                            exit.setText(seances.get(1).getDateFin().getHeure() + "h " + seances.get(1).getDateFin().getMinute());
                            cours.setText(seances.get(1).getMatiere());
                            clssRoom.setText(seances.get(1).getSalle() + "");
                        }
                        case 1: {
                            entrance = (TextView) findViewById(R.id.entrance1);
                            exit = (TextView) findViewById(R.id.exit1);
                            cours = (TextView) findViewById(R.id.course1);
                            clssRoom = (TextView) findViewById(R.id.classRoom1);
                            surfaceView = (SurfaceView) findViewById(R.id.surface1);
                            surfaceView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(v,seances.get(0).getNote()+"");
                                }
                            });
                            entrance.setText(seances.get(0).getDateDebut().getHeure() + "h " + seances.get(0).getDateDebut().getMinute());
                            exit.setText(seances.get(0).getDateFin().getHeure() + "h " + seances.get(0).getDateFin().getMinute());
                            cours.setText(seances.get(0).getMatiere());
                            clssRoom.setText(seances.get(0).getSalle() + "");
                        }
                    }

                }
            });
        } else if ("Administrateur".equals(user.getRole())) {
            SeanceDAO.getInstance().getSeanceDAO().whereEqualTo("classeId", classe.getId())
                    .whereEqualTo("jour.jour", calendar.getTime().getDay())
                    // .orderBy("dateDebut.heure", Query.Direction.DESCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    seances.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Seance sc = document.toObject(Seance.class);
                        if (sc.getJour().getDate().getDate() == calendar.getTime().getDate()
                                && sc.getJour().getDate().getMonth() == calendar.getTime().getMonth()) {
                            seances.add(sc);

                        }
                    }

                    Collections.sort(seances);

                    Log.w("TimeTableSeanca", "====" + seances.size());
                    switch (seances.size()) {
                        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        case 4: {
                            entrance = (TextView) findViewById(R.id.entrance4);
                            exit = (TextView) findViewById(R.id.exit4);
                            cours = (TextView) findViewById(R.id.course4);
                            clssRoom = (TextView) findViewById(R.id.classRoom4);
                            supprimer4 = (TextView) findViewById(R.id.supprimer4);
                            supprimer4.setVisibility(View.VISIBLE);
                            supprimer4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SeanceDAO.getInstance().getSeanceDAO()
                                            .document(seances.get(3).getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "La seance est suprimmeé avec avec succès",
                                                            Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Erreur",
                                                            Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            });
                                }
                            });
                            ajouterNote4 = (TextView) findViewById(R.id.modifier4);
                            ajouterNote4.setVisibility(View.VISIBLE);
                            ajouterNote4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(v,seances.get(3).getNote()+"");
                                }
                            });
                            surfaceView = (SurfaceView) findViewById(R.id.surface4);
                            surfaceView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TimeTable.this, modifierSeeance.class);
                                    intent.putExtra("SelectedSeance", seances.get(3));
                                    startActivity(intent);
                                }
                            });


                            entrance.setText(seances.get(3).getDateDebut().getHeure() + "h " + seances.get(3).getDateDebut().getMinute());
                            exit.setText(seances.get(3).getDateFin().getHeure() + "h " + seances.get(3).getDateFin().getMinute());
                            cours.setText(seances.get(3).getMatiere()+" (Mr. "+seances.get(3).getProfesseur()+")");
                            clssRoom.setText(seances.get(3).getSalle() + "");
                        }
                        case 3: {
                            entrance = (TextView) findViewById(R.id.entrance3);
                            exit = (TextView) findViewById(R.id.exit3);
                            cours = (TextView) findViewById(R.id.course3);
                            clssRoom = (TextView) findViewById(R.id.classRoom3);
                            supprimer3 = (TextView) findViewById(R.id.supprimer3);
                            supprimer3.setVisibility(View.VISIBLE);
                            supprimer3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SeanceDAO.getInstance().getSeanceDAO()
                                            .document(seances.get(2).getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "La seance est suprimmeé avec avec succès",
                                                            Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Erreur",
                                                            Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            });
                                }
                            });
                            ajouterNote3 = (TextView) findViewById(R.id.modifier3);
                            ajouterNote3.setVisibility(View.VISIBLE);
                            ajouterNote3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(v,seances.get(2).getNote()+"");
                                }
                            });
                            surfaceView = (SurfaceView) findViewById(R.id.surface3);
                            surfaceView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TimeTable.this, modifierSeeance.class);
                                    intent.putExtra("SelectedSeance", seances.get(2));
                                    startActivity(intent);
                                }
                            });


                            entrance.setText(seances.get(2).getDateDebut().getHeure() + "h " + seances.get(2).getDateDebut().getMinute());
                            exit.setText(seances.get(2).getDateFin().getHeure() + "h " + seances.get(2).getDateFin().getMinute());
                            cours.setText(seances.get(2).getMatiere()+" (Mr. "+seances.get(2).getProfesseur()+")");
                            clssRoom.setText(seances.get(2).getSalle() + "");
                        }
                        case 2: {
                            entrance = (TextView) findViewById(R.id.entrance2);
                            exit = (TextView) findViewById(R.id.exit2);
                            cours = (TextView) findViewById(R.id.course2);
                            clssRoom = (TextView) findViewById(R.id.classRoom2);
                            supprimer2 = (TextView) findViewById(R.id.supprimer2);
                            supprimer2.setVisibility(View.VISIBLE);
                            supprimer2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SeanceDAO.getInstance().getSeanceDAO()
                                            .document(seances.get(1).getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "La seance est suprimmeé avec avec succès",
                                                            Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Erreur",
                                                            Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            });
                                }
                            });
                            ajouterNote2 = (TextView) findViewById(R.id.modifier2);
                            ajouterNote2.setVisibility(View.VISIBLE);
                            ajouterNote2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(v,seances.get(1).getNote()+"");
                                }
                            });
                            surfaceView = (SurfaceView) findViewById(R.id.surface2);
                            surfaceView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TimeTable.this, modifierSeeance.class);
                                    intent.putExtra("SelectedSeance", seances.get(1));
                                    startActivity(intent);
                                }
                            });

                            entrance.setText(seances.get(1).getDateDebut().getHeure() + "h " + seances.get(1).getDateDebut().getMinute());
                            exit.setText(seances.get(1).getDateFin().getHeure() + "h " + seances.get(1).getDateFin().getMinute());
                            cours.setText(seances.get(1).getMatiere()+" (Mr. "+seances.get(1).getProfesseur()+")");
                            clssRoom.setText(seances.get(1).getSalle() + "");
                        }
                        case 1: {

                            entrance = (TextView) findViewById(R.id.entrance1);
                            exit = (TextView) findViewById(R.id.exit1);
                            cours = (TextView) findViewById(R.id.course1);
                            clssRoom = (TextView) findViewById(R.id.classRoom1);
                            supprimer1 = (TextView) findViewById(R.id.supprimer1);
                            supprimer1.setVisibility(View.VISIBLE);
                            supprimer1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SeanceDAO.getInstance().getSeanceDAO()
                                            .document(seances.get(0).getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "La seance est suprimmeé avec avec succès",
                                                            Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Erreur",
                                                            Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            });
                                }
                            });
                            ajouterNote1 = (TextView) findViewById(R.id.modifier1);
                            ajouterNote1.setVisibility(View.VISIBLE);
                            ajouterNote1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(v,seances.get(0).getNote()+"");
                                }
                            });
                            surfaceView = (SurfaceView) findViewById(R.id.surface1);
                            surfaceView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TimeTable.this, modifierSeeance.class);
                                    intent.putExtra("SelectedSeance", seances.get(0));
                                    startActivity(intent);
                                }
                            });

                            entrance.setText(seances.get(0).getDateDebut().getHeure() + "h " + seances.get(0).getDateDebut().getMinute());
                            exit.setText(seances.get(0).getDateFin().getHeure() + "h " + seances.get(0).getDateFin().getMinute());
                            cours.setText(seances.get(0).getMatiere()+" (Mr. "+seances.get(0).getProfesseur()+")");
                            clssRoom.setText(seances.get(0).getSalle() + "");
                        }
                    }
                }
            });
        } else {

            ClasseDAO.getInstance().getClasseDAO().whereArrayContains("etudiants", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        idClass = document.getId();
                        cl = (Classe) document.toObject(Classe.class);
                        //Log.w("TimeTable", cl.getNom()+"");
                    }

                    //Log.w("TimeTableAfter", idClass+"");


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

                                }
                            }

                            Collections.sort(seances);

                            Log.w("TimeTableSeanca", "====" + seances.size());
                            switch (seances.size()) {
                                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                case 4: {
                                    entrance = (TextView) findViewById(R.id.entrance4);
                                    exit = (TextView) findViewById(R.id.exit4);
                                    cours = (TextView) findViewById(R.id.course4);
                                    clssRoom = (TextView) findViewById(R.id.classRoom4);
                                    surfaceView = (SurfaceView) findViewById(R.id.surface4);
                                    surfaceView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showDialog(v,seances.get(3).getNote()+"");
                                        }
                                    });
                                    entrance.setText(seances.get(3).getDateDebut().getHeure() + "h " + seances.get(3).getDateDebut().getMinute());
                                    exit.setText(seances.get(3).getDateFin().getHeure() + "h " + seances.get(3).getDateFin().getMinute());
                                    cours.setText(seances.get(3).getMatiere());
                                    clssRoom.setText(seances.get(3).getSalle() + "");
                                }
                                case 3: {
                                    entrance = (TextView) findViewById(R.id.entrance3);
                                    exit = (TextView) findViewById(R.id.exit3);
                                    cours = (TextView) findViewById(R.id.course3);
                                    clssRoom = (TextView) findViewById(R.id.classRoom3);
                                    surfaceView = (SurfaceView) findViewById(R.id.surface3);
                                    surfaceView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showDialog(v,seances.get(2).getNote()+"");
                                        }
                                    });
                                    entrance.setText(seances.get(2).getDateDebut().getHeure() + "h " + seances.get(2).getDateDebut().getMinute());
                                    exit.setText(seances.get(2).getDateFin().getHeure() + "h " + seances.get(2).getDateFin().getMinute());
                                    cours.setText(seances.get(2).getMatiere());
                                    clssRoom.setText(seances.get(2).getSalle() + "");
                                }
                                case 2: {
                                    entrance = (TextView) findViewById(R.id.entrance2);
                                    exit = (TextView) findViewById(R.id.exit2);
                                    cours = (TextView) findViewById(R.id.course2);
                                    clssRoom = (TextView) findViewById(R.id.classRoom2);
                                    surfaceView = (SurfaceView) findViewById(R.id.surface2);
                                    surfaceView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showDialog(v,seances.get(1).getNote()+"");
                                        }
                                    });
                                    entrance.setText(seances.get(1).getDateDebut().getHeure() + "h " + seances.get(1).getDateDebut().getMinute());
                                    exit.setText(seances.get(1).getDateFin().getHeure() + "h " + seances.get(1).getDateFin().getMinute());
                                    cours.setText(seances.get(1).getMatiere());
                                    clssRoom.setText(seances.get(1).getSalle() + "");
                                }
                                case 1: {
                                    entrance = (TextView) findViewById(R.id.entrance1);
                                    exit = (TextView) findViewById(R.id.exit1);
                                    cours = (TextView) findViewById(R.id.course1);
                                    clssRoom = (TextView) findViewById(R.id.classRoom1);
                                    surfaceView = (SurfaceView) findViewById(R.id.surface1);
                                    surfaceView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showDialog(v,seances.get(0).getNote()+"");
                                        }
                                    });
                                    entrance.setText(seances.get(0).getDateDebut().getHeure() + "h " + seances.get(0).getDateDebut().getMinute());
                                    exit.setText(seances.get(0).getDateFin().getHeure() + "h " + seances.get(0).getDateFin().getMinute());
                                    cours.setText(seances.get(0).getMatiere());
                                    clssRoom.setText(seances.get(0).getSalle() + "");
                                }
                            }

                        }
                    });

                }
            });

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    public void showDialog(View v,String noteText){
        TextView note;
        Button close;

        noteDialog.setContentView(R.layout.poupnote);
        close=noteDialog.findViewById(R.id.closePopUpButton);
        note=noteDialog.findViewById(R.id.notepopupview);
        note.setText(noteText);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteDialog.dismiss();
            }
        });

        noteDialog.show();
    }

    class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 3;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.session, null);


            //Log.w("TimeTable","=====> User "+user.getId()+"");
            //Log.w("TimeTable",calendar.getTime()+"");


            //Timestamp tm=new Timestamp(calendar.getTime());
            Log.w("positionListView", seances + "");

            return convertView;
        }
    }
}
