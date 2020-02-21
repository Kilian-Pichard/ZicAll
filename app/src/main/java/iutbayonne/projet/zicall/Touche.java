package iutbayonne.projet.zicall;

public class Touche
{

    private String nom;
    private int idImage;
    private boolean noire;

    public Touche(String nom, int idImage, boolean estNoire)
    {
        this.nom = nom;
        this.idImage = idImage;
        this.noire = estNoire;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public String getNom()
    {
        return this.nom;
    }

    public int getIdImage()
    {
        return this.idImage;
    }

    public void setIdImage(int idImage)
    {
        this.idImage = idImage;
    }

    public boolean estNoire()
    {
        return this.noire;
    }

    public void setNoire(boolean estNoire)
    {
        this.noire = estNoire;
    }
}
