package iutbayonne.projet.zicall;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ApprentissagePiano extends AppCompatActivity {

    // Délai d'attente arbitraire en ms au cas où l'on joue 2 fois d'affilée la même touche
    private final int DELAI_ATTENTE = 50;

    private static final String LOG_TAG = "PianoTest";
    private ImageButton start;
    private final Note[] notesBellaCiao = {
            new Note(Clavier.MI_GAMME_2, (float) 0.5),
            new Note(Clavier.LA_GAMME_2, (float) 0.5),
            new Note(Clavier.SI_GAMME_2, (float) 0.5),
            new Note(Clavier.DO_GAMME_2, (float) 0.5),
            new Note(Clavier.LA_GAMME_2, 2),
            new Note(Clavier.MI_GAMME_2, (float) 0.5),
            new Note(Clavier.LA_GAMME_2, (float) 0.5),
            new Note(Clavier.SI_GAMME_2, (float) 0.5),
            new Note(Clavier.DO_GAMME_2, (float) 0.5),
            new Note(Clavier.LA_GAMME_2, 2),
            new Note(Clavier.MI_GAMME_2, (float) 0.5),
            new Note(Clavier.LA_GAMME_2, (float) 0.5),
            new Note(Clavier.SI_GAMME_2, (float) 0.5),
            new Note(Clavier.DO_GAMME_2, 1),
            new Note(Clavier.SI_GAMME_2, (float) 0.5),
            new Note(Clavier.LA_GAMME_2, (float) 0.5),
            new Note(Clavier.DO_GAMME_2, 1),
            new Note(Clavier.SI_GAMME_2, (float) 0.5),
            new Note(Clavier.LA_GAMME_2, (float) 0.5),
            new Note(Clavier.MI_GAMME_2, 1),
            new Note(Clavier.MI_GAMME_2, 1),
            new Note(Clavier.MI_GAMME_2, 1),
            new Note(Clavier.RE_GAMME_2, (float) 0.5),
            new Note(Clavier.MI_GAMME_2, (float) 0.5),
            new Note(Clavier.FA_GAMME_2, (float) 0.5),
            new Note(Clavier.FA_GAMME_2, 2),
            new Note(Clavier.FA_GAMME_2, (float) 0.5),
            new Note(Clavier.MI_GAMME_2, (float) 0.5),
            new Note(Clavier.RE_GAMME_2, (float) 0.5),
            new Note(Clavier.FA_GAMME_2, (float) 0.5),
            new Note(Clavier.MI_GAMME_2, 2),
            new Note(Clavier.MI_GAMME_2, (float) 0.5),
            new Note(Clavier.RE_GAMME_2, (float) 0.5),
            new Note(Clavier.DO_GAMME_2, (float) 0.5),
            new Note(Clavier.SI_GAMME_2, 1),
            new Note(Clavier.MI_GAMME_2, 1),
            new Note(Clavier.DO_GAMME_2, 1),
            new Note(Clavier.SI_GAMME_2, 1),
            new Note(Clavier.LA_GAMME_2, 2)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentissage_piano);
        this.start = findViewById(R.id.start);
        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new ApprentissagePiano.JouerMelodie();
            }
        });

    }

    private class JouerMelodie extends Thread
    {

        public JouerMelodie()
        {
            start();
        }

        public void run()
        {
            /* Désactive le bouton au lancement de la mélodie pour éviter
            de lancer plusieurs fois la mélodie en même temps */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    start.setEnabled(false);
                }
            });

            /* Délai d'attente à prévoir si l'on joue 2 fois d'affilée la même touche.
            Ici, il ne nous sert pas encore car nous avons pour l'instant qu'une seule chanson (Bella Ciao)
            où aucune touche ne se répète, mais cela sera utile si l'on décide d''ajouter des chansons. */
            int attente;
            for(int i = 0; i < notesBellaCiao.length; i++)
            {
                runOnUiThread(new ApprentissagePiano.ActiverTouche(notesBellaCiao[i].getTouche().getIdImage()));

                attente = 0;

                // Pour ne pas comparer à la touche suivante si l'on est déjà sur la dernière
                if(i < notesBellaCiao.length - 1)
                {
                    if(notesBellaCiao[i].getTouche() == notesBellaCiao[i+1].getTouche())
                    {
                        attente = DELAI_ATTENTE;
                    }
                }

                // Temps pendant lequel la touche reste activée
                try
                {
                    Thread.sleep( (long)(notesBellaCiao[i].getDuree() * 1000 - attente) );
                }
                catch(InterruptedException ie)
                {
                    Log.e(LOG_TAG, "Échec du Thread.sleep()");
                }

                runOnUiThread(new ApprentissagePiano.DesactiverTouche(notesBellaCiao[i].getTouche().getIdImage(),
                        notesBellaCiao[i].getTouche().estNoire()));

                // Temps pendant lequel la touche reste désactivée
                try
                {
                    Thread.sleep(attente);
                }
                catch(InterruptedException ie)
                {
                    Log.e(LOG_TAG, "Échec du Thread.sleep(attente)");
                }
            }

            Log.e(LOG_TAG, "Fin de la mélodie");

            // Réactivation du bouton
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    start.setEnabled(true);
                }
            });
        }

    }

    private class ActiverTouche implements Runnable
    {
        private int idImage;

        public ActiverTouche(int idImage)
        {
            this.idImage = idImage;
        }

        public void run()
        {
            ImageView touche = findViewById(this.idImage);
            touche.setImageResource(R.drawable.touche_piano_allumee);
            Log.e(LOG_TAG, "Activation d'une touche");
        }
    }

    private class DesactiverTouche implements Runnable
    {
        private int idImage;
        private boolean estNoire;

        public DesactiverTouche(int idImage, boolean estNoire)
        {
            this.idImage = idImage;
            this.estNoire = estNoire;
        }

        public void run()
        {
            ImageView touche = findViewById(this.idImage);
            touche.setImageResource(
                    this.estNoire ? R.drawable.touche_noire : R.drawable.touche_piano );
            Log.e(LOG_TAG, "Desactivation d'une touche");
        }
    }

}
