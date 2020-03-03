package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import iutbayonne.projet.zicall.ApprentissagePianoPackage.ClavierPianoPackage.Clavier;

public enum EnsembleNotesMelodies {
    NOTES_BELLA_CIAO( new NoteMelodie[]{new NoteMelodie((float) 0.5, Clavier.MI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.SI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.DO_GAMME_2),
                                        new NoteMelodie( 2, Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.MI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.SI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.DO_GAMME_2),
                                        new NoteMelodie( 2, Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.MI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.SI_GAMME_1),
                                        new NoteMelodie( 1, Clavier.DO_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.SI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.LA_GAMME_1),
                                        new NoteMelodie( 1, Clavier.DO_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.SI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, Clavier.LA_GAMME_1),
                                        new NoteMelodie( 1, Clavier.MI_GAMME_2),
                                        new NoteMelodie( 1, Clavier.MI_GAMME_2),
                                        new NoteMelodie( 1, Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.RE_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.FA_GAMME_2),
                                        new NoteMelodie( 2, Clavier.FA_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.FA_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.RE_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.FA_GAMME_2),
                                        new NoteMelodie( 2, Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.RE_GAMME_2),
                                        new NoteMelodie( (float) 0.5, Clavier.DO_GAMME_2),
                                        new NoteMelodie( 1, Clavier.SI_GAMME_1),
                                        new NoteMelodie( 1, Clavier.MI_GAMME_2),
                                        new NoteMelodie( 1, Clavier.DO_GAMME_2),
                                        new NoteMelodie( 1, Clavier.SI_GAMME_1),
                                        new NoteMelodie( 2, Clavier.LA_GAMME_1)}
                    );

    private NoteMelodie[] ensembleNotesMelodie;

    EnsembleNotesMelodies(NoteMelodie[] ensembleNotesMelodie) {
        this.ensembleNotesMelodie = ensembleNotesMelodie;
    }

    public NoteMelodie[] getEnsembleNotesMelodie() {
        return ensembleNotesMelodie;
    }

    public void setEnsembleNotesMelodie(NoteMelodie[] ensembleNotesMelodie) {
        this.ensembleNotesMelodie = ensembleNotesMelodie;
    }
}
