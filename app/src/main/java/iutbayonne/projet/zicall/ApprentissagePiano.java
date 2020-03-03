package iutbayonne.projet.zicall;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import iutbayonne.projet.zicall.ApprentissagePianoPackage.ClavierPianoPackage.Touche;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.Choix_melodie_entrainement_piano;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.Melodie;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.NoteMelodie;

public class ApprentissagePiano extends AppCompatActivity {

    private Melodie melodie;
    private MediaPlayer audioMelodie;
    private ImageButton btnLancerMelodie;
    private ImageButton btnArreterMelodie;
    private JouerMelodie joueurDeMelodie;
    private TextView titreMelodie;
    private TextView informatonsMelodie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentissage_piano);

        this.melodie = Melodie.BELLA_CIAO;
        this.btnLancerMelodie = findViewById(R.id.btnLancerMelodie);
        this.btnArreterMelodie = findViewById(R.id.btnArreterMelodie);
        this.btnArreterMelodie.setEnabled(false);
        this.titreMelodie = findViewById(R.id.titreMelodie);
        this.titreMelodie.setText(melodie.getTitreMelodie());
        this.informatonsMelodie = findViewById(R.id.informationsMelodie);
        this.informatonsMelodie.setText(melodie.getInformationsSupplementaires());
    }

    public void lancerMelodie(View view) {
        joueurDeMelodie = new JouerMelodie(melodie);
        audioMelodie = MediaPlayer.create(getApplicationContext(), this.melodie.getAudioMelodie());
        joueurDeMelodie.lancerAudioMelodie();
    }

    public void arreterMelodie(View view) {
        joueurDeMelodie.setDoitMourrir(true);
        joueurDeMelodie.arreterAudioMelodie();
    }

    private class JouerMelodie extends Thread
    {
        private Melodie melodie;
        private boolean doitMourrir;

        public JouerMelodie(Melodie melodie)
        {
            this.melodie = melodie;
            this.doitMourrir = false;
            start();
        }

        public void run()
        {
            desactiverBouton(btnLancerMelodie);
            activerBouton(btnArreterMelodie);
            attendreDebutChant(melodie.getAttenteDebutChant());
            jouerToutesLesNotes();
            activerBouton(btnLancerMelodie);
            desactiverBouton(btnArreterMelodie);
        }

        public void lancerAudioMelodie(){
            audioMelodie.start();
        }

        public void arreterAudioMelodie(){
            audioMelodie.stop();
        }

        public void jouerToutesLesNotes(){
            NoteMelodie noteCourante;
            for(int nbCouplet = 0; nbCouplet<6; nbCouplet++) {
                for (int i = 0; i < melodie.getNotesMelodies().length; i++) {
                    if (!this.doitMourrir()) {
                        noteCourante = melodie.getNotesMelodies()[i];

                        jouerNote(noteCourante);

                        attendreDelaiEntreDeuxNotesIdentiques(i, noteCourante);
                    }
                }
            }
        }

        public void jouerNote(NoteMelodie noteCourante){
            runOnUiThread(new ApprentissagePiano.ActiverTouche(noteCourante));

            attendre(melodie.getDurreNoteReelle(noteCourante));

            runOnUiThread(new ApprentissagePiano.DesactiverTouche(noteCourante));
        }

        public void attendre(float duree){
            try{
                Thread.sleep((long) duree);
            }catch(InterruptedException ie){}
        }

        public void attendreDelaiEntreDeuxNotesIdentiques(int i, NoteMelodie noteCourante){
            int indiceDerniereNoteDeLaMelodie = melodie.getNotesMelodies().length - 1;
            if(i < indiceDerniereNoteDeLaMelodie){
                Touche toucheSuivante = melodie.getNotesMelodies()[i+1].getTouche();
                if(noteCourante.getTouche() == toucheSuivante){
                    attendre(50);
                }
            }
        }

        public void attendreDebutChant(double secondes){
            try{
                Thread.sleep((long)secondes*1000);
            }catch(InterruptedException ie){}
        }

        public void desactiverBouton(final ImageButton boutton){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boutton.setEnabled(false);
                }
            });
        }

        public void activerBouton(final ImageButton boutton){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boutton.setEnabled(true);
                }
            });
        }

        public boolean doitMourrir() {
            return doitMourrir;
        }

        public void setDoitMourrir(boolean doitMourrir) {
            this.doitMourrir = doitMourrir;
        }
    }

    private class ActiverTouche implements Runnable
    {
        private NoteMelodie note;

        public ActiverTouche(NoteMelodie note)
        {
            this.note = note;
        }

        public void run()
        {
            ImageView touche = findViewById(note.getTouche().getIdImage());
            note.allumerTouche(touche);
        }
    }

    private class DesactiverTouche implements Runnable
    {
        private NoteMelodie note;

        public DesactiverTouche(NoteMelodie note)
        {
            this.note = note;
        }

        public void run()
        {
            ImageView touche = findViewById(note.getTouche().getIdImage());
            note.eteindreTouche(touche);
        }
    }

    public void accerderChoixMelodie(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_melodie_entrainement_piano.class);
        startActivity(otherActivity);
        finish();
    }


    public void accederAccueil(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(otherActivity);
        finish();
    }
}
