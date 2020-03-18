package iutbayonne.projet.zicall.ApprentissagePianoPackage.MelodiePackage;

import iutbayonne.projet.zicall.ApprentissagePianoPackage.ClavierPianoPackage.Clavier;
import iutbayonne.projet.zicall.R;

public enum EnsembleNotesMelodies {
    NOTES_BELLA_CIAO( new NoteMelodie[]{new NoteMelodie((float) 0.5, R.raw.mi1 , Clavier.MI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.la1 , Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.si1 , Clavier.SI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.do2 , Clavier.DO_GAMME_2),
                                        new NoteMelodie( 2, R.raw.la1 , Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.mi1 , Clavier.MI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.la1 , Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.si1 , Clavier.SI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.do2 , Clavier.DO_GAMME_2),
                                        new NoteMelodie( 2, R.raw.la1 , Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.mi1 , Clavier.MI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.la1 , Clavier.LA_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.si1 , Clavier.SI_GAMME_1),
                                        new NoteMelodie( 1, R.raw.do2 , Clavier.DO_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.si1 , Clavier.SI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.la1 , Clavier.LA_GAMME_1),
                                        new NoteMelodie( 1, R.raw.do2 , Clavier.DO_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.si1 , Clavier.SI_GAMME_1),
                                        new NoteMelodie( (float) 0.5, R.raw.la1 , Clavier.LA_GAMME_1),
                                        new NoteMelodie( 1, R.raw.mi2 , Clavier.MI_GAMME_2),
                                        new NoteMelodie( 1, R.raw.mi2 , Clavier.MI_GAMME_2),
                                        new NoteMelodie( 1, R.raw.mi2 , Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.re2 , Clavier.RE_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.mi2 , Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.fa2 , Clavier.FA_GAMME_2),
                                        new NoteMelodie( 2, R.raw.fa2 , Clavier.FA_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.fa2 , Clavier.FA_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.mi2 , Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.re2 , Clavier.RE_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.fa2 , Clavier.FA_GAMME_2),
                                        new NoteMelodie( 2, R.raw.mi2 , Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.mi2 , Clavier.MI_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.re2 , Clavier.RE_GAMME_2),
                                        new NoteMelodie( (float) 0.5, R.raw.do2 , Clavier.DO_GAMME_2),
                                        new NoteMelodie( 1, R.raw.si1 , Clavier.SI_GAMME_1),
                                        new NoteMelodie( 1, R.raw.mi2 , Clavier.MI_GAMME_2),
                                        new NoteMelodie( 1, R.raw.do2 , Clavier.DO_GAMME_2),
                                        new NoteMelodie( 1, R.raw.si1 , Clavier.SI_GAMME_1),
                                        new NoteMelodie( 2, R.raw.la1 , Clavier.LA_GAMME_1)}
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
