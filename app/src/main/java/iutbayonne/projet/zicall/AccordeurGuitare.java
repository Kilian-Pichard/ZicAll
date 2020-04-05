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
import android.widget.TextView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import iutbayonne.projet.zicall.AccordeurGuitarePackage.Corde;

public class AccordeurGuitare extends AppCompatActivity
{
    private float frequenceDetectee;

    /**
     * Écart entre la fréquence détectée et la fréquence de référence de la corde sélectionnée.
     */
    private float margeFrequence;

    /**
     * Se charge de récupérer la fréquence provenant du microphone.
     */
    private AudioDispatcher dispatcher;

    /**
     * Se charge de lancer le dispatcher en continu tant .
     */
    private Thread audioThread;

    private TextView frequenceMesuree;
    private TextView frequenceReference;

    /**
     * Indique si l'utilisateur doit tendre sa corde.
     */
    private TextView erreurGauche;

    /**
     * Indique si l'utilisateur doit détendre sa corde.
     */
    private TextView erreurDroite;

    /**
     * Objet permettant de manipuler la corde correspondante.
     */
    private Corde cordeMi, cordeLa, cordeRe, cordeSol, cordeSi, cordeMiAigu, cordeSelectionnee;

    /**
     * Bouton permettant de sélectionner la corde correspondante.
     */
    private CardView mi, la, re, sol, si, mi2;

    /**
     * Thread qui se charge de l'affichage en temps réel des indications.
     */
    private AffichageFrequence affichage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordeur_guitare);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All</font>"));

        // Association des variables à leurs ressources graphiques
        this.mi = findViewById(R.id.mi);
        this.la = findViewById(R.id.la);
        this.re = findViewById(R.id.re);
        this.sol = findViewById(R.id.sol);
        this.si = findViewById(R.id.si);
        this.mi2 = findViewById(R.id.mi2);
        this.erreurGauche = findViewById(R.id.erreurGauche);
        this.erreurDroite = findViewById(R.id.erreurDroite);
        this.frequenceMesuree = findViewById(R.id.frequenceMesure);
        this.frequenceReference = findViewById(R.id.frequenceReference);

        this.cordeMi = Corde.MI;
        this.cordeLa = Corde.LA;
        this.cordeRe = Corde.RE;
        this.cordeSol = Corde.SOL;
        this.cordeSi = Corde.SI;
        this.cordeMiAigu = Corde.MI_AIGU;

        this.affichage = null;

        mi.setOnClickListener(new SelectionCorde());
        la.setOnClickListener(new SelectionCorde());
        re.setOnClickListener(new SelectionCorde());
        sol.setOnClickListener(new SelectionCorde());
        si.setOnClickListener(new SelectionCorde());
        mi2.setOnClickListener(new SelectionCorde());

        frequenceMesuree.setText("En attente");

        // Relie l'AudioDispatcher à l'entrée par défaut du smartphone (micro)
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        // Création d'un gestionnaire de détection de fréquence
        PitchDetectionHandler pdh = new PitchDetectionHandler()
        {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e)
            {
                /* Récupère le fréquence fondamentale du son capté par le micro en Hz.
                   Renvoie -1 si aucun son n'est capté. */
                frequenceDetectee = res.getPitch();
            }
        };

        /* Ajout du gestionnaire de détection au dispatcher.
           La détection se fera en suivant l'agorithme de Yin */
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);

        // Création et lancement du thread pour récupérer la fréquence
        audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    /**
     * Gestionnaire d'évènements qui se charge de mettre à jour l'affichage lorsque l'on sélectionne une autre corde
     */
    private class SelectionCorde implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.mi:
                    cordeSelectionnee = cordeMi;
                    break;

                case R.id.la:
                    cordeSelectionnee = cordeLa;
                    break;

                case R.id.re:
                    cordeSelectionnee = cordeRe;
                    break;

                case R.id.sol:
                    cordeSelectionnee = cordeSol;
                    break;

                case R.id.si:
                    cordeSelectionnee = cordeSi;
                    break;

                case R.id.mi2:
                    cordeSelectionnee = cordeMiAigu;
                    break;
            }

            frequenceReference.setText(String.valueOf(cordeSelectionnee.getFrequenceReferenceCorde()));
            arreterAffichage();
            affichage = new AffichageFrequence();
        }
    }

    /**
     * Thread qui se charge d'afficher en continu des indications
     */
    private class AffichageFrequence extends Thread
    {
        /**
         * Indique si le Thread doit s'arreter ou non
         */
        boolean doitMourrir;

        public AffichageFrequence()
        {
            this.doitMourrir = false;
            start();
        }

        public void run()
        {
            while(!doitMourrir)
            {
                runOnUiThread(new Indication());

                try
                {
                    Thread.sleep((long) 500);
                }
                catch (InterruptedException ie)
                {}
            }
        }

        public void arreter()
        {
            this.doitMourrir = true;
            this.interrupt();
        }
    }

    /**
     * Affiche à l'écran la fréquence détectée par le microphone, ainsi qu'une indication pour
     * dire à l'utilisateur de tendre ou détendre la corde de sa guitare.
     */
    private class Indication implements Runnable
    {
        public Indication()
        {

        }

        public void run()
        {
            // Si une fréquence est mesurée
            if(frequenceDetectee != -1)
            {
                if(cordeSelectionnee.estDansIntervaleFrequenceAcceptable(frequenceDetectee))
                {
                    frequenceMesuree.setTextColor(getResources().getColor(R.color.vert));
                }
                else
                {
                    frequenceMesuree.setTextColor(getResources().getColor(R.color.rouge));
                }

                frequenceMesuree.setText(String.valueOf((float) (Math.round(frequenceDetectee * 100.0)/100.0)));
                margeFrequence = (float)cordeSelectionnee.getFrequenceReferenceCorde() - frequenceDetectee;

                if(margeFrequence > 0)
                {
                    erreurGauche.setText("Tendre\nla corde");
                    erreurDroite.setText("");
                }
                if(margeFrequence < 0)
                {
                    erreurGauche.setText("");
                    erreurDroite.setText("Détendre\nla corde");
                }
            }
        }
    }

    public void arreterAffichage()
    {
        if(affichage != null)
        {
            affichage.arreter();
        }
    }

    public void arreterEcoute()
    {
        audioThread.interrupt();
        if (!dispatcher.isStopped()) {
            dispatcher.stop();
        }
    }

    public void accederAccueil()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        arreterAffichage();
        arreterEcoute();

        startActivity(otherActivity);
        finish();
    }

    /**
     * Méthode qui s'exécute lorsque l'on appuye sur le bouton "Retour" du smartphone.
     * Surchargée afin d'arrêter les proprement les Thread de l'activity en cours avant de la quitter.
     */
    @Override
    public void onBackPressed()
    {
        arreterAffichage();
        arreterEcoute();
        super.onBackPressed();
    }

    /**
     * Récupère l'interface correspondante à la toolbar désirée et l'affiche.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_touteslesactivites, menu);
        return true;
    }

    /**
     * Associe chaque bouton de la toolbar à une action.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_home:
                accederAccueil();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}