package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import iutbayonne.projet.zicall.MainActivity;
import iutbayonne.projet.zicall.R;

public class Chanson_entrainement_guitare extends AppCompatActivity
{
    /**
     * Permet à la page d'être scrollé.
     */
    private ScrollView scrollView;

    /**
     * Section où sont affichées les paroles de la chanson.
     */
    private LinearLayout layoutAffichageChanson;

    private Chanson chanson;
    private Button btnStartPauseAudio;

    /**
     * Lecteur qui permet de jouer la chanson.
     */
    private MediaPlayer audioChanson;

    /**
     * Se charge de rendre le scroll automatique pendant que la chanson est jouée.
     */
    private AutoScroll autoScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanson_entrainement_guitare);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All</font>"));

        this.scrollView = findViewById(R.id.scrollViewChanson);
        this.layoutAffichageChanson = findViewById(R.id.layoutAffichageChanson);

        this.chanson = Chanson.BELLA_CIAO;
        this.chanson.afficher(layoutAffichageChanson, chanson, this);
        this.btnStartPauseAudio = findViewById(R.id.playPauseAudio);
        this.audioChanson = MediaPlayer.create(getApplicationContext(), this.chanson.getAudioChanson());
    }

    /**
     * Méthode qui s'exécute lorsque l'on retourne sur la page notamment lorsqu'on appuie sur le bouton retour.
     * Surchargée afin d'arrêter proprement les Thread de l'activity en cours avant de la quitter.
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();

        this.scrollView.scrollTo(0,0);
        this.audioChanson = MediaPlayer.create(getApplicationContext(), this.chanson.getAudioChanson());
    }

    public void lancerAudioChanson(View view)
    {
        if(!audioChanson.isPlaying())
        {
            this.audioChanson.start();
            this.autoScroll = new AutoScroll();
            this.btnStartPauseAudio.setText("Pause");
        }
        else
        {
            this.audioChanson.pause();
            this.autoScroll.arreter();
            this.btnStartPauseAudio.setText("Play");
        }
    }

    public void accederAccueil()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if(audioChanson.isPlaying())
        {
            audioChanson.stop();
            autoScroll.arreter();
        }
        audioChanson.release();
        audioChanson = null;

        startActivity(otherActivity);
        finish();
    }

    public void accederAuRecapitulatifDesAccords()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Recapitulatif_des_accords.class);

        if(this.audioChanson.isPlaying())
        {
            this.audioChanson.stop();
            this.autoScroll.arreter();
            this.btnStartPauseAudio.setText("Play");
        }
        this.audioChanson.release();
        this.audioChanson = null;

        startActivity(otherActivity);
    }

    public void choisirAutreChanson()
    {
        onBackPressed();
    }

    /**
     * Thread qui se charge de rendre le scroll automatique pendant que la chanson est jouée.
     */
    public class AutoScroll extends Thread
    {
        private boolean enMarche;

        public AutoScroll()
        {
            this.enMarche = true;
            start();
        }

        /**
         * S'exécute au lancement du Thread.
         */
        public void run()
        {
            while(enMarche && audioChanson.isPlaying())
            {
                try
                {
                    Thread.sleep(125);
                }
                catch(InterruptedException ie)
                {

                }
                scrollView.smoothScrollBy(0, 2);
            }
        }

        public void arreter()
        {
            this.enMarche = false;
            this.interrupt();
        }
    }

    /**
     * Méthode qui s'exécute lorsque l'on appuye sur le bouton "Retour" du smartphone.
     * Surchargée afin d'arrêter proprement les Thread de l'activity en cours avant de la quitter.
     */
    @Override
    public void onBackPressed()
    {
        if(audioChanson != null)
        {
            if(audioChanson.isPlaying())
            {
                audioChanson.stop();
                autoScroll.arreter();
            }

            audioChanson.release();
            audioChanson = null;
        }

        super.onBackPressed();
    }

    /**
     * Récupère l'interface correspondante à la toolbar désirée et l'affiche.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chanson_entrainement_guitare_activity, menu);
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
            case R.id.action_accords:
                accederAuRecapitulatifDesAccords();
                return true;
            case R.id.action_chansons:
                choisirAutreChanson();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
