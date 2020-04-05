package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import android.widget.ImageView;
import iutbayonne.projet.zicall.ApprentissagePianoPackage.ClavierPianoPackage.Touche;
import iutbayonne.projet.zicall.R;

public class NoteMelodie
{
    private float duree;

    /**
     * Identifiant du fichier audio correspondant au son de la note.
     */
    private int audioPianoNote;

    /**
     * Touche du clavier du piano correspondant à la note.
     */
    private Touche touche;

    public NoteMelodie(float duree, int audioPianoNote, Touche touche)
    {
        this.duree = duree;
        this.audioPianoNote = audioPianoNote;
        this.touche = touche;
    }

    public float getDuree()
    {
        return duree;
    }

    public float getDureeActiveEnMillisecondes()
    {
        return getDuree() * 1000;
    }

    public void setDuree(float duree)
    {
        this.duree = duree;
    }

    public Touche getTouche()
    {
        return touche;
    }

    public void setTouche(Touche touche) {
        this.touche = touche;
    }

    public int getAudioPianoNote()
    {
        return audioPianoNote;
    }

    public void setAudioPianoNote(int audioPianoNote)
    {
        this.audioPianoNote = audioPianoNote;
    }

    public void allumerTouche(ImageView image)
    {
        image.setImageResource(R.drawable.touche_piano_allumee);
    }

    public void eteindreTouche(ImageView imageTouche)
    {
        if(getTouche().isNoire())
        {
            imageTouche.setImageResource(R.drawable.touche_noire);
        }
        else
            {
            imageTouche.setImageResource(R.drawable.touche_blanche);
        }
    }
}
