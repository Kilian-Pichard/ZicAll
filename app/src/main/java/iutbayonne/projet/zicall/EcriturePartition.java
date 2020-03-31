package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.NoteMelodie;
import iutbayonne.projet.zicall.EcriturePartitionPackage.GestionnaireTypeNotePartition;
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
    private Button btnJouerPartition;
    private Button btnDo1;

    private ImageView croche;
    private ImageView noire;
    private ImageView blanche;
    private ImageView ronde;

    private JouerPartition joueurDePartition;

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

    private NotePartition listeNotes[] = {  new NotePartition(DO_GAMME_0_NOIRE, R.raw.do1, 1.0),
                                            new NotePartition(DO_DIESE_GAMME_0_NOIRE, R.raw.dod1, 1.0),
                                            new NotePartition(RE_GAMME_0_NOIRE, R.raw.re1, 1.0),
                                            new NotePartition(RE_DIESE_GAMME_0_NOIRE, R.raw.red1, 1.0),
                                            new NotePartition(MI_GAMME_0_NOIRE, R.raw.mi1, 1.0),
                                            new NotePartition(FA_GAMME_0_NOIRE, R.raw.fa1, 1.0),
                                            new NotePartition(FA_DIESE_GAMME_0_NOIRE, R.raw.fad1, 1.0),
                                            new NotePartition(SOL_GAMME_0_NOIRE, R.raw.sol1, 1.0),
                                            new NotePartition(SOL_DIESE_GAMME_0_NOIRE, R.raw.sold1, 1.0),
                                            new NotePartition(LA_GAMME_0_NOIRE, R.raw.la1, 1.0),
                                            new NotePartition(LA_DIESE_GAMME_0_NOIRE, R.raw.lad1, 1.0),
                                            new NotePartition(SI_GAMME_0_NOIRE, R.raw.si1, 1.0),
                                            new NotePartition(DO_GAMME_1_NOIRE, R.raw.do2, 1.0),
                                            new NotePartition(DO_DIESE_GAMME_1_NOIRE, R.raw.dod2, 1.0),
                                            new NotePartition(RE_GAMME_1_NOIRE, R.raw.re2, 1.0),
                                            new NotePartition(RE_DIESE_GAMME_1_NOIRE, R.raw.red2, 1.0),
                                            new NotePartition(MI_GAMME_1_NOIRE, R.raw.mi2, 1.0),
                                            new NotePartition(FA_GAMME_1_NOIRE, R.raw.fa2, 1.0),
                                            new NotePartition(FA_DIESE_GAMME_1_NOIRE, R.raw.fad2, 1.0),
                                            new NotePartition(SOL_GAMME_1_NOIRE, R.raw.sol2, 1.0),
                                            new NotePartition(SOL_DIESE_GAMME_1_NOIRE, R.raw.sold2, 1.0),
                                            new NotePartition(LA_GAMME_1_NOIRE, R.raw.la2, 1.0),
                                            new NotePartition(LA_DIESE_GAMME_1_NOIRE, R.raw.lad2, 1.0),
                                            new NotePartition(SI_GAMME_1_NOIRE, R.raw.si2, 1.0),
                                         };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecriture_partition);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        partition = new Partition();
        frequenceDetectee = -1;
        ecritureTerminee = false;
        affichageNotes = null;

        this.croche = findViewById(R.id.choix_croche);
        this.croche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                        new GestionnaireTypeNotePartition(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage(), "croche");

                //on modifie la source dans l'objet note
                partition.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), gestionnaireTypeNotePartition.getNouvelleSourceImageNote());

                //on modifie la durée en secondes de la note en fonction de son nouveau type
                partition.getNotes().get(partition.getIndiceNoteCourante()).setDuree(0.5);

                //on met à jour la vue
                partition.afficher(listeLignesPartition, getApplicationContext());
            }
        });

        this.noire = findViewById(R.id.choix_noire);
        this.noire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                        new GestionnaireTypeNotePartition(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage(), "noire");

                //on modifie l'image de l'objet note
                partition.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), gestionnaireTypeNotePartition.getNouvelleSourceImageNote());

                //on modifie la durée en secondes de la note en fonction de son nouveau type
                partition.getNotes().get(partition.getIndiceNoteCourante()).setDuree(1.0);

                //on met à jour la vue
                partition.afficher(listeLignesPartition, getApplicationContext());
            }
        });

        this.blanche = findViewById(R.id.choix_blanche);
        this.blanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                        new GestionnaireTypeNotePartition(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage(), "blanche");

                //on modifie la source dans l'objet note
                partition.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), gestionnaireTypeNotePartition.getNouvelleSourceImageNote());

                //on modifie la durée en secondes de la note en fonction de son nouveau type
                partition.getNotes().get(partition.getIndiceNoteCourante()).setDuree(2.0);

                //on met à jour la vue
                partition.afficher(listeLignesPartition, getApplicationContext());
            }
        });

        this.ronde = findViewById(R.id.choix_ronde);
        this.ronde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                        new GestionnaireTypeNotePartition(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage(), "ronde");

                //on modifie la source dans l'objet note
                partition.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), gestionnaireTypeNotePartition.getNouvelleSourceImageNote());

                //on modifie la durée en secondes de la note en fonction de son nouveau type
                partition.getNotes().get(partition.getIndiceNoteCourante()).setDuree(4.0);

                //on met à jour la vue
                partition.afficher(listeLignesPartition, getApplicationContext());
            }
        });

        listeLignesPartition = findViewById(R.id.partition_List_view);
        partition.afficher(listeLignesPartition, getApplicationContext());

        //----ON POURRA SUPPRIMER çA A LA FIN-------------
        btnDo1 = findViewById(R.id.btnDo1);
        btnDo1.setEnabled(false);
        btnDo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                partition.ajouterNote(new NotePartition(DO_GAMME_1_NOIRE, R.raw.do2, 1.0), listeLignesPartition, getApplicationContext());
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
                if(!partition.isWritting())
                {
                    btnDo1.setEnabled(true);
                    partition.setWritting(true);
                    btnDemarrageStopEcriture.setText("Arrêter écriture");
                }
                else
                {
                    ecritureTerminee = true;
                    dispatcher.stop();
                    audioThread.interrupt();
                    affichageNotes.arreter();
                    btnDo1.setEnabled(false);
                    btnDemarrageStopEcriture.setText("Demarrer écriture");
                    partition.setWritting(false);
                    btnJouerPartition.setEnabled(true);
                }

                if(ecritureTerminee)
                {
                    btnDemarrageStopEcriture.setEnabled(false); //empêcher de relancer l'enregistrement
                }
            }
        });


        this.btnJouerPartition = findViewById(R.id.btnJouerPartition);
        this.btnJouerPartition.setEnabled(false);
        this.btnJouerPartition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerPartition();
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

    @Override
    public void onBackPressed()
    {
        affichageNotes.arreter();
        audioThread.interrupt();
        if(!dispatcher.isStopped())
        {
            dispatcher.stop();
        }
        super.onBackPressed();
    }

    public void accederAccueil()
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        affichageNotes.arreter();
        audioThread.interrupt();
        if(!dispatcher.isStopped())
        {
            dispatcher.stop();
        }

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

    public ListView getListeLignesPartition() {
        return listeLignesPartition;
    }


    //Pour jouer la mélodie

    public void lancerPartition() {
        joueurDePartition = new JouerPartition(partition);
    }

    private class JouerPartition extends Thread
    {
        private Partition melodie;
        private boolean doitMourrir;

        public JouerPartition(Partition melodie)
        {
            this.melodie = melodie;
            this.doitMourrir = false;
            start();
        }

        public void run()
        {
            jouerToutesLesNotes();
        }

        public void jouerToutesLesNotes(){
            NotePartition noteCourante;
            List<NotePartition> liste = partition.getNotes();
            for(int i=0; i<liste.size(); i++){
                noteCourante = liste.get(i);
                jouerNote(noteCourante);
            }
        }

        private void jouerNote(final NotePartition noteCourante) {

            final MediaPlayer audioNote;
            audioNote = MediaPlayer.create(getApplicationContext(), noteCourante.getAudioNote());
            final SourceImageNotePartition sourceNoteMemoire = partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    audioNote.start();
                }
            });

            attendre((long) (noteCourante.getDuree()*1000));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    audioNote.stop();
                    audioNote.release();
                    //audioNote = null;
                }
            });
        }

        public void attendre(long duree){
            try{
                Thread.sleep(duree);
            }catch(InterruptedException ie){}
        }
    }
}
