package iutbayonne.projet.zicall.ApprentissagePianoPackage.ClavierPianoPackage;

public class Touche {

    private String nomTouche;
    private int idImage;
    private boolean noire;

    Touche(String nomTouche, int idImage, boolean noire) {
        this.nomTouche = nomTouche;
        this.idImage = idImage;
        this.noire = noire;
    }

    public String getNomTouche() {
        return nomTouche;
    }

    public void setNomTouche(String nomTouche) {
        this.nomTouche = nomTouche;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public boolean isNoire() {
        return noire;
    }

    public void setNoire(boolean noire) {
        this.noire = noire;
    }
}
