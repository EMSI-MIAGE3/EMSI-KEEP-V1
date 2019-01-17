package emsi.com.appemploidutremps;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import emsi.com.appemploidutremps.dao.SeanceDAO;
import emsi.com.appemploidutremps.models.Seance;
import emsi.com.appemploidutremps.models.SeanceDate;
import emsi.com.appemploidutremps.models.SeanceTime;

public class modifierSeeance extends AppCompatActivity {

    private EditText matiere,notes;
    private EditText heureDeb, minDeb, heureFin, minFin;
    private EditText salle, professeur;
    private Spinner type, groupe;
    private Button valider,dateModif;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    Calendar setDate =Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_seeance);

        matiere = findViewById(R.id.MatiereModif);
        notes = findViewById(R.id.noteModif);
        heureDeb = findViewById(R.id.HeureDebModif);
        minDeb = findViewById(R.id.MinDebModif);
        heureFin = findViewById(R.id.HeureFinModif);
        minFin = findViewById(R.id.MinFinModif);
        salle = findViewById(R.id.SalleModif);
        type = findViewById(R.id.SpinnerModif);
        dateModif=findViewById(R.id.DatModif);
        groupe= findViewById(R.id.ModifGroupe);
        valider=findViewById(R.id.validerUpdate);

        final Seance seance = (Seance) getIntent().getSerializableExtra("SelectedSeance");

        matiere.setText(seance.getMatiere());
        notes.setText(seance.getNote());
        heureDeb.setText(seance.getDateDebut().getHeure()+"");
        minDeb.setText(seance.getDateDebut().getMinute()+"");
        heureFin.setText(seance.getDateFin().getHeure()+"");
        minFin.setText(seance.getDateFin().getMinute()+"");
        salle.setText(seance.getSalle());

       // professeur.setText(seance.getProfesseur().getNom());


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.groupe_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupe.setAdapter(adapter);


        if(seance.getGroupe().size()>1) groupe.setSelection(2);
        else groupe.setSelection(seance.getGroupe().get(0)-1);


        switch (seance.getType()) {
            case "SEANCE": type.setSelection(0);break;
            case "CONTROLE": type.setSelection(1);break;
            case "RATTRAPAGE": type.setSelection(2);break;
        }


        Calendar calendar=Calendar.getInstance();
        calendar.setTime(seance.getJour().getDate());
        final int year= calendar.get(Calendar.YEAR);
        final int month= calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);

        dateModif.setText(year+"/"+(month+1)+"/"+day);

        setDate=calendar;

        dateModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DatePickerDialog dialog=new DatePickerDialog(modifierSeeance.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateModif.setText(year+"/"+month+"/"+dayOfMonth);
                setDate.set(year,month,dayOfMonth);
            }
        };


        Log.w("GetSeanceModifier", "Before=====>" + seance.toString());



        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                seance.setMatiere(matiere.getText().toString());
                seance.setNote(notes.getText().toString());
                seance.setJour(new SeanceDate(setDate.getTime().getDay(),setDate.getTime()));
                seance.setDateDebut(new SeanceTime(Integer.parseInt(heureDeb.getText().toString()),Integer.parseInt(minDeb.getText().toString())));
                seance.setDateFin(new SeanceTime(Integer.parseInt(heureFin.getText().toString()),Integer.parseInt(minFin.getText().toString())));
                seance.setType(type.getSelectedItem().toString());
                List<Integer> groupeList =new ArrayList<>();
                if ("1-2".equals(groupe.getSelectedItem())){
                    groupeList.add(1); groupeList.add(2);
                }
                else groupeList.add(Integer.parseInt(groupe.getSelectedItem().toString()));
                seance.setGroupe(groupeList);
                seance.setSalle(salle.getText().toString());
                SeanceDAO.getInstance().getSeanceDAO().document(seance.getId()).set(seance);
                Log.w("GetSeanceModifier", "After =====>" + seance.toString());

                Toast toast = Toast.makeText(getApplicationContext(),
                        "La séance est modifiée",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });



    }
}
