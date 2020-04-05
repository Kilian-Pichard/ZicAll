package iutbayonne.projet.zicall.AccordeurGuitarePackage;

public enum Corde
{
    MI(82.4),
    LA(110.0),
    RE(146.8),
    SOL(196.0),
    SI(246.9),
    MI_AIGU(329.6);

    /**
     * Fréquence que doit produire la corde si celle-ci est bien accordée.
     */
    private double frequenceReferenceCorde;

    /**
     * Constante qui définit l'écart maximal toléré entre la fréquence de référence de la corde et la
     * fréquence captée par le microphone : un écart supérieur signifie que la corde n'est pas bien accordée.
     */
    private final double ECART_MAXIMAL = 0.5;

    Corde(double frequenceReferenceCorde)
    {
        this.frequenceReferenceCorde = frequenceReferenceCorde;
    }

    public boolean estDansIntervaleFrequenceAcceptable(float frequenceMesuree)
    {
        double borneInferieure = getFrequenceMinimaleAcceptable();
        double borneSuperieure = getFrequenceMaximaleAcceptable();
        return (frequenceMesuree >= borneInferieure && frequenceMesuree <= borneSuperieure);
    }

    public double getFrequenceReferenceCorde()
    {
        return frequenceReferenceCorde;
    }

    public double getFrequenceMinimaleAcceptable()
    {
        return getFrequenceReferenceCorde() - ECART_MAXIMAL;
    }

    public double getFrequenceMaximaleAcceptable()
    {
        return getFrequenceReferenceCorde() + ECART_MAXIMAL;
    }
}
