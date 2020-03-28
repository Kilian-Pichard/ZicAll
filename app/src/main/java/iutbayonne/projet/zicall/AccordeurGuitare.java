package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class AccordeurGuitare extends AppCompatActivity {

    private TextView frequenceMesuree;
    private TextView frequenceReference;
    private TextView indication;

    private Thread audioThread;
    private AudioDispatcher dispatcher;

    HashMap<String, Double> lesFrequences;

    private Button mi;
    private Button la;
    private Button re;
    private Button sol;
    private Button si;
    private Button mi2;

    private String cordeCourante = "mi";

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

        this.frequenceMesuree = findViewById(R.id.frequenceMesure);
        this.frequenceReference = findViewById(R.id.frequenceReference);
        this.indication = findViewById(R.id.indication);

        lesFrequences = new HashMap<String, Double>();

        lesFrequences.put("mi", 82.4);
        lesFrequences.put("la", 110.0);
        lesFrequences.put("re", 146.8);
        lesFrequences.put("sol", 196.0);
        lesFrequences.put("si", 246.9);
        lesFrequences.put("mi2", 329.6);

        new RecupFrequence();

        mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(false);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);

                cordeCourante = "mi";
                frequenceReference.setText("" + lesFrequences.get(cordeCourante));
            }
        });

        la.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(false);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);

                cordeCourante = "la";
                frequenceReference.setText("" + lesFrequences.get(cordeCourante));
            }
        });

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(false);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(true);

                cordeCourante = "re";
                frequenceReference.setText("" + lesFrequences.get(cordeCourante));
            }
        });

        sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(false);
                si.setEnabled(true);
                mi2.setEnabled(true);

                cordeCourante = "sol";
                frequenceReference.setText("" + lesFrequences.get(cordeCourante));
            }
        });

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(false);
                mi2.setEnabled(true);

                cordeCourante = "si";
                frequenceReference.setText("" + lesFrequences.get(cordeCourante));
            }
        });

        mi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mi.setEnabled(true);
                la.setEnabled(true);
                re.setEnabled(true);
                sol.setEnabled(true);
                si.setEnabled(true);
                mi2.setEnabled(false);

                cordeCourante = "mi2";
                frequenceReference.setText("" + lesFrequences.get(cordeCourante));
            }
        });
    }

    private class RecupFrequence extends Thread
    {
        public RecupFrequence()
        {
            start();
        }

        public void run()
        {
            // Relie l'AudioDispatcher à l'entrée par défaut du smartphone (micro)
            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

            // Création d'un gestionnaire de détection de fréquence
            PitchDetectionHandler pdh = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult res, AudioEvent e){

                    /* Récupère le fréquence fondamentale du son capté par le micro en Hz.
                       Renvoie -1 si aucun son n'est capté. */
                    float frequenceDetectee = res.getPitch();

                    /* Le traitement de la fréquence se fait dans la méthode runOnUiThread car notre traitement
                    implique des changements dans l'interface (affichage de la fréquence par exemple)
                    qui ne sont faisable qu'en accédant au thread de l'interface utilisateur (UiThread)*/

                    runOnUiThread(new Indication(frequenceDetectee));

                    try
                    {
                        Thread.sleep((long) 200);
                    }
                    catch(InterruptedException ie){}
                }
            };

            /* Ajout du gestionnaire de détection au dispatcher.
            La détection se fera en suivant l'agorithme de Yin */
            AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
            dispatcher.addAudioProcessor(pitchProcessor);

            // On lance le dispatcher dans un thread à part
            audioThread = new Thread(dispatcher, "Audio Thread");
            audioThread.start();
        }
    }

    private class Indication implements Runnable
    {
        private float frequenceDetectee;

        public Indication(float frequenceDetectee)
        {
            this.frequenceDetectee = frequenceDetectee;
        }

        public void run()
        {
            if(frequenceDetectee == -1)
            {
                indication.setText("En attente");
                frequenceMesuree.setText("");
            }
            else
            {
                frequenceMesuree.setText(frequenceDetectee + " Hz");

                if(frequenceDetectee < (lesFrequences.get(cordeCourante) - 0.5))
                {
                    indication.setText("plus haut");
                }
                if(frequenceDetectee > (lesFrequences.get(cordeCourante) + 0.5))
                {
                    indication.setText("plus bas");
                }
                if(frequenceDetectee >= (lesFrequences.get(cordeCourante) -  0.5) && frequenceDetectee <= (lesFrequences.get(cordeCourante) + 0.5))
                {
                    indication.setText("c'est bien");
                }
            }
        }
    }
}
