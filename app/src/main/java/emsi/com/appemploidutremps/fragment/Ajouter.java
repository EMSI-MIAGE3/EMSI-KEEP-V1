package emsi.com.appemploidutremps.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import emsi.com.appemploidutremps.AdminCP;
import emsi.com.appemploidutremps.CalendarTimeTible;
import emsi.com.appemploidutremps.R;
import emsi.com.appemploidutremps.dao.ClasseDAO;
import emsi.com.appemploidutremps.dao.FiliereDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.Filiere;
import emsi.com.appemploidutremps.models.User;

public class Ajouter extends Fragment {


    private Spinner spinnerAnnee;
    private Spinner spinnerFiliere;
    private Spinner spinnerClasse;
    private Button consulterButton;
    private List<Classe> classes=new ArrayList<>();
    private List<Filiere> filieres=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ajouter,container,false);
    }


    @Override
    public void onResume() {
        super.onResume();

        spinnerAnnee = (Spinner) getView().findViewById(R.id.annee2);
        spinnerClasse = (Spinner) getView().findViewById(R.id.classe2);
        spinnerFiliere = (Spinner) getView().findViewById(R.id.filiere2);
        consulterButton = (Button) getView().findViewById(R.id.consulter_button2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.annee_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnnee.setAdapter(adapter);

        spinnerAnnee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filieres.clear();
                FiliereDAO.getInstance().getFiliereDAO()
                        .whereEqualTo("annee", Integer.parseInt(spinnerAnnee.getSelectedItem().toString()))
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            filieres.add(document.toObject(Filiere.class));

                        }

                        ArrayAdapter<Filiere> dataAdapter = new ArrayAdapter<Filiere>(getContext(),
                                android.R.layout.simple_spinner_item, filieres);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerFiliere.setAdapter(dataAdapter);
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                spinnerClasse.setEnabled(false);
            }
        });


        spinnerFiliere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Filiere selectedFiliere = (Filiere) spinnerFiliere.getSelectedItem();
                classes.clear();
                ClasseDAO.getInstance().getClasseDAO()
                        .whereEqualTo("filiere.annee", selectedFiliere.getAnnee())
                        .whereEqualTo("filiere.nom", selectedFiliere.getNom())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            classes.add(document.toObject(Classe.class));

                        }

                        ArrayAdapter<Classe> dataAdapter = new ArrayAdapter<Classe>(getContext(),
                                android.R.layout.simple_spinner_item, classes);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerClasse.setAdapter(dataAdapter);


                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        consulterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Classe selectedClasse = (Classe) spinnerClasse.getSelectedItem();
                intent.putExtra("selectedClasse", selectedClasse);
                intent.putExtra("ConnectedUser", (User) getActivity().getIntent().getSerializableExtra("ConnectedUser"));
                //Log.w("GetIntentFragmentUser",(User) getActivity().getIntent().getSerializableExtra("ConnectedUser")+"");
                //Log.w("GetIntentFragmentClasse",selectedClasse+"");
                AdminCP.setClasseToPass(selectedClasse);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AjouterSeance()).commit();

            }
        });


    }
}
