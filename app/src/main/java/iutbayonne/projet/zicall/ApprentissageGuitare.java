package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Choix_chanson_entrainement_guitare;
import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Recapitulatif_des_accords;

public class ApprentissageGuitare extends AppCompatActivity
{
    /**
     * Bouton qui renvoie vers la fonctionnalité correspondante.
     */
    private CardView cv_accord, cv_chanson;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentissage_guitare);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All</font>"));

        cv_accord = findViewById(R.id.accordcardview);
        cv_chanson = findViewById(R.id.chansoncardview);

        cv_accord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederAuRecapitulatifDesAccords(v);
            }
        });

        cv_chanson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proposerDesChansonsEntrainement(v);
            }
        });
    }

    public void accederAuRecapitulatifDesAccords(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Recapitulatif_des_accords.class);
        startActivity(otherActivity);
    }

    public void proposerDesChansonsEntrainement(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_chanson_entrainement_guitare.class);
        startActivity(otherActivity);
    }

    public void accederAccueil()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(otherActivity);
        finish();
    }

    /**
     * Récupère l'interface correspondante à la toolbar désirée et l'affiche.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_touteslesactivites, menu);
        return true;
    }

    /**
     * Associe chaque bouton de la toolbar à une action.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                accederAccueil();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
