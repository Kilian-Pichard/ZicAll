package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import iutbayonne.projet.zicall.AccordeurGuitarePackage.Corde;

public class AccordeurGuitare extends AppCompatActivity {

    private float frequenceDetectee;
    private Thread audioThread;
    private AudioDispatcher dispatcher;

    private TextView frequenceMesuree;
    private TextView frequenceReference;

    private Corde cordeMi;
    private Corde cordeLa;
    private Corde cordeRe;
    private Corde cordeSol;
    private Corde cordeSi;
    private Corde cordeMiAigu;
    private Corde cordeSelectionne;

    private Button mi;
    private Button la;
    private Button re;
    private Button sol;
    private Button si;
    private Button mi2;
    private Button btnAccueil;

    private AffichageFrequence affichage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordeur_guitare);

        this.mi = findViewById(R.id.mi);
        this.la = findViewById(R.id.la);
        this.re = findViewById(R.id.re);
        this.sol = findViewById(R.id.sol);
        this.si = findViewById(R.id.si);
        this.mi2 = findViewById(R.id.mi2);
        this.btnAccueil = findViewById(R.id.accueil);

        this.frequenceMesuree = findViewById(R.id.frequenceMesure);
        this.frequenceReference = findViewById(R.id.frequenceReference);

        this.cordeMi = Corde.MI;
        this.cordeLa = Corde.LA;
        this.cordeRe = Corde.RE;
        this.cordeSol = Corde.SOL;
        this.cordeSi = Corde.SI;
        this.cordeMiAigu = Corde.MI_AIGU;

        this.cordeSelectionne = cordeMi;

        mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cordeSelectionne = cordeMi;
                frequenceReference.setText(String.valueOf(cordeSelectionne.getFrequenceReferenceCorde()));
                mi.setEnabled(false);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);
            }
        });

        la.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cordeSelectionne = cordeLa;
                frequenceReference.setText(String.valueOf(cordeSelectionne.getFrequenceReferenceCorde()));
                mi.setEnabled(true);
                la.setEnabled(false);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);
            }
        });

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cordeSelectionne = cordeRe;
                frequenceReference.setText(String.valueOf(cordeSelectionne.getFrequenceReferenceCorde()));
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(false);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);
            }
        });

        sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cordeSelectionne = cordeSol;
                frequenceReference.setText(String.valueOf(cordeSelectionne.getFrequenceReferenceCorde()));
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(false);
                si.setEnabled(true);
                mi2.setEnabled(true);
            }
        });

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cordeSelectionne = cordeSi;
                frequenceReference.setText(String.valueOf(cordeSelectionne.getFrequenceReferenceCorde()));
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(false);
                mi2.setEnabled(true);
            }
        });

        mi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cordeSelectionne = cordeMiAigu;
                frequenceReference.setText(String.valueOf(cordeSelectionne.getFrequenceReferenceCorde()));
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(false);
            }
        });

        btnAccueil.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent otherActivity;
                otherActivity = new Intent(getApplicationContext(), MainActivity.class);
                // Vide la pile des activity
                otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                affichage.arreter();
                audioThread.interrupt();
                dispatcher.stop();

                startActivity(otherActivity);
                finish();
            }
        });

        frequenceMesuree.setText("en attente");

        //CREATION DISPATCHER POUR RECUPERER LA FREQUENCE
        // Relie l'AudioDispatcher à l'entrée par défaut du smartphone (micro)
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        // Création d'un gestionnaire de détection de fréquence
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
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

         //CREATION ET LANCEMENT THREAD RECUPERATION FREQUENCE
        audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();

        //LANCEMENT THREAD DE L'AFFICHAGE
        affichage = new AffichageFrequence();

    }

    private class AffichageFrequence extends Thread
    {
        boolean enMarche;

        public AffichageFrequence()
        {
            this.enMarche = true;
            start();
        }

        public void run()
        {
            while(enMarche)
            {
                runOnUiThread(new Indication());

                try
                {
                    Thread.sleep((long) 500);
                }
                catch (InterruptedException ie)
                {

                }
            }
        }

        public void arreter()
        {
            this.enMarche = false;
            this.interrupt();
        }
    }

    private class Indication implements Runnable
    {

        public Indication()
        {

        }

        public void run()
        {
            // si une fréquence est mesurée
            if(frequenceDetectee != -1)
            {
                if(cordeSelectionne.estDansIntervaleFrequenceAcceptable(frequenceDetectee))
                {
                    frequenceMesuree.setTextColor(getResources().getColor(R.color.vert));
                }
                else
                {
                    frequenceMesuree.setTextColor(getResources().getColor(R.color.rouge));
                }

                frequenceMesuree.setText(String.valueOf((float) (Math.round(frequenceDetectee * 100.0)/100.0)));
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        affichage.arreter();
        audioThread.interrupt();
        if(!dispatcher.isStopped())
        {
            dispatcher.stop();
        }
        super.onBackPressed();
    }
}