package emsi.com.appemploidutremps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import emsi.com.appemploidutremps.dao.ClasseDAO;
import emsi.com.appemploidutremps.dao.SeanceDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Jour;
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
    static ArrayList<Seance> seances = new ArrayList<>();


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
    TextView clssRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_table);

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


        user = (User) getIntent().getSerializableExtra("UserToTimetible");
        calendar = (Calendar) getIntent().getSerializableExtra("ChosenDate");


        ClasseDAO.getInstance().getClasseDAO().whereArrayContains("etudiants", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    idClass = document.getId();
                    cl = (Classe) document.toObject(Classe.class);
                    //Log.w("TimeTable", cl.getNom()+"");
                }
            }
        });
        //Log.w("TimeTableAfter", idClass+"");

        SeanceDAO.getInstance().getSeanceDAO().whereEqualTo("classeId", idClass)
                .whereEqualTo("groupe", user.getGroupe())
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


                Log.w("TimeTableSeanca", "====" + seances.size());
                switch (seances.size()) {
                    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    case 2: {
                        entrance = (TextView) findViewById(R.id.entrance2);
                        exit = (TextView) findViewById(R.id.exit2);
                        cours = (TextView) findViewById(R.id.course2);
                        clssRoom = (TextView) findViewById(R.id.classRoom2);

                        entrance.setText(seances.get(1).getDateDebut().getHeure() + "h " + seances.get(1).getDateDebut().getMinute());
                        exit.setText(seances.get(1).getDateFin().getHeure() + "h " + seances.get(1).getDateFin().getMinute());
                        cours.setText(seances.get(1).getMatiere());
                        clssRoom.setText(seances.get(1).getGroupe() + "");
                    }
                    case 1: {
                        entrance = (TextView) findViewById(R.id.entrance1);
                        exit = (TextView) findViewById(R.id.exit1);
                        cours = (TextView) findViewById(R.id.course1);
                        clssRoom = (TextView) findViewById(R.id.classRoom1);
                        entrance.setText(seances.get(0).getDateDebut().getHeure() + "h " + seances.get(0).getDateDebut().getMinute());
                        exit.setText(seances.get(0).getDateFin().getHeure() + "h " + seances.get(0).getDateFin().getMinute());
                        cours.setText(seances.get(0).getMatiere());
                        clssRoom.setText(seances.get(0).getGroupe() + "");
                    }
                }

            }
        });


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
