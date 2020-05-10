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
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import iutbayonne.projet.zicall.AccordeurPackage.CordeUkulele;

public class AccordeurUkulele extends AppCompatActivity
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
    private CordeUkulele cordeSol, cordeUt, cordeMi, cordeLa, cordeSelectionnee;
    /**
     * Bouton permettant de sélectionner la corde correspondante.
     */
    private CardView sol, ut, mi, la, cordePrecedente;
    /**
     * Texte des boutons des différentes cordes.
     */
    private TextView texteSol, texteUt, texteMi, texteLa;
    /**
     * Thread qui se charge de l'affichage en temps réel des indications.
     */
    private AffichageFrequence affichage;
    /**
     * Case à cocher qui permet de mettre l'accordeur de ukulele en automatique
     */
    private CheckBox auto;
    /**
     * Map qui va servir a associer les vues aux différentes cordes.
     */
    Map<CardView, CordeUkulele> mapVueCorde = new HashMap<>();
    /**
     * Map qui va servir a associer les textes des vues aux différentes cordes.
     */
    Map<TextView, CordeUkulele> mapTexteVueCorde = new HashMap<>();
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
        setContentView(R.layout.activity_accordeur_ukulele);
        
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All</font>"));
        
        // Association des variables à leurs ressources graphiques
        this.sol = findViewById(R.id.sol);
        this.ut = findViewById(R.id.ut);
        this.mi = findViewById(R.id.mi);
        this.la = findViewById(R.id.la);
        this.auto = findViewById(R.id.checkBox_auto);
        this.erreurGauche = findViewById(R.id.erreurGauche);
        this.erreurDroite = findViewById(R.id.erreurDroite);
        this.frequenceMesuree = findViewById(R.id.frequenceMesure);
        this.frequenceReference = findViewById(R.id.frequenceReference);
        
        this.texteSol = findViewById(R.id.texteSol);
        this.texteUt = findViewById(R.id.texteUt);
        this.texteMi = findViewById(R.id.texteMi);
        this.texteLa = findViewById(R.id.texteLa);
        
        this.cordeSol = CordeUkulele.SOL;
        this.cordeUt = CordeUkulele.UT;
        this.cordeMi = CordeUkulele.MI;
        this.cordeLa = CordeUkulele.LA;
        
        this.affichage = null;

        sol.setOnClickListener(new SelectionCorde());
        ut.setOnClickListener(new SelectionCorde());
        mi.setOnClickListener(new SelectionCorde());
        la.setOnClickListener(new SelectionCorde());
        auto.setOnClickListener(new SelectionBoutonAuto());

        mapVueCorde.put(sol,cordeSol);
        mapVueCorde.put(ut,cordeUt);
        mapVueCorde.put(mi,cordeMi);
        mapVueCorde.put(la,cordeLa);

        mapTexteVueCorde.put(texteSol,cordeSol);
        mapTexteVueCorde.put(texteUt,cordeUt);
        mapTexteVueCorde.put(texteMi,cordeMi);
        mapTexteVueCorde.put(texteLa,cordeLa);
        
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
        sol.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
        ut.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
        mi.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
        la.setCardBackgroundColor(Color.parseColor("#FEFFFF"));
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
            for (Map.Entry<CardView, CordeUkulele> entry : mapVueCorde.entrySet())
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
                    for (Map.Entry<CardView, CordeUkulele> entry : mapVueCorde.entrySet())
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
            if(!((CheckBox)view).isChecked())
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
     * dire à l'utilisateur de tendre ou détendre la corde de son ukulélé.
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
                allBackgroundWhite();
                for(Map.Entry<CardView, CordeUkulele> entry : mapVueCorde.entrySet())
                {
                    if(entry.getValue().estDansLIntervalleDeFrequence(frequenceDetectee))
                    {
                        entry.getKey().setBackgroundColor(Color.parseColor("#20A0D3"));
                        cordeSelectionnee = entry.getValue();
                        frequenceReference.setText(String.valueOf(cordeSelectionnee.getFrequenceReferenceCorde()));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();
                    }
                }
                if(cordeSelectionnee.estDansIntervaleFrequenceAcceptable(frequenceDetectee))
                {
                    frequenceMesuree.setTextColor(getResources().getColor(R.color.vert));
                    //Changer couleur texte si corde bien accordée -> mettre en vert
                    for (Map.Entry<TextView, CordeUkulele> entry : mapTexteVueCorde.entrySet()) {
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
                    erreurGauche.setText("Tendre\nla corde");
                    erreurDroite.setText("");
                }
                if(margeFrequence < 0)
                {
                    erreurGauche.setText("");
                    erreurDroite.setText("Détendre\nla corde");
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "La fréquence ne peut pas être lue", Toast.LENGTH_LONG).show();
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

    public void changerPourGuitare()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), AccordeurGuitare.class);
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
                changerPourGuitare();
                return true;

            case R.id.action_ukulele:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
