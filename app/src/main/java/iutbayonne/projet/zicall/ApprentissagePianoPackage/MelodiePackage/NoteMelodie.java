package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import android.widget.ImageView;

import iutbayonne.projet.zicall.ApprentissagePianoPackage.ClavierPianoPackage.Touche;
import iutbayonne.projet.zicall.R;

public class NoteMelodie{
    private float duree;
    private int audioPianoNote;
    private Touche touche;

    public NoteMelodie(float duree, int audioPianoNote, Touche touche) {
        this.duree = duree;
        this.audioPianoNote = audioPianoNote;
        this.touche = touche;
    }

    public float getDuree() {
        return duree;
    }

    public float getDureeActiveEnMillisecondes(){
        return getDuree() * 1000;
    }

    public void setDuree(float duree) {
        this.duree = duree;
    }

    public Touche getTouche() {
        return touche;
    }

    public void setTouche(Touche touche) {
        this.touche = touche;
    }

    public int getAudioPianoNote() {
        return audioPianoNote;
    }

    public void setAudioPianoNote(int audioPianoNote) {
        this.audioPianoNote = audioPianoNote;
    }

    public void allumerTouche(ImageView image){
        image.setImageResource(R.drawable.touche_piano_allumee);
    }

    public void eteindreTouche(ImageView imageTouche){
        if (getTouche().isNoire()) {
            imageTouche.setImageResource(R.drawable.touche_noire);
        }
        else{
            imageTouche.setImageResource(R.drawable.touche_blanche);
        }
    }
}
