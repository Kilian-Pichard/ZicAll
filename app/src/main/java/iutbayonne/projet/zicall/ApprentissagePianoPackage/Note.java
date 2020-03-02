package iutbayonne.projet.zicall.ApprentissagePianoPackage;

public class Note
{

    private float duree;
    private Touche maTouche;

    public Note(Touche uneTouche, float duree)
    {
        this.duree = duree;
        this.maTouche = uneTouche;
    }

    public float getDuree()
    {
        return duree;
    }

    public void setDuree(float duree)
    {
        this.duree = duree;
    }

    public Touche getTouche()
    {
        return maTouche;
    }

    public void setTouche(Touche maTouche)
    {
        this.maTouche = maTouche;
    }
}
