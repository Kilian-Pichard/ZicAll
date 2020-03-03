package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Chanson_entrainement_guitare;
import iutbayonne.projet.zicall.ApprentissagePiano;
import iutbayonne.projet.zicall.R;

public class Choix_melodie_entrainement_piano extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_melodie_entrainement_piano);
    }

    public void accederMelodie(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), ApprentissagePiano.class);
        startActivity(otherActivity);
    }
}
