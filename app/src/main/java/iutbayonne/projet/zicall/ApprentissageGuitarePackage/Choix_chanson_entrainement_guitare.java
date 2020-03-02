package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Chanson_entrainement_guitare;
import iutbayonne.projet.zicall.R;

public class Choix_chanson_entrainement_guitare extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_chanson_entrainement_guitare);
    }

    public void accederChanson(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Chanson_entrainement_guitare.class);
        startActivity(otherActivity);
    }
}
