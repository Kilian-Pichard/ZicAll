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
    private Thread audioThread;
    private AudioDispatcher dispatcher;
    private AffichageNotes affichageNotes;
    private boolean demandeInterruption;

    private Partition partition;
    private ListView listeLignesPartition;
    private Button btnDemarrageStopEcriture;
    private Button btnDo1;
    private Button btnRe1;
    private Button btnMi1;
    private Button btnFa1;
    private Button btnSol1;
    private Button btnLa1;
    private Button btnSi1;
    private Button btnDo0;
    private Button btnRe0;
    private Button btnMi0;
    private Button btnFa0;
    private Button btnSol0;
    private Button btnLa0;
    private Button btnSi0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecriture_partition);

        partition = new Partition();
        listeLignesPartition = findViewById(R.id.partition_List_view);

        btnDemarrageStopEcriture = findViewById(R.id.btnDemarrageStopEcriture);

        configurerLesBouttons();

        desactiverBouttonsNotes();

        frequenceDetectee = -1;

        demandeInterruption = false;

        partition.afficher(listeLignesPartition, getApplicationContext());
        affichageNotes = null;



        //CREATION DISPATCHER POUR RECUPERER LA FREQUENCE
        // Relie l'AudioDispatcher à l'entrée par défaut du smartphone (micro)
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        // Création d'un gestionnaire de détection de fréquence
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e){
                if(partition.isWritting()) {
                                /* Récupère le fréquence fondamentale du son capté par le micro en Hz.
                                   Renvoie -1 si aucun son n'est capté. */
                    if (frequenceDetectee == -1) {
                        frequenceDetectee = res.getPitch();
                    }

                }
            }
        };

                        /* Ajout du gestionnaire de détection au dispatcher.
                        La détection se fera en suivant l'agorithme de Yin */
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);

        //CREATION ET LANCEMENT THREAD RECUPERATION FREQUENCE
        audioThread = new Thread(dispatcher, "Audio Thread");

        audioThread.start();
        affichageNotes = new AffichageNotes();

        btnDemarrageStopEcriture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(partition.isWritting()){
                    //affichageNotes.interrupt();
                    //demandeInterruption = true;
                    desactiverBouttonsNotes();
                    btnDemarrageStopEcriture.setText("Demarrer écriture");
                    partition.setWritting(false);

                    //rafraichir les onclicks sur les notes
                    partition.ajouterLigne(new Ligne(new NotePartition(PARTITION_VIERGER),
                                    new NotePartition(PARTITION_VIERGER),
                                    new NotePartition(PARTITION_VIERGER),
                                    new NotePartition(PARTITION_VIERGER),
                                    new NotePartition(PARTITION_VIERGER),
                                    new NotePartition(PARTITION_VIERGER)
                            )
                    );
                    partition.supprimerLigne(partition.getLignes().get(partition.getIndiceLigneCourante()+1));//supprimer la dernière ligne
                    partition.afficher(listeLignesPartition, getApplicationContext());
                }
                else{
                    //demandeInterruption = false;
                    activerBouttonsNotes();
                    partition.setWritting(true);
                    btnDemarrageStopEcriture.setText("Arrêter écriture");
                }
            }
        });

        //while(!demandeInterruption){}

        //audioThread.interrupt();
        //dispatcher.stop();

        //partition.ajouterNote(new NotePartition(DO_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());


    }

    public void rafraichirClickSurNotes(){
        partition.ajouterLigne(new Ligne(new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER),
                        new NotePartition(PARTITION_VIERGER)
                )
        );
        partition.supprimerLigne(partition.getLignes().get(partition.getIndiceLigneCourante()+1));//supprimer la dernière ligne
        partition.afficher(listeLignesPartition, getApplicationContext());
    }

    public void configurerLesBouttons(){

        btnDo1 = findViewById(R.id.btnDo1);
        btnDo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(DO_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnRe1 = findViewById(R.id.btnRe1);
        btnRe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(RE_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnMi1 = findViewById(R.id.btnMi1);
        btnMi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(MI_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnFa1 = findViewById(R.id.btnFa1);
        btnFa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(FA_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnSol1 = findViewById(R.id.btnSol1);
        btnSol1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(SOL_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnLa1 = findViewById(R.id.btnLa1);
        btnLa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(LA_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnSi1 = findViewById(R.id.btnSi1);
        btnSi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(SI_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnDo0 = findViewById(R.id.btnDo0);
        btnDo0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(DO_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnRe0 = findViewById(R.id.btnRe0);
        btnRe0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(RE_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnMi0 = findViewById(R.id.btnMi0);
        btnMi0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(MI_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnFa0 = findViewById(R.id.btnFa0);
        btnFa0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(FA_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnSol0 = findViewById(R.id.btnSol0);
        btnSol0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(SOL_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnLa0 = findViewById(R.id.btnLa0);
        btnLa0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(LA_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });

        btnSi0 = findViewById(R.id.btnSi0);
        btnSi0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(SI_GAMME_0_NOIRE), listeLignesPartition, getApplicationContext());
            }
        });
    }

    public void desactiverBouttonsNotes(){
        btnDo1.setEnabled(false);
        btnRe1.setEnabled(false);
        btnMi1.setEnabled(false);
        btnFa1.setEnabled(false);
        btnSol1.setEnabled(false);
        btnLa1.setEnabled(false);
        btnSi1.setEnabled(false);
        btnDo0.setEnabled(false);
        btnRe0.setEnabled(false);
        btnMi0.setEnabled(false);
        btnFa0.setEnabled(false);
        btnSol0.setEnabled(false);
        btnLa0.setEnabled(false);
        btnSi0.setEnabled(false);
    }

    public void activerBouttonsNotes(){
        btnDo1.setEnabled(true);
        btnRe1.setEnabled(true);
        btnMi1.setEnabled(true);
        btnFa1.setEnabled(true);
        btnSol1.setEnabled(true);
        btnLa1.setEnabled(true);
        btnSi1.setEnabled(true);
        btnDo0.setEnabled(true);
        btnRe0.setEnabled(true);
        btnMi0.setEnabled(true);
        btnFa0.setEnabled(true);
        btnSol0.setEnabled(true);
        btnLa0.setEnabled(true);
        btnSi0.setEnabled(true);
    }


    public void toutesLesNotes(){
        partition.ajouterLigne(new Ligne(new NotePartition(DO_GAMME_1_NOIRE),
                        new NotePartition(RE_GAMME_1_NOIRE),
                        new NotePartition(MI_GAMME_1_NOIRE),
                        new NotePartition(FA_GAMME_1_NOIRE),
                        new NotePartition(SOL_GAMME_1_NOIRE),
                        new NotePartition(LA_GAMME_1_NOIRE)
                )

        );
        partition.ajouterLigne(new Ligne(new NotePartition(SI_GAMME_1_NOIRE),
                        new NotePartition(DO_DIESE_GAMME_1_NOIRE),
                        new NotePartition(RE_DIESE_GAMME_1_NOIRE),
                        new NotePartition(FA_DIESE_GAMME_1_NOIRE),
                        new NotePartition(SOL_DIESE_GAMME_1_NOIRE),
                        new NotePartition(LA_DIESE_GAMME_1_NOIRE)
                )

        );

        partition.ajouterLigne(new Ligne(new NotePartition(DO_GAMME_0_NOIRE),
                        new NotePartition(RE_GAMME_0_NOIRE),
                        new NotePartition(MI_GAMME_0_NOIRE),
                        new NotePartition(FA_GAMME_0_NOIRE),
                        new NotePartition(SOL_GAMME_0_NOIRE),
                        new NotePartition(LA_GAMME_0_NOIRE)
                )
        );
        partition.ajouterLigne(new Ligne(new NotePartition(SI_GAMME_0_NOIRE),
                        new NotePartition(DO_DIESE_GAMME_0_NOIRE),
                        new NotePartition(RE_DIESE_GAMME_0_NOIRE),
                        new NotePartition(FA_DIESE_GAMME_0_NOIRE),
                        new NotePartition(SOL_DIESE_GAMME_0_NOIRE),
                        new NotePartition(LA_DIESE_GAMME_0_NOIRE)
                )

        );
    }


    public NotePartition determinerNote(double frequence){
        NotePartition note = null;

        if(250 < frequenceDetectee && frequenceDetectee < 500) {//gamme 1
            if (250 < frequenceDetectee && frequenceDetectee < 269) {
                note = new NotePartition(DO_GAMME_1_NOIRE);
            }
            else {
                if (269 < frequenceDetectee && frequenceDetectee < 285) {
                    note = new NotePartition(DO_DIESE_GAMME_1_NOIRE);
                }
            }
        }
        if(frequenceDetectee == -1){
            note = new NotePartition(SI_GAMME_1_RONDE);
        }

        /*if(120 < frequenceDetectee && frequenceDetectee < 250){//gamme 0
            if(120 < frequenceDetectee && frequenceDetectee < 134){
                note = new NotePartition(DO_GAMME_0_NOIRE);
            }
            else{
                if(134 < frequenceDetectee && frequenceDetectee < 142){
                    note = new NotePartition(DO_DIESE_GAMME_0_NOIRE);
                }
                else{
                    if(142 < frequenceDetectee && frequenceDetectee < 150.5){
                        note = new NotePartition(RE_GAMME_0_NOIRE);
                    }
                    else{
                        if(150.5 < frequenceDetectee && frequenceDetectee < 159.5){
                            note = new NotePartition(RE_DIESE_GAMME_0_NOIRE);
                        }
                        else{
                            if(159.5 < frequenceDetectee && frequenceDetectee < 169){
                                note = new NotePartition(MI_GAMME_0_NOIRE);
                            }
                            else{
                                if(169 < frequenceDetectee && frequenceDetectee < 179){
                                    note = new NotePartition(FA_DIESE_GAMME_1_NOIRE);
                                }
                                else{
                                    if(179 < frequenceDetectee && frequenceDetectee < 189.5){
                                        note = new NotePartition(FA_DIESE_GAMME_1_NOIRE);
                                    }
                                    else{
                                        if(189.5 < frequenceDetectee && frequenceDetectee < 285){
                                            note = new NotePartition(SOL_GAMME_0_NOIRE);
                                        }
                                        else{
                                            if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                note = new NotePartition(SOL_DIESE_GAMME_0_NOIRE);
                                            }
                                            else{
                                                if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                    note = new NotePartition(LA_GAMME_0_NOIRE);
                                                }
                                                else{
                                                    if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                        note = new NotePartition(LA_DIESE_GAMME_0_NOIRE);
                                                    }
                                                    else{
                                                        if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                            note = new NotePartition(SI_GAMME_0_NOIRE);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else{
            if(250 < frequenceDetectee && frequenceDetectee < 500){//gamme 1
                if(250 < frequenceDetectee && frequenceDetectee < 269){
                    note = new NotePartition(DO_GAMME_1_NOIRE);
                }
                else{
                    if(269 < frequenceDetectee && frequenceDetectee < 285){
                        note = new NotePartition(DO_DIESE_GAMME_1_NOIRE);
                    }
                    else{
                        if(269 < frequenceDetectee && frequenceDetectee < 285){
                            note = new NotePartition(RE_GAMME_1_NOIRE);
                        }
                        else{
                            if(269 < frequenceDetectee && frequenceDetectee < 285){
                                note = new NotePartition(RE_DIESE_GAMME_1_NOIRE);
                            }
                            else{
                                if(269 < frequenceDetectee && frequenceDetectee < 285){
                                    note = new NotePartition(MI_GAMME_1_NOIRE);
                                }
                                else{
                                    if(269 < frequenceDetectee && frequenceDetectee < 285){
                                        note = new NotePartition(FA_DIESE_GAMME_1_NOIRE);
                                    }
                                    else{
                                        if(269 < frequenceDetectee && frequenceDetectee < 285){
                                            note = new NotePartition(FA_DIESE_GAMME_1_NOIRE);
                                        }
                                        else{
                                            if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                note = new NotePartition(SOL_GAMME_1_NOIRE);
                                            }
                                            else{
                                                if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                    note = new NotePartition(SOL_DIESE_GAMME_1_NOIRE);
                                                }
                                                else{
                                                    if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                        note = new NotePartition(LA_GAMME_1_NOIRE);
                                                    }
                                                    else{
                                                        if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                            note = new NotePartition(LA_DIESE_GAMME_1_NOIRE);
                                                        }
                                                        else{
                                                            if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                                note = new NotePartition(SI_GAMME_1_NOIRE);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else{
                if(250 < frequenceDetectee && frequenceDetectee < 500){//gamme 2
                    if(250 < frequenceDetectee && frequenceDetectee < 269){
                        note = new NotePartition(DO_GAMME_1_NOIRE);
                    }
                    else{
                        if(269 < frequenceDetectee && frequenceDetectee < 285){
                            note = new NotePartition(DO_DIESE_GAMME_1_NOIRE);
                        }
                        else{
                            if(269 < frequenceDetectee && frequenceDetectee < 285){
                                note = new NotePartition(RE_GAMME_1_NOIRE);
                            }
                            else{
                                if(269 < frequenceDetectee && frequenceDetectee < 285){
                                    note = new NotePartition(RE_DIESE_GAMME_1_NOIRE);
                                }
                                else{
                                    if(269 < frequenceDetectee && frequenceDetectee < 285){
                                        note = new NotePartition(MI_GAMME_1_NOIRE);
                                    }
                                    else{
                                        if(269 < frequenceDetectee && frequenceDetectee < 285){
                                            note = new NotePartition(FA_DIESE_GAMME_1_NOIRE);
                                        }
                                        else{
                                            if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                note = new NotePartition(FA_DIESE_GAMME_1_NOIRE);
                                            }
                                            else{
                                                if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                    note = new NotePartition(SOL_GAMME_1_NOIRE);
                                                }
                                                else{
                                                    if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                        note = new NotePartition(SOL_DIESE_GAMME_1_NOIRE);
                                                    }
                                                    else{
                                                        if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                            note = new NotePartition(LA_GAMME_1_NOIRE);
                                                        }
                                                        else{
                                                            if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                                note = new NotePartition(LA_DIESE_GAMME_1_NOIRE);
                                                            }
                                                            else{
                                                                if(269 < frequenceDetectee && frequenceDetectee < 285){
                                                                    note = new NotePartition(SI_GAMME_1_NOIRE);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }*/

        return note;
    }

    private class AffichageNotes extends Thread
    {

        public AffichageNotes()
        {
            start();
        }

        public void run()
        {
            while(true) {

                if(partition.isWritting()) {
                    runOnUiThread(new EcrireNote());

                    try {
                        Thread.sleep((long) 800);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        }
    }

    private class EcrireNote implements Runnable
    {

        public EcrireNote()
        {
        }

        public void run()
        {
            if (frequenceDetectee != -1){

                if(250 < frequenceDetectee && frequenceDetectee < 500) {//gamme 1
                    if (250 < frequenceDetectee && frequenceDetectee < 269) {
                        partition.ajouterNote(new NotePartition(DO_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
                    }
                    else {
                        if (269 < frequenceDetectee && frequenceDetectee < 285) {
                            partition.ajouterNote(new NotePartition(DO_DIESE_GAMME_1_NOIRE), listeLignesPartition, getApplicationContext());
                        }
                    }
                }
            }

            frequenceDetectee = -1;
        }
    }
}
