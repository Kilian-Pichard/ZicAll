package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.CheckBox;

import java.util.HashMap;
import java.util.Map;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import iutbayonne.projet.zicall.AccordeurPackage.CordeGuitare;

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
    private CordeGuitare cordeMi, cordeLa, cordeRe, cordeSol, cordeSi, cordeMiAigu, cordeSelectionnee;

    /**
     * Bouton permettant de sélectionner la corde correspondante.
     */
    private CardView mi, la, re, sol, si, mi2;

    /**
     * Texte des boutons des différentes cordes.
     */
    private TextView texteMi, texteLa, texteRe, texteSol, texteSi, texteMi2;

    /**
     * Thread qui se charge de l'affichage en temps réel des indications.
     */
    private AffichageFrequence affichage;

    /**
     * Case à cocher qui permet de mettre l'accordeur de guitare en automatique
     */
    private CheckBox auto;

    /**
     * Map qui va servir a associer les vues aux différentes cordes.
     */
    Map<CardView, CordeGuitare> mapVueCorde = new HashMap<>();

    /**
     * Map qui va servir a associer les textes des vues aux différentes cordes.
     */
    Map<TextView, CordeGuitare> mapTexteVueCorde = new HashMap<>();

    /**
     * Permet de savoir quelle est la vue actuellement séléctionnée
     */
    private CardView laVueCourante;

    /**
     * Permet de savoir si on a appuyé sur un bouton d'une corde
     */
    Boolean premiereCordeSelectionnee = false;


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
        this.auto = findViewById(R.id.checkBox_auto);
        this.erreurGauche = findViewById(R.id.erreurGauche);
        this.erreurDroite = findViewById(R.id.erreurDroite);
        this.frequenceMesuree = findViewById(R.id.frequenceMesure);
        this.frequenceReference = findViewById(R.id.frequenceReference);

        this.texteMi = findViewById(R.id.texteMi);
        this.texteLa = findViewById(R.id.texteLa);
        this.texteRe = findViewById(R.id.texteRe);
        this.texteSol = findViewById(R.id.texteSol);
        this.texteSi = findViewById(R.id.texteSi);
        this.texteMi2 = findViewById(R.id.texteMiAigu);

        this.cordeMi = CordeGuitare.MI;
        this.cordeLa = CordeGuitare.LA;
        this.cordeRe = CordeGuitare.RE;
        this.cordeSol = CordeGuitare.SOL;
        this.cordeSi = CordeGuitare.SI;
        this.cordeMiAigu = CordeGuitare.MI_AIGU;

        this.affichage = null;

        mi.setOnClickListener(new SelectionCorde());
        la.setOnClickListener(new SelectionCorde());
        re.setOnClickListener(new SelectionCorde());
        sol.setOnClickListener(new SelectionCorde());
        si.setOnClickListener(new SelectionCorde());
        mi2.setOnClickListener(new SelectionCorde());
        auto.setOnClickListener(new SelectionBoutonAuto());

        mapVueCorde.put(mi,cordeMi);
        mapVueCorde.put(la,cordeLa);
        mapVueCorde.put(re,cordeRe);
        mapVueCorde.put(sol,cordeSol);
        mapVueCorde.put(si,cordeSi);
        mapVueCorde.put(mi2,cordeMiAigu);

        mapTexteVueCorde.put(texteMi,cordeMi);
        mapTexteVueCorde.put(texteLa,cordeLa);
        mapTexteVueCorde.put(texteRe,cordeRe);
        mapTexteVueCorde.put(texteSol,cordeSol);
        mapTexteVueCorde.put(texteSi,cordeSi);
        mapTexteVueCorde.put(texteMi2,cordeMiAigu);

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

        //auto.setChecked(true);
        frequenceReference.setText("...");
    }

    public void allBackgroundWhite(){
        mi.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
        la.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
        re.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
        sol.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
        si.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
        mi2.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
    }


    /**
     * Gestionnaire d'évènements qui se charge de mettre à jour l'affichage lorsque l'on sélectionne une autre corde
     */
    private class SelectionCorde implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            premiereCordeSelectionnee = true;
            auto.setChecked(false);
            view.setBackgroundColor(Color.parseColor("#20A0D3"));
            for (Map.Entry<CardView, CordeGuitare> entry : mapVueCorde.entrySet())
            {
                if (entry.getKey() == view)
                {
                    cordeSelectionnee = entry.getValue();
                    frequenceReference.setText(String.valueOf(cordeSelectionnee.getFrequenceReferenceCorde()));
                }
                else {
                    entry.getKey().setBackgroundColor(Color.parseColor("#FEFFFF"));
                }
            }

            arreterAffichage();
            affichage = new AffichageFrequence();
        }
    }

    /**
     * Gestionnaire d'évènements qui se charge de mettre à jour l'affichage et la corde séléctionnée lorsque l'on appuie sur le bouton Auto
     */
    private class SelectionBoutonAuto implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            if(((CheckBox)view).isChecked())
            {
                if (premiereCordeSelectionnee)
                {
                    for (Map.Entry<CardView, CordeGuitare> entry : mapVueCorde.entrySet())
                    {
                        if(cordeSelectionnee == entry.getValue())
                        {
                            laVueCourante = entry.getKey();
                        }
                    }
                    laVueCourante.setBackgroundColor(Color.parseColor("#FEFFFF"));
                }
                frequenceReference.setText("...");
            }
            else
            {
                allBackgroundWhite();
                mi.setCardBackgroundColor(Color.parseColor("#20A0D3"));
                cordeSelectionnee = cordeMi;
                frequenceReference.setText(String.valueOf(cordeSelectionnee.getFrequenceReferenceCorde()));
            }

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

        /**
         * S'exécute au lancement du Thread.
         */
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

        /**
         * Méthode à exécuter à la création du Runnable.
         */
        public void run()
        {
            // Si une fréquence est mesurée
            if(frequenceDetectee != -1)
            {
                if (auto.isChecked()){
                    allBackgroundWhite();

                    for(Map.Entry<CardView, CordeGuitare> entry : mapVueCorde.entrySet())
                    {
                        if(entry.getValue().estDansLIntervalleDeFrequenceDeCorde(frequenceDetectee))
                        {
                            entry.getKey().setBackgroundColor(Color.parseColor("#20A0D3"));
                            cordeSelectionnee = entry.getValue();
                            frequenceReference.setText(String.valueOf(cordeSelectionnee.getFrequenceReferenceCorde()));
                        }
                        else {
                            entry.getKey().setBackgroundColor(Color.parseColor("#FEFFFF"));
                        }
                    }
                }
                else
                {
                    frequenceReference.setText(String.valueOf(cordeSelectionnee.getFrequenceReferenceCorde()));
                }

                if(cordeSelectionnee.estDansIntervaleFrequenceAcceptable(frequenceDetectee))
                {
                    frequenceMesuree.setTextColor(getResources().getColor(R.color.vert));
                    for (Map.Entry<TextView, CordeGuitare> entry : mapTexteVueCorde.entrySet()) {
                        if(cordeSelectionnee == entry.getValue())
                        {
                            entry.getKey().setTextColor(Color.parseColor("#148235"));
                        }
                    }
                }

                else
                {
                    frequenceMesuree.setTextColor(getResources().getColor(R.color.rouge));
                }

                frequenceMesuree.setText(String.valueOf((float) (Math.round(frequenceDetectee * 100.0)/100.0)));
                margeFrequence = (float)cordeSelectionnee.getFrequenceReferenceCorde() - frequenceDetectee;

                if(margeFrequence > 0)
                {
                    erreurGauche.setText(getString(R.string.tendre_corde));
                    erreurDroite.setText("");
                }
                if(margeFrequence < 0)
                {
                    erreurGauche.setText("");
                    erreurDroite.setText(getString(R.string.detendre_corde));
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

    public void changerPourUkulele()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), AccordeurUkulele.class);
        arreterEcoute();
        startActivity(otherActivity);
    }

    public void changerPourBasse()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), AccordeurBasse.class);
        arreterEcoute();
        startActivity(otherActivity);
    }

    /**
     * Méthode qui s'exécute lorsque l'on appuye sur le bouton "Retour" du smartphone.
     * Surchargée afin d'arrêter proprement les Thread de l'activity en cours avant de la quitter.
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
        inflater.inflate(R.menu.menu_accordeurs, menu);
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

            case R.id.action_guitare:
                return true;

            case R.id.action_ukulele:
                changerPourUkulele();
                return true;

            case R.id.action_basse:
                changerPourBasse();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}