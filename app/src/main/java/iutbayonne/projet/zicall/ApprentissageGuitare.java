package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Choix_chanson_entrainement_guitare;
import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Recapitulatif_des_accords;

public class ApprentissageGuitare extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentissage_guitare);
    }

    public void accederAuRecapitulatifDesAccords(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Recapitulatif_des_accords.class);
        startActivity(otherActivity);
    }

    public void proposerDesChansonsEntrainement(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_chanson_entrainement_guitare.class);
        startActivity(otherActivity);

    }
}
