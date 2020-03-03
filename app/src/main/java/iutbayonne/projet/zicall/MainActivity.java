package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.Choix_melodie_entrainement_piano;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void accederApprentissageGuitare(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), ApprentissageGuitare.class);
        startActivity(otherActivity);
    }

    public void accederAccordeurGuitare(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), AccordeurGuitare.class);
        startActivity(otherActivity);
    }

    public void accederEcriturePartition(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), EcriturePartition.class);
        startActivity(otherActivity);
    }

    public void accederChoixMelodieEntrainementPiano(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_melodie_entrainement_piano.class);
        startActivity(otherActivity);
    }
}