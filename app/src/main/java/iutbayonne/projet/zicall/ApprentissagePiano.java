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
    private ImageButton btnLancerMelodie;
    private ImageButton btnArreterMelodie;
    private JouerMelodie joueurDeMelodie;
    private TextView titreMelodie;
    private TextView informationsMelodie;
    private static MediaPlayer audioTouche;

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
        this.informationsMelodie = findViewById(R.id.informationsMelodie);
        this.informationsMelodie.setText(melodie.getInformationsSupplementaires());

        this.joueurDeMelodie = null;
    }

    public void lancerMelodie(View view) {
        joueurDeMelodie = new JouerMelodie(melodie);
    }

    public void arreterMelodie(View view)
    {
        joueurDeMelodie.setDoitMourrir(true);
    }

    private class JouerMelodie extends Thread
    {
        private Melodie melodie;
        private boolean doitMourrir;
        private ActiverTouche activeurTouche;
        private DesactiverTouche desactiveurTouche;

        public JouerMelodie(Melodie melodie)
        {
            this.melodie = melodie;
            this.doitMourrir = false;
            this.activeurTouche = new ActiverTouche(null, null);
            this.desactiveurTouche = new DesactiverTouche(null, null);
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
            this.activeurTouche.setNote(noteCourante);
            this.activeurTouche.setAudioTouche(audioTouche);
            runOnUiThread(activeurTouche);

            attendre(melodie.getDurreNoteReelle(noteCourante));

            this.desactiveurTouche.setNote(noteCourante);
            this.desactiveurTouche.setAudioTouche(activeurTouche.audioTouche);
            runOnUiThread(desactiveurTouche);
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
        private MediaPlayer audioTouche;

        public ActiverTouche(NoteMelodie note, MediaPlayer audioTouche)
        {
            this.note = note;
            this.audioTouche = audioTouche;
        }

        public void run()
        {
            ImageView touche = findViewById(note.getTouche().getIdImage());
            note.allumerTouche(touche);
            audioTouche = MediaPlayer.create(getApplicationContext(), note.getAudioPianoNote());
            audioTouche.start();
        }

        public void setNote(NoteMelodie note) {
            this.note = note;
        }

        public void setAudioTouche(MediaPlayer audioTouche) {
            this.audioTouche = audioTouche;
        }
    }

    private class DesactiverTouche implements Runnable
    {
        private NoteMelodie note;
        private MediaPlayer audioTouche;

        public DesactiverTouche(NoteMelodie note, MediaPlayer audioTouche)
        {
            this.note = note;
            this.audioTouche = audioTouche;
        }

        public void run()
        {
            ImageView touche = findViewById(note.getTouche().getIdImage());
            note.eteindreTouche(touche);
            audioTouche.stop();
            audioTouche.release();
            audioTouche = null;
        }

        public void setNote(NoteMelodie note) {
            this.note = note;
        }

        public void setAudioTouche(MediaPlayer audioTouche) {
            this.audioTouche = audioTouche;
        }
    }

    public void accederChoixMelodie(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_melodie_entrainement_piano.class);
        startActivity(otherActivity);
    }

    public void accederAccueil(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), MainActivity.class);
        // Vide la pile des activity
        otherActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if(joueurDeMelodie != null)
        {
            arreterMelodie(null);
        }

        startActivity(otherActivity);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        if(joueurDeMelodie != null)
        {
            arreterMelodie(null);
        }

        super.onBackPressed();
    }
}