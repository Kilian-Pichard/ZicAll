package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

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
import iutbayonne.projet.zicall.MainActivity;
import iutbayonne.projet.zicall.R;

public class Choix_chanson_entrainement_guitare extends AppCompatActivity {

    /**
     * Bouton qui renvoie vers la chanson correspondante.
     */
    private CardView cv_bellaciao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_chanson_entrainement_guitare);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All</font>"));

        cv_bellaciao = findViewById(R.id.cv_bellaciaoguitare);

        cv_bellaciao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederChanson(v);
            }
        });
    }

    public void accederChanson(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Chanson_entrainement_guitare.class);
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
     * Méthode qui s'exécute lorsque l'on appuye sur le bouton "Retour" du smartphone.
     * Surchargée afin d'arrêter proprement les Thread de l'activity en cours avant de la quitter.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_touteslesactivites, menu);
        return true;
    }

    /**
     * Récupère l'interface correspondante à la toolbar désirée et l'affiche.
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