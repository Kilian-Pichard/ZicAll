package iutbayonne.projet.zicall;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import iutbayonne.projet.zicall.ApprentissagePianoPackage.ClavierPianoPackage.Touche;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.Melodie;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.NoteMelodie;

public class ApprentissagePiano extends AppCompatActivity {

    private Melodie melodie;
    private MediaPlayer audioMelodie;

    private static final String LOG_TAG = "PianoTest";
    private ImageButton btnLancerMelodie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentissage_piano);

        this.melodie = Melodie.BELLA_CIAO;
        this.btnLancerMelodie = findViewById(R.id.lancerMelodie);
        this.audioMelodie = MediaPlayer.create(getApplicationContext(), melodie.getAudioMelodie());
    }

    public void lancerMelodie(View view) {
        new JouerMelodie(melodie);
    }

    private class JouerMelodie extends Thread
    {

        private Melodie melodie;

        public JouerMelodie(Melodie melodie)
        {
            this.melodie = melodie;
            start();
        }

        public void run()
        {
            desactiverBtnPlay();

            NoteMelodie noteCourante;
            for(int i = 0; i < melodie.getNotesMelodies().length; i++)
            {
                noteCourante = melodie.getNotesMelodies()[i];

                jouerNote(noteCourante);

                attendreDelaiEntreDeuxNotesIdentiques(i, noteCourante);
            }

            activerBtnPlay();
        }

        public void jouerNote(NoteMelodie noteCourante){
            runOnUiThread(new ApprentissagePiano.ActiverTouche(noteCourante));

            attendre(noteCourante.getDureeActiveEnMillisecondes());

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

        public void desactiverBtnPlay(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnLancerMelodie.setEnabled(false);
                }
            });
        }

        public void activerBtnPlay(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnLancerMelodie.setEnabled(true);
                }
            });
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
}
