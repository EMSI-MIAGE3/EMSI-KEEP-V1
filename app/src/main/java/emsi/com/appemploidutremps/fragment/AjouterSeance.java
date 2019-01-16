package emsi.com.appemploidutremps.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import emsi.com.appemploidutremps.AdminCP;
import emsi.com.appemploidutremps.R;
import emsi.com.appemploidutremps.dao.SeanceDAO;
import emsi.com.appemploidutremps.dao.UserDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Professeur;
import emsi.com.appemploidutremps.models.Seance;
import emsi.com.appemploidutremps.models.SeanceDate;
import emsi.com.appemploidutremps.models.SeanceTime;

public class AjouterSeance extends Fragment {

    private Spinner spinnerType,spinnerGroupe,spinnerProf;


    private Button getDate,ajouterSeance;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private static Calendar seanDate=Calendar.getInstance();

    EditText matiereTxt,heureDeb,minuteDeb,heureFin,minuteFin,salle,nbrSemaine,notes;

    private static List<Professeur> professeurs=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajouter_seance,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();

        matiereTxt =(EditText) getView().findViewById(R.id.seance_matiere);
        heureDeb =(EditText) getView().findViewById(R.id.heure_seance_picker_debut);
        minuteDeb =(EditText) getView().findViewById(R.id.minute_seance_picker_debut);
        heureFin=(EditText) getView().findViewById(R.id.heure_seance_picker_fin);
        minuteFin=(EditText) getView().findViewById(R.id.minute_seance_picker_fin);
        salle=(EditText) getView().findViewById(R.id.seance_salle);
        nbrSemaine=(EditText) getView().findViewById(R.id.nbr_seance) ;
        notes=(EditText) getView().findViewById(R.id.notes_ajout);

        ajouterSeance=(Button) getView().findViewById(R.id.ajouter_seance);
        professeurs.clear();
        UserDAO.getInstance().getUserDAO().whereEqualTo("role","Professeur")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    professeurs=task.getResult().toObjects(Professeur.class);

                ArrayAdapter<Professeur> dataAdapter = new ArrayAdapter<Professeur>(getContext(),
                        android.R.layout.simple_spinner_item, professeurs);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProf.setAdapter(dataAdapter);
            }
        });

        //Spinner group and Type_array
        spinnerGroupe=(Spinner) getView().findViewById(R.id.seance_roupe);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.groupe_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupe.setAdapter(adapter);

        spinnerType=(Spinner) getView().findViewById(R.id.seance_type);
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        spinnerProf=(Spinner) getView().findViewById(R.id.ajouterProf);


        //Date time picker

        getDate=(Button) getView().findViewById(R.id.date_session_picker);
        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar=Calendar.getInstance();
                int year= calendar.get(Calendar.YEAR);
                int month= calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(getActivity(),
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
                Log.w("AjouterSeanceDateBefore",seanDate.getTime()+"");
                getDate.setText(year+"/"+month+1+"/"+dayOfMonth);
                seanDate.set(year,month,dayOfMonth);

            }
        };


        ajouterSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Integer> groupe =new ArrayList<>();
                if ("1-2".equals(spinnerGroupe.getSelectedItem())){
                    groupe.add(1); groupe.add(2);
                }
                else groupe.add(Integer.parseInt(spinnerGroupe.getSelectedItem().toString()));

                Seance seance=new Seance(matiereTxt.getText().toString(),"Description",new SeanceDate(seanDate.getTime().getDay()
                        ,seanDate.getTime()),
                        new SeanceTime(Integer.parseInt(heureDeb.getText().toString()),Integer.parseInt(minuteDeb.getText().toString())),
                        new SeanceTime(Integer.parseInt(heureFin.getText().toString()),Integer.parseInt(minuteFin.getText().toString())),
                        spinnerType.getSelectedItem().toString(),
                        notes.getText().toString(),AdminCP.getClasseToPass().getId(),
                        (Professeur) spinnerProf.getSelectedItem(),groupe,salle.getText().toString());

                seance.setClasse(AdminCP.getClasseToPass());

                Calendar cal=Calendar.getInstance();
                cal.setTime(seance.getJour().getDate());
                for(int i=0;i<Integer.parseInt(nbrSemaine.getText().toString());i++){
                    //Log.w("Seeeeeeeance=====>",seance+"");
                    SeanceDAO.getInstance().addSeance(seance);
                    cal.add(Calendar.DATE,7);
                    seance.setJour(new SeanceDate(cal.getTime().getDay(),cal.getTime()));
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ConsulterClasse()).commit();
            }
        });

        Log.w("AjouterSeanceDate",seanDate.getTime()+"");

        Intent intent=getActivity().getIntent();
        //Classe classe=(Classe) intent.getSerializableExtra("selectedClasse");

        Classe classe=AdminCP.getClasseToPass();
        Log.w("AjouterSeance",classe.getId()+"");


    }
}
