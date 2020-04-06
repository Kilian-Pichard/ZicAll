package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.List;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import iutbayonne.projet.zicall.EcriturePartitionPackage.GestionnaireTypeNotePartition;
import iutbayonne.projet.zicall.EcriturePartitionPackage.NotePartition;
import iutbayonne.projet.zicall.EcriturePartitionPackage.Partition;
import static iutbayonne.projet.zicall.EcriturePartitionPackage.SourceImageNotePartition.*;

public class EcriturePartition extends AppCompatActivity {

    private float frequenceDetectee;
    private boolean ecritureTerminee;

    /**
     * Se charge de récupérer la fréquence provenant du microphone.
     */
    private AudioDispatcher dispatcher;

    /**
     * Se charge de lancer le dispatcher en continu tant .
     */
    private Thread audioThread;

    /**
     * Thread qui se charge de l'affichage en temps réel des notes reconnues.
     */
    private AffichageNotes affichageNotes;

    /**
     * Partition manipulée par l'utilisateur.
     */
    private Partition partition;

    /**
     * Permet de gérer l'affichage des différentes lignes de la partition.
     */
    private ListView listeLignesPartition;

    /**
     * Bouton qui permet de démarrer puis d'arrêter l'écriture.
     */
    private Button btnDemarrageStopEcriture;

    /**
     * Permet de jouer l'audio de la partition écrite.
     */
    private Button btnJouerPartition;

    /**
     * Bouton permettant de changer le type de la note sélectionnée
     * par le type de note associé au bouton.
     */
    private ImageView croche, noire, blanche, ronde;

    /**
     * Thread chargé de jouer la mélodie écrite par l'utilisateur.
     */
    private JouerPartition joueurDePartition;

    private boolean partitionEstEntrainDEtreJouee;

    /**
     * Intervalles de fréquences correspondants aux différentes notes.
     */
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

    /**
     * Ensemble des notes que l'on peut écrire sur la partition.
     */
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecriture_partition);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All </font>"));

        partition = new Partition();
        frequenceDetectee = -1;
        ecritureTerminee = false;
        partitionEstEntrainDEtreJouee = false;
        affichageNotes = null;
        joueurDePartition = null;

        this.croche = findViewById(R.id.choix_croche);
        this.croche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!partition.isWritting() && partition.getNotes().size() > 0) {
                    GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                            new GestionnaireTypeNotePartition(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage(), "croche");

                    //on modifie la source dans l'objet note
                    partition.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), gestionnaireTypeNotePartition.getNouvelleSourceImageNote());

                    //on modifie la durée en secondes de la note en fonction de son nouveau type
                    partition.getNotes().get(partition.getIndiceNoteCourante()).setDuree(0.5);

                    //on met à jour la vue
                    partition.afficher(listeLignesPartition, getApplicationContext());
                    partition.setEnCoursDeModification(false);
                }
            }
        });

        this.noire = findViewById(R.id.choix_noire);
        this.noire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!partition.isWritting() && partition.getNotes().size() > 0) {
                    GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                            new GestionnaireTypeNotePartition(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage(), "noire");

                    //on modifie l'image de l'objet note
                    partition.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), gestionnaireTypeNotePartition.getNouvelleSourceImageNote());

                    //on modifie la durée en secondes de la note en fonction de son nouveau type
                    partition.getNotes().get(partition.getIndiceNoteCourante()).setDuree(1.0);

                    //on met à jour la vue
                    partition.afficher(listeLignesPartition, getApplicationContext());
                    partition.setEnCoursDeModification(false);
                }
            }
        });

        this.blanche = findViewById(R.id.choix_blanche);
        this.blanche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!partition.isWritting() && partition.getNotes().size() > 0) {
                    GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                            new GestionnaireTypeNotePartition(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage(), "blanche");

                    //on modifie la source dans l'objet note
                    partition.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), gestionnaireTypeNotePartition.getNouvelleSourceImageNote());

                    //on modifie la durée en secondes de la note en fonction de son nouveau type
                    partition.getNotes().get(partition.getIndiceNoteCourante()).setDuree(2.0);

                    //on met à jour la vue
                    partition.afficher(listeLignesPartition, getApplicationContext());
                    partition.setEnCoursDeModification(false);
                }
            }
        });

        this.ronde = findViewById(R.id.choix_ronde);
        this.ronde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!partition.isWritting() && partition.getNotes().size() > 0) {
                    GestionnaireTypeNotePartition gestionnaireTypeNotePartition =
                            new GestionnaireTypeNotePartition(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()).getSourceImage(), "ronde");

                    //on modifie la source dans l'objet note
                    partition.getLigneCourante().setImageNote(partition.getLigneCourante().getNoteViaIndex(partition.getIndiceNoteCourante()), gestionnaireTypeNotePartition.getNouvelleSourceImageNote());

                    //on modifie la durée en secondes de la note en fonction de son nouveau type
                    partition.getNotes().get(partition.getIndiceNoteCourante()).setDuree(4.0);

                    //on met à jour la vue
                    partition.afficher(listeLignesPartition, getApplicationContext());
                    partition.setEnCoursDeModification(false);
                }
            }
        });

        listeLignesPartition = findViewById(R.id.partition_List_view);
        partition.afficher(listeLignesPartition, getApplicationContext());

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
                    btnDemarrageStopEcriture.setText("Arrêter écriture");
                    partition.setWritting(true);
                }
                else
                {
                    ecritureTerminee = true;
                    dispatcher.stop();
                    audioThread.interrupt();
                    affichageNotes.arreter();
                    btnDemarrageStopEcriture.setText("Demarrer écriture");
                    partition.setWritting(false);
                    partition.initialiserModificationPartition();
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
                if(!partition.isEnCoursDeModification() && !partitionEstEntrainDEtreJouee) {
                    lancerPartition();
                }
            }
        });


        affichageNotes = new AffichageNotes();

        audioThread.start();
        affichageNotes.start();
    }

    /**
     * Thread qui se charge d'afficher en continu des notes en fonction
     * des fréquences captées dans le microphone.
     */
    private class AffichageNotes extends Thread
    {
        public AffichageNotes()
        {}

        /**
         * S'exécute au lancement du Thread.
         */
        public void run()
        {
            while(true)
            {
                if(partition.isWritting())
                {
                    runOnUiThread(new EcrireNote());

                    try
                    {
                        Thread.sleep((long) 1000);
                    }
                    catch(InterruptedException ie)
                    {}
                }
            }
        }

        public void arreter()
        {
            this.interrupt();
        }
    }

    /**
     * Affiche une note à l'écran en fonction de la fréquence captée via le microphone.
     */
    private class EcrireNote implements Runnable
    {

        public EcrireNote()
        {}

        /**
         * Méthode à exécuter à la création du Runnable.
         */
        public void run()
        {
            if (frequenceDetectee != -1)
            {
                for(int indiceIntervalFrequence = 0; indiceIntervalFrequence < intervalesFrequencesReconnaissanceNotes.length; indiceIntervalFrequence++ )
                {
                    if(intervalesFrequencesReconnaissanceNotes[indiceIntervalFrequence][0] < frequenceDetectee
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

    /**
     * Méthode qui s'exécute lorsque l'on appuye sur le bouton "Retour" du smartphone.
     * Surchargée afin d'arrêter proprement les Thread de l'activity en cours avant de la quitter.
     */
    @Override
    public void onBackPressed()
    {
        affichageNotes.arreter();
        audioThread.interrupt();
        if(!dispatcher.isStopped())
        {
            dispatcher.stop();
        }

        if(joueurDePartition != null)
        {
            joueurDePartition.arreter();
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

        if(joueurDePartition != null)
        {
            joueurDePartition.arreter();
        }

        startActivity(otherActivity);
        finish();
    }

    /**
     * Récupère l'interface correspondante à la toolbar désirée et l'affiche.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_touteslesactivites, menu);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @deprecated Cette méthode n'est jamais utilisée. ELle doit être supprimée.
     */
    public ListView getListeLignesPartition()
    {
        return listeLignesPartition;
    }


    //Pour jouer la mélodie

    public void lancerPartition()
    {
        joueurDePartition = new JouerPartition();
    }

    /**
     * Thread chargé de jouer la partition écrite par l'utilisateur.
     */
    private class JouerPartition extends Thread
    {
        /**
         * Indique si le Thread doit s'arreter ou non
         */
        private boolean doitMourrir;

        public JouerPartition()
        {
            this.doitMourrir = false;
            start();
        }

        /**
         * S'exécute au lancement du Thread.
         */
        public void run()
        {
            partitionEstEntrainDEtreJouee = true;
            jouerToutesLesNotes();
            partitionEstEntrainDEtreJouee = false;
        }

        public void jouerToutesLesNotes()
        {
            NotePartition noteCourante;
            List<NotePartition> liste = partition.getNotes();

            for(int i=0; i <liste.size(); i++)
            {
                if(!this.doitMourrir)
                {
                    noteCourante = liste.get(i);
                    jouerNote(noteCourante);
                }
                else
                {
                    break;
                }
            }
        }

        private void jouerNote(NotePartition noteCourante)
        {
            MediaPlayer audioNote;
            audioNote = MediaPlayer.create(getApplicationContext(), noteCourante.getAudioNote());

            audioNote.start();

            attendre((long) (noteCourante.getDuree()*1000));

            audioNote.stop();
            audioNote.release();
            audioNote = null;
        }

        public void attendre(long duree)
        {
            try
            {
                Thread.sleep(duree);
            }
            catch(InterruptedException ie)
            {}
        }

        public void arreter()
        {
            this.doitMourrir = true;
            partitionEstEntrainDEtreJouee = false;
            this.interrupt();
        }
    }
}
