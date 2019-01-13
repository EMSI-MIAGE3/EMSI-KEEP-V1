package emsi.com.appemploidutremps;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import emsi.com.appemploidutremps.dao.ClasseDAO;
import emsi.com.appemploidutremps.dao.FiliereDAO;
import emsi.com.appemploidutremps.dao.SeanceDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Filiere;
import emsi.com.appemploidutremps.models.Jour;
import emsi.com.appemploidutremps.models.Professeur;
import emsi.com.appemploidutremps.models.Seance;
import emsi.com.appemploidutremps.models.SeanceDate;
import emsi.com.appemploidutremps.models.SeanceTime;
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
                Filiere filiere=new Filiere();
                filiere.setNom("MIAGE");
                filiere.setAnnee(5);
                FiliereDAO.getInstance().addFiliere(filiere);
                filiere.setNom("IIR");
                filiere.setAnnee(3);
                FiliereDAO.getInstance().addFiliere(filiere);
            }
        });
    }
}
