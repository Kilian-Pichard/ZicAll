package iutbayonne.projet.zicall;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.lang.String;

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
     * Permet d'enregistrer la partition écrite dans le téléphone.
     */
    private Button btnEnregistrerPartition;

    /**
     * Permet de récupérer les partitions écrites dans le téléphone.
     */
    private Button btnRecupererPartition;

    /**
     * Bouton permettant de changer le type de la note sélectionnée
     * par le type de note associé au bouton.
     */
    private ImageView croche, noire, blanche, ronde;

    /**
     * Thread chargé de jouer la mélodie écrite par l'utilisateur.
     */
    private JouerPartition joueurDePartition;

    /**
     * Booleen qui permet de savoir si la partition est en train d'etre jouée
     */
    private boolean partitionEstEntrainDEtreJouee;

    /**
     * Ensemble des notes qui vont etre enrgsitrées
     */
    private String lesNotes;

    /**
     * Ensemble des notes qui sont présentes dans la partition
     */
    private String laListeDesNotes = "listeVide";

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
    private NotePartition listeNotes[] = {  new NotePartition("DO_GAMME_0_NOIRE","NOIRE",DO_GAMME_0_NOIRE, R.raw.do1, 1.0),
            new NotePartition("DO_DIESE_GAMME_0","NOIRE",DO_DIESE_GAMME_0_NOIRE, R.raw.dod1, 1.0),
            new NotePartition("RE_GAMME_0","NOIRE",RE_GAMME_0_NOIRE, R.raw.re1, 1.0),
            new NotePartition("RE_DIESE_GAMME_0","NOIRE",RE_DIESE_GAMME_0_NOIRE, R.raw.red1, 1.0),
            new NotePartition("MI_GAMME_0","NOIRE",MI_GAMME_0_NOIRE, R.raw.mi1, 1.0),
            new NotePartition("FA_GAMME_0","NOIRE",FA_GAMME_0_NOIRE, R.raw.fa1, 1.0),
            new NotePartition("FA_DIESE_GAMME_0","NOIRE",FA_DIESE_GAMME_0_NOIRE, R.raw.fad1, 1.0),
            new NotePartition("SOL_GAMME_0","NOIRE",SOL_GAMME_0_NOIRE, R.raw.sol1, 1.0),
            new NotePartition("SOL_DIESE_GAMME_0","NOIRE",SOL_DIESE_GAMME_0_NOIRE, R.raw.sold1, 1.0),
            new NotePartition("LA_GAMME_0","NOIRE",LA_GAMME_0_NOIRE, R.raw.la1, 1.0),
            new NotePartition("LA_DIESE_GAMME_0","NOIRE",LA_DIESE_GAMME_0_NOIRE, R.raw.lad1, 1.0),
            new NotePartition("SI_GAMME_0","NOIRE",SI_GAMME_0_NOIRE, R.raw.si1, 1.0),
            new NotePartition("DO_GAMME_1","NOIRE",DO_GAMME_1_NOIRE, R.raw.do2, 1.0),
            new NotePartition("DO_DIESE_GAMME_1","NOIRE",DO_DIESE_GAMME_1_NOIRE, R.raw.dod2, 1.0),
            new NotePartition("RE_GAMME_1","NOIRE",RE_GAMME_1_NOIRE, R.raw.re2, 1.0),
            new NotePartition("RE_DIESE_GAMME_1","NOIRE",RE_DIESE_GAMME_1_NOIRE, R.raw.red2, 1.0),
            new NotePartition("MI_GAMME_1","NOIRE",MI_GAMME_1_NOIRE, R.raw.mi2, 1.0),
            new NotePartition("FA_GAMME_1","NOIRE",FA_GAMME_1_NOIRE, R.raw.fa2, 1.0),
            new NotePartition("FA_DIESE_GAMME_1","NOIRE",FA_DIESE_GAMME_1_NOIRE, R.raw.fad2, 1.0),
            new NotePartition("SOL_GAMME_1_NOIRE","NOIRE",SOL_GAMME_1_NOIRE, R.raw.sol2, 1.0),
            new NotePartition("SOL_DIESE_GAMME_1","NOIRE",SOL_DIESE_GAMME_1_NOIRE, R.raw.sold2, 1.0),
            new NotePartition("LA_GAMME_1","NOIRE",LA_GAMME_1_NOIRE, R.raw.la2, 1.0),
            new NotePartition("LA_DIESE_GAMME_1","NOIRE",LA_DIESE_GAMME_1_NOIRE, R.raw.lad2, 1.0),
            new NotePartition("SI_GAMME_1","NOIRE",SI_GAMME_1_NOIRE, R.raw.si2, 1.0),
    };

    /**
     * Nom du fichier partition créé par l'utilisateur.
     */
    String fileName;

    /**
     * Nom du chemin qui mène au stockage interne de l'application.
     */
    String path;

    /**
     * Nom de la partition que l'on aura créé.
     */
    File myFile;

    Boolean partitionRecuperee = false;

    Partition nouvellePartition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                    //on met à jour le type de la note
                    partition.getNotes().get(partition.getIndiceNoteCourante()).setTypeDeNote("CROCHE");

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

                    //on met à jour le type de la note
                    partition.getNotes().get(partition.getIndiceNoteCourante()).setTypeDeNote("NOIRE");

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

                    //on met à jour le type de la note
                    partition.getNotes().get(partition.getIndiceNoteCourante()).setTypeDeNote("BLANCHE");

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

                    //on met à jour le type de la note
                    partition.getNotes().get(partition.getIndiceNoteCourante()).setTypeDeNote("RONDE");

                    partition.setEnCoursDeModification(false);
                }
            }
        });

        listeLignesPartition = findViewById(R.id.partition_List_view);
        partition.afficher(listeLignesPartition, getApplicationContext());

        //CREATION DISPATCHER POUR RECUPERER LA FREQUENCE
        // Relie l'AudioDispatcher à l'entrée par défaut du smartphone (micro)
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        // Création d'un gestionnaire de détection de fréquence
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                if (partition.isWritting()) {
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
                if (!partition.isWritting()) {
                    if (Locale.getDefault().getLanguage() == "fr") {
                        btnDemarrageStopEcriture.setText("ARRÊTER L'ÉCRITURE");
                    }
                    else {
                        btnDemarrageStopEcriture.setText("STOP WRITING");
                    }
                    partition.setWritting(true);
                    btnRecupererPartition.setEnabled(false);
                }
                else {
                    ecritureTerminee = true;
                    dispatcher.stop();
                    audioThread.interrupt();
                    affichageNotes.arreter();
                    if (Locale.getDefault().getLanguage() == "fr") {
                        btnDemarrageStopEcriture.setText("DÉMARRER L'ÉCRITURE");
                    }
                    else {
                        btnDemarrageStopEcriture.setText("START WRITING");
                    }
                    btnRecupererPartition.setEnabled(true);
                    partition.setWritting(false);
                    partition.initialiserModificationPartition();
                    btnJouerPartition.setEnabled(true);
                    btnEnregistrerPartition.setEnabled(true);
                }

                if (ecritureTerminee) {
                    btnDemarrageStopEcriture.setEnabled(false); //empêcher de relancer l'enregistrement
                }
            }
        });


        this.btnJouerPartition = findViewById(R.id.btnJouerPartition);
        this.btnJouerPartition.setEnabled(false);
        this.btnJouerPartition.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!partition.isEnCoursDeModification() && !partitionEstEntrainDEtreJouee) {
                    lancerPartition();
                }
            }
        });

        // On crée un fichier qui correspond à l'emplacement extérieur
        this.btnEnregistrerPartition = findViewById(R.id.btnEnregistrerPartition);
        this.btnEnregistrerPartition.setEnabled(false);
        this.btnEnregistrerPartition.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View pView) {
                lesNotes = recupererNotes();
                if (laListeDesNotes != "listeVide") {
                    final AlertDialog alertDialog = new AlertDialog.Builder(EcriturePartition.this).create();
                    alertDialog.setTitle(getString(R.string.saisirNomPartition));
                    final EditText texteSaisie = new EditText(EcriturePartition.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    texteSaisie.setLayoutParams(layoutParams);
                    alertDialog.setView(texteSaisie);
                    alertDialog.setIcon(R.drawable.partition_icon);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                fileName = texteSaisie.getText().toString();
                                if(!fileName.isEmpty())
                                {
                                    path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                                    String leChemin = path + "/" + fileName;
                                    myFile = new File(leChemin);
                                    lesNotes = recupererNotes();

                                    FileOutputStream output = openFileOutput(fileName, MODE_PRIVATE);
                                    output.write(lesNotes.getBytes());
                                    if (output != null)
                                    {
                                        output.close();
                                    }

                                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                                            && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()))
                                    {
                                        // On crée un nouveau fichier. Si le fichier existe déjà, il ne sera pas créé
                                        myFile.createNewFile();
                                        output = new FileOutputStream(myFile);
                                        output.write(lesNotes.getBytes());
                                        if (output != null)
                                        {
                                            output.close();
                                        }

                                        Toast.makeText(getApplicationContext(), getString(R.string.partitionSuivanteCreee) + " " + fileName, Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), getString(R.string.impossibleCreerPartition), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else
                                    {
                                        Toast.makeText(getApplicationContext(), getString(R.string.nommerPartition), Toast.LENGTH_LONG).show();
                                    }
                                alertDialog.dismiss();
                            }
                            catch (FileNotFoundException e)
                            {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            catch (IOException e)
                            {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                    alertDialog.show();
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.aucuneNoteJouee), Toast.LENGTH_LONG).show();
                }
                Log.d("LOG","Les notes enregistrees : " + lesNotes);
            }
        });


        this.btnRecupererPartition = findViewById(R.id.btnRecupererPartition);
        this.btnRecupererPartition.setEnabled(true);
        this.btnRecupererPartition.setOnClickListener(new View.OnClickListener() {
            public void onClick(View pView) {
                path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                final File directory = new File(path);
                if (directory.listFiles() != null)
                {
                    File[] files = directory.listFiles();
                    String lesFichiers = null;
                    final String[] nomsFichiers = new String[files.length];
                    final CharSequence[] items = new CharSequence[files.length];
                    for (int i = 0; i < files.length; i++) {
                        lesFichiers = lesFichiers + files[i].getName() + "\n";
                        nomsFichiers[i] = files[i].getName();
                        items[i] = nomsFichiers[i];
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(EcriturePartition.this);
                    builder.setTitle(getString(R.string.choisirPartition));
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            try{
                                fileName = items[item].toString();
                                Partition partition = new Partition();
                                FileInputStream input = openFileInput(fileName);
                                int value;
                                int taillePartitionRecuperee = 1;
                                // On utilise un StringBuffer pour construire la chaîne au fur et à mesure
                                StringBuffer partitionLu = new StringBuffer();
                                String charCourant = null;
                                HashMap<Integer,String> mapDeNote = new HashMap<Integer,String>();
                                HashMap<Integer,String> mapDeDuree = new HashMap<Integer,String>();
                                HashMap<Integer,String> mapDeTypeDeNote = new HashMap<Integer,String>();
                                int i = 0;
                                int nbDeTiret = 0;
                                int nbDePointVirgule = 0;
                                int nbDeVirgule = 0;
                                int leNombreDeNote = 1;
                                int emplacementDernierTiret = 0;
                                int laTaille = 0;

                                int nbNotes = 0;
                                int nbTypeNote = 0;
                                int nbDuree = 0;

                                // On lit les caractères les uns après les autres
                                while((value = input.read()) != -1) {
                                    // On écrit dans le fichier le caractère partitionLu
                                    charCourant = partitionLu.append((char)value).toString();
                                    laTaille++;

                                    if (charCourant.endsWith(";")) {
                                        nbDePointVirgule++;
                                        taillePartitionRecuperee += 1;
                                        mapDeDuree.put(i,charCourant.substring(charCourant.length()-4,charCourant.length()-1));
                                        nbDuree++;
                                        i++;
                                    }

                                    if (charCourant.endsWith("_")) {
                                        nbDeTiret++;
                                        if (nbDeTiret == 4*leNombreDeNote)
                                        {
                                            //emplacementDernierTiret = charCourant.indexOf(charCourant.substring(charCourant.length()-2,charCourant.length()-1));
                                            emplacementDernierTiret = charCourant.length();
                                            if(taillePartitionRecuperee == 1)
                                            {
                                                mapDeNote.put(0,charCourant.substring(0,charCourant.length()-1)); nbNotes++;
                                                leNombreDeNote++;
                                            }
                                            else
                                            {/*
                                                int tailleTotaleAvantPointVirgule = 0;
                                                for (int j = 0 ; j < i ; j++)
                                                {
                                                    tailleTotaleAvantPointVirgule += mapDeNote.get(j).length() + mapDeTypeDeNote.get(j).length() + mapDeDuree.get(j).length() + nbDeTiret + nbDePointVirgule + nbDeVirgule;
                                                }*/
                                                mapDeNote.put(i,charCourant.substring(charCourant.indexOf(";",laTaille-1)+1,charCourant.length()-1)); nbNotes++;
                                                leNombreDeNote++;
                                            }
                                        }
                                    }

                                    if (charCourant.endsWith(",")){
                                        nbDeVirgule++;
                                        if (taillePartitionRecuperee == 1){
                                            mapDeTypeDeNote.put(0,charCourant.substring(0,charCourant.length()-1)); nbTypeNote++;
                                        }
                                        else
                                        {
                                            mapDeTypeDeNote.put(i,charCourant.substring(charCourant.indexOf("_",emplacementDernierTiret-1),charCourant.length()-1)); nbTypeNote++;
                                        }
                                    }
                                }

                                if ( (input.read() == -1) )
                                {
                                        mapDeDuree.put(i,charCourant.substring(charCourant.length()-3));
                                        nbDuree++;
                                }

                                if(input != null)
                                {
                                    input.close();
                                }

                                Log.d("LES NOTES", "toutes les notes:" + charCourant);

                                Log.d("LE LOG", "nbNote:"+nbNotes+" - nbTypeNote:"+nbTypeNote+" - nbDuree:"+nbDuree);


                                String messageNotesTot = "Les notes : \n";
                                String messageTypeDeNotesTot = "Les typesDeNotes : \n";
                                for (int lm = 0 ; lm < mapDeNote.size() ; lm++)
                                {
                                    messageNotesTot += mapDeNote.get(lm) + "\n";
                                    messageTypeDeNotesTot += mapDeTypeDeNote.get(lm) + "\n";
                                }
                                Log.d("Log notes tot", messageNotesTot);
                                Log.d("Log typeDeNotes tot", messageTypeDeNotesTot);

                                Toast.makeText(getApplicationContext(), "Taille partition : " + mapDeNote.size(), Toast.LENGTH_LONG).show();

                                // Creer nouvelle partition

                                int nombreNotes = 0;
                                nouvellePartition = new Partition();
                                listeLignesPartition = findViewById(R.id.partition_List_view);
                                nouvellePartition.afficher(listeLignesPartition, getApplicationContext());

                                for (int noteCourante = 0 ; noteCourante <= mapDeNote.size() ; noteCourante++)
                                {
                                    for(int noteConnu = 0 ; noteConnu <= listeNotes.length-1 ; noteConnu++)
                                    {
                                        if (listeNotes[noteConnu].getNomNote().equals(mapDeNote.get(noteCourante)))
                                        {
                                            nombreNotes++;
                                            nouvellePartition.ajouterNote(listeNotes[noteConnu],listeLignesPartition,getApplicationContext());
                                            listeNotes[noteConnu].setDuree(Double.parseDouble(mapDeDuree.get(noteCourante)));
                                            changerDuree(listeNotes[noteConnu]);
                                        }
                                    }
                                }

                                btnJouerPartition.setEnabled(true);
                                partitionRecuperee = true;
/*
                                Toast.makeText(getApplicationContext(), "Taille partition après " + nouvellePartition.getNotes().size(), Toast.LENGTH_LONG).show();

                                Log.d("Taille", "La taille de mapDeNote : " + mapDeNote.size());
                                Log.d("Log1", "nbNotes : " + nombreNotes);
                                Log.d("Log2", "listeNotes.length-1 : " + listeNotes.length);

                                String message = "Les notes : \n";
                                for (int lm = 0 ; lm < nouvellePartition.getNotes().size() ; lm++)
                                {
                                    message += nouvellePartition.getNotes().get(lm).getNomNote() + "\n";
                                }
                                Log.d("Log notes", message);
*/

                                dialog.dismiss();
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), getString(R.string.auncunePartitionEnregistrees), Toast.LENGTH_LONG).show();
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
            List<NotePartition> liste;

            if (partitionRecuperee)
            {
                liste = nouvellePartition.getNotes();
            }
            else
            {
                liste = partition.getNotes();
            }

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

    private static String convertirTableauDeStringEnString(String[] leTab, String delimiteur)
    {
        StringBuilder sb = new StringBuilder();
        for(String leString : leTab)
        {
            sb.append(leString).append(delimiteur);
        }
        return sb.substring(0, sb.length() - 1);

    }

    public String recupererNotes()
    {
        List<NotePartition> listeNotesPartition = partition.getNotes();
        NotePartition[] noteCourante = new NotePartition[listeNotesPartition.size()];
        String[] tabDesNotes = new String[listeNotesPartition.size()];
        if (!listeNotesPartition.isEmpty()){
            for(int i = 0; i < listeNotesPartition.size() ; i++)
            {
                    noteCourante[i] = listeNotesPartition.get(i);
                    tabDesNotes[i] = noteCourante[i].getNomNote() + "," + noteCourante[i].getDuree();
            }
            laListeDesNotes = convertirTableauDeStringEnString(tabDesNotes,";");
        }
        return laListeDesNotes;
    }

    private HashMap<String, Double> mapDesNotesAvecDurees = new HashMap<String, Double>();

    public void changerDuree(NotePartition notePartition)
    {
        mapDesNotesAvecDurees.put("CROCHE",0.5);
        mapDesNotesAvecDurees.put("NOIRE",1.0);
        mapDesNotesAvecDurees.put("BLANCHE",2.0);
        mapDesNotesAvecDurees.put("RONDE",4.0);

        for (HashMap.Entry mapentry : mapDesNotesAvecDurees.entrySet()) {
            if (mapentry.getValue().equals(notePartition.getDuree()))
            {
                notePartition.setTypeDeNote(mapentry.getKey().toString());
            }
        }
    }
}
