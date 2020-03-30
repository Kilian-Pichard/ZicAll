package iutbayonne.projet.zicall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.Choix_melodie_entrainement_piano;

public class MainActivity extends AppCompatActivity
{
    // Tag qui sera utilisé pour afficher des messages dans la console pour le débogage
    private static final String LOG_TAG = "AudioRecordTest";

    // Code arbitraire qui sera utilisé lors de la demande de droits et de la réception de la réponse
    private static final int REQUETE_PERMISSION_ENRERISTRER_AUDIO = 1;

    private boolean permissionDEnregistrerAccordee = false;

    // Permissions à demander. Ici, seulement la permission pour enregistrer le son provenant du micro.
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    /* Méthode qui s'exécute lorsque l'on reçoit les résultats de toutes les demandes
    de permission de l'application. Ici, nous ne nous soucions que de la permission pour
    enregistrer le son provenant du micro. Sans cette permission accordée, l'application se ferme. */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode)
        {
            case REQUETE_PERMISSION_ENRERISTRER_AUDIO:
                permissionDEnregistrerAccordee  = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;

            // On devrait rajouter des case si on avait d'autres permissions à demander
        }

        if (!permissionDEnregistrerAccordee)
        {
            // Envoi d'un message d'erreur dans la console puis fermeture de l'appli
            Log.e(LOG_TAG, "Permission d'enregistrer non approuvée");
            finish();
        }
    }

    private CardView CV_piano;
    private CardView CV_guitare;
    private CardView CV_partition;
    private CardView CV_accordeur;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Demande la permission d'enregistrer du son
        ActivityCompat.requestPermissions(this, permissions, REQUETE_PERMISSION_ENRERISTRER_AUDIO);

        CV_guitare = findViewById(R.id.guitarecardview);
        CV_piano = findViewById(R.id.pianocardview);
        CV_accordeur = findViewById(R.id.accordeurcardview);
        CV_partition = findViewById(R.id.partitioncardview);

        CV_guitare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederApprentissageGuitare(v);
            }
        });

        CV_piano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederChoixMelodieEntrainementPiano(v);
            }
        });

        CV_accordeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederAccordeurGuitare(v);
            }
        });

        CV_partition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederEcriturePartition(v);
            }
        });
    }

    public void accederApprentissageGuitare(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), ApprentissageGuitare.class);
        startActivity(otherActivity);
    }

    public void accederAccordeurGuitare(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), AccordeurGuitare.class);
        startActivity(otherActivity);
    }

    public void accederEcriturePartition(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), EcriturePartition.class);
        startActivity(otherActivity);
    }

    public void accederChoixMelodieEntrainementPiano(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_melodie_entrainement_piano.class);
        startActivity(otherActivity);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                //accederAccueil();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}