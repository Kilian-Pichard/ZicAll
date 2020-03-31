package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Chanson_entrainement_guitare;
import iutbayonne.projet.zicall.ApprentissagePiano;
import iutbayonne.projet.zicall.MainActivity;
import iutbayonne.projet.zicall.R;

public class Choix_melodie_entrainement_piano extends AppCompatActivity {

    private CardView cv_bellaciao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_melodie_entrainement_piano);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        cv_bellaciao = findViewById(R.id.cv_bellaciaopiano);

        cv_bellaciao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accederMelodie(v);
            }
        });
    }

    public void accederMelodie(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), ApprentissagePiano.class);
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
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_touteslesactivites, menu);
        return true;
    }

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
