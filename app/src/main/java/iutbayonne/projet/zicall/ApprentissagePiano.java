package iutbayonne.projet.zicall;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.ClavierPianoPackage.Touche;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.Melodie;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage.NoteMelodie;

public class ApprentissagePiano extends AppCompatActivity
{
    /**
     * Mélodie à jouer pour apprendre à jouer du piano.
     */
    private Melodie melodie;

    private ImageView btnLancerMelodie;
    private ImageView btnArreterMelodie;

    /**
     * Thread chargé de jouer la mélodie en illuminant les touches du clavier.
     */
    private JouerMelodie joueurDeMelodie;

    private TextView titreMelodie;

    /**
     * @deprecated Cette variable n'est plus utilisée. Elle doit être supprimée.
     */
    private static MediaPlayer audioTouche;

    private double vitesseMelodie;

    /**
     * Lien vers le bouton qui permet de changer la vitesse de mélodie jouée.
     */
    private Spinner s_vitesse;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentissage_piano);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Zic'All</font>"));

        this.btnLancerMelodie = findViewById(R.id.btnLancerMelodie);
        this.btnArreterMelodie = findViewById(R.id.btnArreterMelodie);
        this.s_vitesse = findViewById(R.id.S_vitesse);
        this.titreMelodie = findViewById(R.id.titreMelodie);

        this.btnArreterMelodie.setEnabled(false);

        this.melodie = Melodie.BELLA_CIAO;
        this.titreMelodie.setText(melodie.getTitreMelodie());

        s_vitesse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0:
                        vitesseMelodie = 1.0;
                        break;

                    case 1:
                        vitesseMelodie = 0.5;
                        break;

                    case 2:
                        vitesseMelodie = 0.25;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        this.joueurDeMelodie = null;
    }

    public void lancerMelodie(View view)
    {
        joueurDeMelodie = new JouerMelodie(melodie);
    }

    public void arreterMelodie(View view)
    {
        joueurDeMelodie.setDoitMourrir(true);
    }

    /**
     * Thread chargé de jouer la mélodie en illuminant les touches du clavier.
     */
    private class JouerMelodie extends Thread
    {
        /**
         * Mélodie que le Thread doit jouer.
         */
        private Melodie melodie;

        /**
         * Indique si le Thread doit s'arreter ou non.
         */
        private boolean doitMourrir;

        /**
         * Action éxcutée dans le Thread permettant d'illuminer les différentes touches et joue les sons associés.
         */
        private ActiverTouche activeurTouche;

        /**
         * Action éxcutée dans le Thread permettant d'éteindre les différentes touches et de couper les sons associés.
         */
        private DesactiverTouche desactiveurTouche;

        public JouerMelodie(Melodie melodie)
        {
            this.melodie = melodie;
            this.doitMourrir = false;
            this.activeurTouche = new ActiverTouche(null, null);
            this.desactiveurTouche = new DesactiverTouche(null, null);
            start();
        }

        /**
         * S'exécute au lancement du Thread.
         */
        public void run()
        {
            desactiverBouton(btnLancerMelodie);
            activerBouton(btnArreterMelodie);
            jouerToutesLesNotes();
            activerBouton(btnLancerMelodie);
            desactiverBouton(btnArreterMelodie);
        }

        public void jouerToutesLesNotes()
        {
            NoteMelodie noteCourante;
            for(int nbCouplet = 0; nbCouplet<6; nbCouplet++)
            {
                for (int i = 0; i < melodie.getNotesMelodies().length; i++)
                {
                    if (!this.doitMourrir())
                    {
                        noteCourante = melodie.getNotesMelodies()[i];

                        jouerNote(noteCourante);

                        attendreDelaiEntreDeuxNotesIdentiques(i, noteCourante);
                    }
                }
            }
        }

        public void jouerNote(NoteMelodie noteCourante)
        {
            this.activeurTouche.setNote(noteCourante);
            this.activeurTouche.setAudioTouche(audioTouche);
            runOnUiThread(activeurTouche);

            attendre((float)(melodie.getDurreNoteReelle(noteCourante)/vitesseMelodie));

            this.desactiveurTouche.setNote(noteCourante);
            this.desactiveurTouche.setAudioTouche(activeurTouche.audioTouche);
            runOnUiThread(desactiveurTouche);
        }

        public void attendre(float duree)
        {
            try
            {
                Thread.sleep((long) duree);
            }
            catch(InterruptedException ie)
            {}
        }

        /**
         * Méthode appelée lorsque deux notes consécutives sont identiques. Elle laisse le temps à
         * l'utilisateur de comprendre qu'il faut jouer deux fois la même touche car illuminer de
         * façon consécutive deux fois la même touche n'est pas perceptible par l'oeil humain.
         * @param i Indice de la note suivante dans les notes de la mélodie.
         */
        public void attendreDelaiEntreDeuxNotesIdentiques(int i, NoteMelodie noteCourante)
        {
            int indiceDerniereNoteDeLaMelodie = melodie.getNotesMelodies().length - 1;
            if(i < indiceDerniereNoteDeLaMelodie)
            {
                Touche toucheSuivante = melodie.getNotesMelodies()[i+1].getTouche();
                if(noteCourante.getTouche() == toucheSuivante)
                {
                    attendre(50);
                }
            }
        }

        /**
         * @deprecated Cette méthode n'est plus utlisée. Elle doit être supprimée.
         */
        public void attendreDebutChant(double secondes){
            try{
                Thread.sleep((long)secondes*1000);
            }catch(InterruptedException ie){}
        }

        public void desactiverBouton(final ImageView bouton){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bouton.setEnabled(false);
                }
            });
        }

        public void activerBouton(final ImageView bouton){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bouton.setEnabled(true);
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

    /**
     * Action à exécuter dans le Thread JouerMelodie qui permet d'illuminer une touche et joue le son associé.
     */
    private class ActiverTouche implements Runnable
    {
        /**
         * Note que l'on souhaite activer.
         */
        private NoteMelodie note;

        /**
         * Permet de manipuler le fichier audio de la note correspondante.
         */
        private MediaPlayer audioTouche;

        public ActiverTouche(NoteMelodie note, MediaPlayer audioTouche)
        {
            this.note = note;
            this.audioTouche = audioTouche;
        }

        /**
         * Méthode à exécuter à la création du Runnable.
         */
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

    /**
     * Action à exécuter dans le Thread JouerMelodie qui permet d'éteindre une touche et de couper le son associé.
     */
    private class DesactiverTouche implements Runnable
    {
        /**
         * Note que l'on souhaite désactiver.
         */
        private NoteMelodie note;

        /**
         * Permet de manipuler le fichier audio de la note correspondante.
         */
        private MediaPlayer audioTouche;

        public DesactiverTouche(NoteMelodie note, MediaPlayer audioTouche)
        {
            this.note = note;
            this.audioTouche = audioTouche;
        }

        /**
         * Méthode à exécuter à la création du Runnable.
         */
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

    public void accederChoixMelodie(View view)
    {
        onBackPressed();
    }

    public void accederAccueil()
    {
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

    /**
     * Méthode qui s'exécute lorsque l'on appuye sur le bouton "Retour" du smartphone.
     * Surchargée afin d'arrêter proprement le Thread de l'activity en cours avant de la quitter.
     */
    @Override
    public void onBackPressed()
    {
        if(joueurDeMelodie != null)
        {
            arreterMelodie(null);
        }

        super.onBackPressed();
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
}