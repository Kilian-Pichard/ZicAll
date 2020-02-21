package iutbayonne.projet.zicall;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "PianoTest";
    private Button start;
    private ImageView touche;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.start = findViewById(R.id.start);

        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new JouerMelodie();
            }
        });

    }

    private void activerTouche()
    {
        this.touche.setImageResource(R.drawable.touche_piano_allumee);
        Log.e(LOG_TAG, "Activation d'une touche");
    }

    private void desactiverTouche(boolean estNoire)
    {
        this.touche.setImageResource(
                estNoire ? R.drawable.touche_noire : R.drawable.touche_piano );
        Log.e(LOG_TAG, "Desactivation d'une touche");
    }

    public class JouerMelodie extends Thread
    {

        public JouerMelodie()
        {
            start();
        }

        public void run()
        {

            Note[] notesBellaCiao = {
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

            for(int i = 0; i < notesBellaCiao.length; i++)
            {
                touche = findViewById(notesBellaCiao[i].getTouche().getIdImage());

                activerTouche();

                try
                {
                    Thread.sleep( (long)(notesBellaCiao[i].getDuree() * 1000) );
                }
                catch(InterruptedException ie)
                {
                    Log.e(LOG_TAG, "Échec du Thread.sleep()");
                }

                desactiverTouche(notesBellaCiao[i].getTouche().estNoire());
            }

            Log.e(LOG_TAG, "Fin de la mélodie");

        }

    }

}