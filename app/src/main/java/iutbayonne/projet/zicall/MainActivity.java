package iutbayonne.projet.zicall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.Choix_melodie_entrainement_piano;

public class MainActivity extends AppCompatActivity
{
    /**
     * Tag qui sera utilisé pour afficher des messages dans la console pour le débogage.
     */
    private final String LOG_TAG = "AudioRecordTest";

    /**
     * Code arbitraire qui sera utilisé lors de la demande de droits et de la réception de la réponse.
     */
    private static final int REQUETE_PERMISSION_ENRERISTRER_AUDIO = 1;

    private boolean permissionDEnregistrerAccordee = false;

    /**
     * Permissions à demander. Ici, seulement la permission pour enregistrer le son provenant du micro.
     */
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    /**
     * Méthode qui s'exécute lorsque l'on reçoit les résultats de toutes les demandes
     * de permission de l'application. Ici, nous ne nous soucions que de la permission pour
     * enregistrer le son provenant du micro. Sans cette permission accordée, l'application se ferme.
     * @param requestCode Code de la requête dont on a obtenu la réponse.
     * @param grantResults Tableau contenant les résultats des demandes de permissions (-1 si refusée ou 0 si acceptée).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        chargerParam();

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

    /**
     * Bouton qui renvoie vers la fonctionnalité correspondante.
     */
    private CardView cv_piano, cv_guitare, cv_partition, cv_accordeur;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All</font>"));

        // Demande la permission d'enregistrer du son
        ActivityCompat.requestPermissions(this, permissions, REQUETE_PERMISSION_ENRERISTRER_AUDIO);

        cv_guitare = findViewById(R.id.guitarecardview);
        cv_piano = findViewById(R.id.pianocardview);
        cv_accordeur = findViewById(R.id.accordeurcardview);
        cv_partition = findViewById(R.id.partitioncardview);

        cv_guitare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederApprentissageGuitare(v);
            }
        });

        cv_piano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederChoixMelodieEntrainementPiano(v);
            }
        });

        cv_accordeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederAccordeurGuitare(v);
            }
        });

        cv_partition.setOnClickListener(new View.OnClickListener() {
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

    /**
     * Récupère l'interface correspondante à la toolbar désirée et l'affiche.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    /**
     * Associe chaque bouton de la toolbar à une action.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent otherActivity;
                otherActivity = new Intent(getApplicationContext(), Informations.class);
                startActivity(otherActivity);
                return true;

            case R.id.action_lang:
                afficherChangementLangue();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void afficherChangementLangue() {
        final String[] listLangues = {"Français", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle(R.string.choisirLangue);
        mBuilder.setSingleChoiceItems(listLangues, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0 :
                        setLocale("fr");
                        recreate();
                        break;
                    case 1 :
                        setLocale("en");
                        recreate();
                        break;
                    default:
                        setLocale("en");
                        recreate();
                        break;
                }
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String langue) {
        Locale locale = new Locale(langue);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // save
        SharedPreferences.Editor editor = getSharedPreferences("parametres", MODE_PRIVATE).edit();
        editor.putString("Langue", langue);
        editor.apply();
    }

    public void chargerParam() {
        SharedPreferences preferences = getSharedPreferences("parametres", Activity.MODE_PRIVATE);
        String langue = preferences.getString("Langue", "");
        setLocale(langue);
    }
}