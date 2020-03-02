package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import iutbayonne.projet.zicall.R;

public enum Accord {

    DO("Do", R.drawable.do_majeur, R.raw.do_majeur),
    DO_MINEUR("Do_mineur", R.drawable.dom, R.raw.dom),
    DO_DIESE("Do_diese", R.drawable.do_diese, R.raw.dod),
    DO_DIESE_MINEUR("Do_diese_mineur", R.drawable.dom_diese, R.raw.dodm),
    RE("Re", R.drawable.re, R.raw.re),
    RE_MINEUR("Re_mineur", R.drawable.rem, R.raw.rem),
    RE_DIESE("Re_diese", R.drawable.re_diese, R.raw.red),
    RE_DIESE_MINEUR("Re_diese_mineur", R.drawable.rem_diese, R.raw.redm),
    MI("Mi", R.drawable.mi, R.raw.mi),
    MI_MINEUR("Mi_mineur", R.drawable.mim, R.raw.mim),
    FA("Fa", R.drawable.fa, R.raw.fa),
    FA_MINEUR("Fa_mineur", R.drawable.fam, R.raw.fam),
    FA_DIESE("FA_diese", R.drawable.fa_diese, R.raw.fad),
    FA_DIESE_MINEUR("Fa_diese_mineur", R.drawable.fam_diese, R.raw.fadm),
    SOL("Sol", R.drawable.sol, R.raw.sol),
    SOL_MINEUR("Sol_mineur", R.drawable.solm, R.raw.solm),
    SOL_DIESE("Sol_diese", R.drawable.sol_disese, R.raw.sold),
    SOL_DIESE_MINEUR("Sol_diese_mineur", R.drawable.solm_diese, R.raw.soldm),
    LA("La", R.drawable.la, R.raw.la),
    LA_MINEUR("La_mineur", R.drawable.lam, R.raw.lam),
    LA_DIESE("La_diese", R.drawable.la_diese, R.raw.lad),
    LA_DIESE_MINEUR("La_diese_mineur", R.drawable.lam_diese, R.raw.ladm),
    SI("Si", R.drawable.si, R.raw.si),
    SI_MINEUR("Si_mineur", R.drawable.sim, R.raw.sim),
    AIDE_LECTURE_TABLATURE("Aide_lecture_tablature", R.drawable.aide_lecture_tablature, R.raw.sim);


    private String nomAccord;
    private int imageAccord;
    private int audioAccord;

    Accord(String nomAccord, int imageAccord, int audioAccord) {
        this.nomAccord = nomAccord;
        this.imageAccord = imageAccord;
        this.audioAccord = audioAccord;
    }

    public String getNomAccord() {
        return nomAccord;
    }

    public void setNomAccord(String nomAccord) {
        this.nomAccord = nomAccord;
    }

    public int getImageAccord() {
        return imageAccord;
    }

    public void setImageAccord(int imageAccord) {
        this.imageAccord = imageAccord;
    }

    public int getAudioAccord() {
        return audioAccord;
    }

    public void setAudioAccord(int audioAccord) {
        this.audioAccord = audioAccord;
    }
}
