package iutbayonne.projet.zicall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class AccordeurGuitare extends AppCompatActivity {

    private TextView frequenceMesuree;

    private Thread audioThread;
    private AudioDispatcher dispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accordeur_guitare);

        this.frequenceMesuree = findViewById(R.id.frequenceMesure);
        new JouerMelodie();
    }


    // Détermine ce que l'on affiche en fonction de la fréquence détectée
    public void traiterFrequence(float frequenceDetectee)
    {
        if(frequenceDetectee == -1)
        {
            frequenceMesuree.setText("Aucun son audible");
        }
        else
        {
            frequenceMesuree.setText(frequenceDetectee + " Hz");
        }
    }

    private class JouerMelodie extends Thread
    {
        public JouerMelodie()
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
                    final float frequenceDetectee = res.getPitch();

                    /* Le traitement de la fréquence se fait dans la méthode runOnUiThread car notre traitement
                    implique des changements dans l'interface (affichage de la fréquence par exemple)
                    qui ne sont faisable qu'en accédant au thread de l'interface utilisateur (UiThread)*/

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new JouerMelodie();
                            traiterFrequence(frequenceDetectee);
                        }
                    });

                    try
                    {
                        Thread.sleep((long) 300);
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
}
