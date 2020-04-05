package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import iutbayonne.projet.zicall.R;

public enum Melodie
{
    BELLA_CIAO("Bella ciao", "Chant des partisans italiens \n interprété par Samuel et Thomas Nadal", R.raw.bella_ciao, EnsembleNotesMelodies.NOTES_BELLA_CIAO, 0.6, 1);

    private String titreMelodie;
    private String informationsSupplementaires;

    /**
     *  Identifiant du fichier audio dans les ressources du projet.
     */
    private int audioMelodie;

    private EnsembleNotesMelodies notesMelodies;

    /**
     * @deprecated Cet attribut n'est plus utlisé. Il doit être supprimé.
     */
    private double multiplicateurDureeNotesReelle;

    /**
     * @deprecated Cet attribut n'est plus utlisé. Il doit être supprimé.
     */
    private double attenteDebutChant;

    Melodie(String titreMelodie, String informationsSupplementaires, int audioMelodie, EnsembleNotesMelodies notesMelodies, double multiplicateurDureeNotesReelle, double attenteDebutChant)
    {
        this.titreMelodie = titreMelodie;
        this.informationsSupplementaires = informationsSupplementaires;
        this.audioMelodie = audioMelodie;
        this.notesMelodies = notesMelodies;
        this.multiplicateurDureeNotesReelle = multiplicateurDureeNotesReelle;
        this.attenteDebutChant = attenteDebutChant;
    }

    public String getTitreMelodie()
    {
        return titreMelodie;
    }

    public String getInformationsSupplementaires()
    {
        return informationsSupplementaires;
    }

    public int getAudioMelodie()
    {
        return audioMelodie;
    }

    public NoteMelodie[] getNotesMelodies()
    {
        return notesMelodies.getEnsembleNotesMelodie();
    }

    public double getMultiplicateurDureeNotesReelle()
    {
        return multiplicateurDureeNotesReelle;
    }

    /**
     * @deprecated Cette méthode n'est plus utlisée. Elle doit être supprimée.
     */
    public double getAttenteDebutChant()
    {
        return attenteDebutChant;
    }

    public long getDurreNoteReelle(NoteMelodie note)
    {
        return (long)(note.getDureeActiveEnMillisecondes()*getMultiplicateurDureeNotesReelle());
    }
}
