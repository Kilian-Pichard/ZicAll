package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import android.content.Context;
import android.media.MediaPlayer;

import iutbayonne.projet.zicall.R;

public enum Melodie{
    BELLA_CIAO("Bella ciao", "Chant des partisans italiens \n interprété par Samuel et Thomas Nadal", R.raw.bella_ciao, EnsembleNotesMelodies.NOTES_BELLA_CIAO);

    private String titreMelodie;
    private String informationsSupplementaires;
    private int audioMelodie;
    private EnsembleNotesMelodies notesMelodies;

    Melodie(String titreMelodie, String informationsSupplementaires, int audioMelodie, EnsembleNotesMelodies notesMelodies) {
        this.titreMelodie = titreMelodie;
        this.informationsSupplementaires = informationsSupplementaires;
        this.audioMelodie = audioMelodie;
        this.notesMelodies = notesMelodies;
    }

    public String getTitreMelodie() {
        return titreMelodie;
    }

    public void setTitreMelodie(String titreMelodie) {
        this.titreMelodie = titreMelodie;
    }

    public String getInformationsSupplementaires() {
        return informationsSupplementaires;
    }

    public void setInformationsSupplementaires(String informationsSupplementaires) {
        this.informationsSupplementaires = informationsSupplementaires;
    }

    public int getAudioMelodie() {
        return audioMelodie;
    }

    public void setAudioMelodie(int audioMelodie) {
        this.audioMelodie = audioMelodie;
    }

    public NoteMelodie[] getNotesMelodies() {
        return notesMelodies.getEnsembleNotesMelodie();
    }

    public void setNotesMelodies(EnsembleNotesMelodies notesMelodies) {
        this.notesMelodies = notesMelodies;
    }

    public void jouerAudioMelodie(Melodie melodie, Context context){
        MediaPlayer audioMelodie = MediaPlayer.create(context, melodie.getAudioMelodie());
        audioMelodie.start();
    }
}
