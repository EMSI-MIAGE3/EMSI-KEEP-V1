package emsi.com.appemploidutremps;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import emsi.com.appemploidutremps.dao.ClasseDAO;
import emsi.com.appemploidutremps.dao.SeanceDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Seance;
import emsi.com.appemploidutremps.models.User;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView tv=(TextView) findViewById(R.id.myViewTest);

        Button btn=(Button) findViewById(R.id.buttonTest);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seance seance=new Seance();
                seance.setClasseId("5cb5TUgxKAtHgRhinzSn");
                Calendar calendar=Calendar.getInstance();
                Calendar calendar1=Calendar.getInstance();
                Calendar calendar2=Calendar.getInstance();
                calendar.set(2019,1,15,8,30,0);
                seance.setJour(calendar.getTime());
                calendar1.set(2019,1,15,8,30,0);
                seance.setDateDebut(calendar1.getTime());
                calendar2.set(2019,1,15,10,30,0);
                seance.setDateFin(calendar2.getTime());
                seance.setMatiere("CMMI");

                Log.w("Seance",""+seance);

                SeanceDAO.getInstance().addSeance(seance);
                Log.w("Seance",""+seance);
            }
        });
    }
}
