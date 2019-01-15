package emsi.com.appemploidutremps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import emsi.com.appemploidutremps.models.Seance;

public class modifierSeeance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_seeance);

        Seance seance=(Seance) getIntent().getSerializableExtra("SelectedSeance");
        Log.w("GetSeanceModifier","=====>"+seance);

    }
}
