package emsi.com.appemploidutremps.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import emsi.com.appemploidutremps.CalendarTimeTible;
import emsi.com.appemploidutremps.R;
import emsi.com.appemploidutremps.dao.ClasseDAO;
import emsi.com.appemploidutremps.dao.UserDAO;
import emsi.com.appemploidutremps.models.Classe;
import emsi.com.appemploidutremps.models.User;

public class ConsulterProf extends Fragment {

    List<JSONObject> jsObj = new ArrayList<>();
    Client client = new Client("UBFMREIX6X", "f284c35dca49a72fc260723781430966");
    Index index = client.getIndex("EmploiDuTemps");


    static ArrayList<User> resultList=new ArrayList<>();
    ListView searchResult;
    EditText searchText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consulter_prof,container,false);
    }


    @Override
    public void onResume() {
        super.onResume();

        searchResult =(ListView) getView().findViewById(R.id.result_search_prof);
        searchText =(EditText) getView().findViewById(R.id.search_prof_text);


        resultList.clear();

        UserDAO.getInstance().getUserDAO()
                .whereEqualTo("role","Professeur")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    resultList.add(document.toObject(User.class));
                    Log.w("ListnerChange",resultList+"");
                }
                ArrayAdapter<User> arrayAdapter=new ArrayAdapter<User>(getContext(),android.R.layout.simple_list_item_1,resultList);
                searchResult.setAdapter(arrayAdapter);
            }
        });


         searchText.addTextChangedListener(new TextWatcher() {

             ArrayList<User> moment=new ArrayList<>();
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 ArrayAdapter<User> arrayAdapter=new ArrayAdapter<User>(getContext(),android.R.layout.simple_list_item_1,resultList);
                 searchResult.setAdapter(arrayAdapter);

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {

                 moment.clear();
                 for(User u:resultList){
                     if (u.getNom()!=null && ((u.getNom()+" "+u.getPrenom()).toLowerCase().contains(s.toString().toLowerCase())))
                         moment.add(u);
                 }
                 ArrayAdapter<User> arrayAdapter=new ArrayAdapter<User>(getContext(),android.R.layout.simple_list_item_1,moment);
                 searchResult.setAdapter(arrayAdapter);
                 }
         });

        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u=(User) parent.getItemAtPosition(position);
                Intent intent=new Intent(getContext(),CalendarTimeTible.class);
                intent.putExtra("ConnectedUser",u);
                startActivity(intent);

            }
        });

    }
}

