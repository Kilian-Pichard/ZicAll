package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import iutbayonne.projet.zicall.EcriturePartitionPackage.Ligne;
import iutbayonne.projet.zicall.EcriturePartitionPackage.NotePartition;
import iutbayonne.projet.zicall.EcriturePartitionPackage.Partition;
import iutbayonne.projet.zicall.EcriturePartitionPackage.SourceImageNotePartition;

import static iutbayonne.projet.zicall.EcriturePartitionPackage.SourceImageNotePartition.*;

public class EcriturePartition extends AppCompatActivity {

    private float frequenceDetectee;
    private boolean ecritureTerminee;
    private Thread audioThread;
    private AudioDispatcher dispatcher;
    private AffichageNotes affichageNotes;

    private Partition partition;
    private ListView listeLignesPartition;
    private Button btnDemarrageStopEcriture;
    private Button btnDo1;

    private double intervalesFrequencesReconnaissanceNotes[][] = {  {120, 134}, //  do_gamme_0
                                                                    {134, 142}, //  do_diese_gamme_0
                                                                    {142, 150}, //  re_gamme_0
                                                                    {150, 157}, //  re_diese_gamme_0
                                                                    {157, 169}, //  mi_gamme_0
                                                                    {169, 179}, //  fa_gamme_0
                                                                    {179, 190}, //  fa_diese_gamme_0
                                                                    {190, 201}, //  sol_gamme_0
                                                                    {201, 213}, //  sol_diese_gamme_0
                                                                    {213, 226}, //  la_gamme_0
                                                                    {226, 239}, //  la_diese_gamme_0
                                                                    {239, 253}, //  si_gamme_0
                                                                    {253, 269}, //  do_gamme_1
                                                                    {269, 285}, //  do_diese_gamme_1
                                                                    {285, 302}, //  re_gamme_1
                                                                    {302, 320}, //  re_diese_gamme_1
                                                                    {320, 339}, //  mi_gamme_1
                                                                    {339, 359}, //  fa_gamme_1
                                                                    {359, 380}, //  fa_diese_gamme_1
                                                                    {380, 403}, //  sol_gamme_1
                                                                    {403, 427}, //  sol_diese_gamme_1
                                                                    {427, 453}, //  la_gamme_1
                                                                    {453, 479}, //  la_diese_gamme_1
                                                                    {479, 508} //   si_gamme_1
                                                                };

    private NotePartition listeNotes[] = {  new NotePartition(DO_GAMME_0_NOIRE),
                                            new NotePartition(DO_DIESE_GAMME_0_NOIRE),
                                            new NotePartition(RE_GAMME_0_NOIRE),
                                            new NotePartition(RE_DIESE_GAMME_0_NOIRE),
                                            new NotePartition(MI_GAMME_0_NOIRE),
                                            new NotePartition(FA_GAMME_0_NOIRE),
                                            new NotePartition(FA_DIESE_GAMME_0_NOIRE),
                                            new NotePartition(SOL_GAMME_0_NOIRE),
                                            new NotePartition(SOL_DIESE_GAMME_0_NOIRE),
                                            new NotePartition(LA_GAMME_0_NOIRE),
                                            new NotePartition(LA_DIESE_GAMME_0_NOIRE),
                                            new NotePartition(SI_GAMME_0_NOIRE),
                                            new NotePartition(DO_GAMME_1_NOIRE),
                                            new NotePartition(DO_DIESE_GAMME_1_NOIRE),
                                            new NotePartition(RE_GAMME_1_NOIRE),
                                            new NotePartition(RE_DIESE_GAMME_1_NOIRE),
                                            new NotePartition(MI_GAMME_1_NOIRE),
                                            new NotePartition(FA_GAMME_1_NOIRE),
                                            new NotePartition(FA_DIESE_GAMME_1_NOIRE),
                                            new NotePartition(SOL_GAMME_1_NOIRE),
                                            new NotePartition(SOL_DIESE_GAMME_1_NOIRE),
                                            new NotePartition(LA_GAMME_1_NOIRE),
                                            new NotePartition(LA_DIESE_GAMME_1_NOIRE),
                                            new NotePartition(SI_GAMME_1_NOIRE),
                                         };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecriture_partition);

        partition = new Partition();
        frequenceDetectee = -1;
        ecritureTerminee = false;
        affichageNotes = null;

        listeLignesPartition = findViewById(R.id.partition_List_view);
        partition.afficher(listeLignesPartition, getApplicationContext());

        //----ON POURRA SUPPRIMER çA A LA FIN-------------
        btnDo1 = findViewById(R.id.btnDo1);
        btnDo1.setEnabled(false);
        btnDo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(DO_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });
        //------------------------------------------------

        //CREATION DISPATCHER POUR RECUPERER LA FREQUENCE
            // Relie l'AudioDispatcher à l'entrée par défaut du smartphone (micro)
            dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

            // Création d'un gestionnaire de détection de fréquence
            PitchDetectionHandler pdh = new PitchDetectionHandler() {
                @Override
                public void handlePitch(PitchDetectionResult res, AudioEvent e){
                    if(partition.isWritting()) {
                                    // Récupère le fréquence fondamentale du son capté par le micro en Hz.
                                     //  Renvoie -1 si aucun son n'est capté.
                        if (frequenceDetectee == -1) {
                            frequenceDetectee = res.getPitch();
                        }
                    }
                }
            };

            // Ajout du gestionnaire de détection au dispatcher.
            //La détection se fera en suivant l'agorithme de Yin //
            AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
            dispatcher.addAudioProcessor(pitchProcessor);

        //CREATION Tread de récupération de fréquence
        audioThread = new Thread(dispatcher, "Audio Thread");


        btnDemarrageStopEcriture = findViewById(R.id.btnDemarrageStopEcriture);
        btnDemarrageStopEcriture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!partition.isWritting()){
                    btnDo1.setEnabled(true);
                    partition.setWritting(true);
                    btnDemarrageStopEcriture.setText("Arrêter écriture");

                }
                else{
                    ecritureTerminee = true;
                    dispatcher.stop();
                    audioThread.interrupt();
                    affichageNotes.arreter();
                    btnDo1.setEnabled(false);
                    btnDemarrageStopEcriture.setText("Demarrer écriture");
                    partition.setWritting(false);

                    //rafraichir les onclicks sur les notes
                       /* partition.ajouterLigne(new Ligne(new NotePartition(PARTITION_VIERGER),
                                        new NotePartition(PARTITION_VIERGER),
                                        new NotePartition(PARTITION_VIERGER),
                                        new NotePartition(PARTITION_VIERGER),
                                        new NotePartition(PARTITION_VIERGER),
                                        new NotePartition(PARTITION_VIERGER)
                                )
                        );
                        partition.supprimerLigne(partition.getLignes().get(partition.getIndiceLigneCourante()+1));//supprimer la dernière ligne
                        partition.afficher(listeLignesPartition, getApplicationContext());*/
                }

                if(ecritureTerminee){
                    btnDemarrageStopEcriture.setEnabled(false); //empêcher de relancer l'enregistrement
                }
            }
        });


        affichageNotes = new AffichageNotes();

        audioThread.start();
        affichageNotes.start();
    }

    private class AffichageNotes extends Thread
    {

        public AffichageNotes(){}

        public void run()
        {
            while(true) {
                if(partition.isWritting()) {
                    runOnUiThread(new EcrireNote());

                    try {
                        Thread.sleep((long) 1000);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        }

        public void arreter(){
            this.interrupt();
        }
    }

    private class EcrireNote implements Runnable {

        public EcrireNote() {
        }

        public void run() {
            if (frequenceDetectee != -1) {
                for(int indiceIntervalFrequence = 0; indiceIntervalFrequence < intervalesFrequencesReconnaissanceNotes.length; indiceIntervalFrequence++ ){
                    if( intervalesFrequencesReconnaissanceNotes[indiceIntervalFrequence][0] < frequenceDetectee
                            &&
                        frequenceDetectee < intervalesFrequencesReconnaissanceNotes[indiceIntervalFrequence][1])
                    {
                        partition.ajouterNote(listeNotes[indiceIntervalFrequence], listeLignesPartition, getApplicationContext());
                        break;
                    }
                }
            }
            frequenceDetectee = -1;
        }
    }
}
