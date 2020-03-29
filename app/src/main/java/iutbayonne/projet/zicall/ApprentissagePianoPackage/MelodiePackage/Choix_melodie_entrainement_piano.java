package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Chanson_entrainement_guitare;
import iutbayonne.projet.zicall.ApprentissagePiano;
import iutbayonne.projet.zicall.MainActivity;
import iutbayonne.projet.zicall.R;

public class Choix_melodie_entrainement_piano extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_melodie_entrainement_piano);
    }

    public void accederMelodie(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), ApprentissagePiano.class);
        startActivity(otherActivity);
    }

    public void accederAccueil(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(otherActivity);
        finish();
    }
}
