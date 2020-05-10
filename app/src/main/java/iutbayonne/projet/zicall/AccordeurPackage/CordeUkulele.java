package iutbayonne.projet.zicall.AccordeurPackage;

public enum CordeUkulele {
    SOL("sol",392.0),
    UT("do",261.63),
    MI("mi",329.6),
    LA("la",440.00);

    /**
     * Nom de la corde.
     */
    private String nomCorde;

    /**
     * Fréquence que doit produire la corde si celle-ci est bien accordée.
     */
    private double frequenceReferenceCorde;

    /**
     * Constante qui définit l'écart maximal toléré entre la fréquence de référence de la corde et la
     * fréquence captée par le microphone : un écart supérieur signifie que la corde n'est pas bien accordée.
     */
    private final double ECART_MAXIMAL = 0.5;

    CordeUkulele(String nomCorde, double frequenceReferenceCorde)
    {
        this.nomCorde = nomCorde;
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

    public String getNomCorde(){
        return this.nomCorde;
    }

    public boolean estDansLIntervalleDeFrequenceDeCorde(float frequenceDetectee)
    {
        boolean resultat;
        if (this.nomCorde == "do")
        {
            resultat = ((Math.round(frequenceDetectee * 100.0)/100.0) <= this.getFrequenceReferenceCorde()+10.0);
        }
        else if(this.nomCorde == "la")
        {
            resultat = ((Math.round(frequenceDetectee * 100.0)/100.0) >= this.getFrequenceReferenceCorde()-10.0);
        }
        else
        {
            resultat = ((Math.round(frequenceDetectee * 100.0)/100.0) >= this.getFrequenceReferenceCorde()-10.0) && ((Math.round(frequenceDetectee * 100.0)/100.0) <= this.getFrequenceReferenceCorde()+10.0);
        }
        return resultat;
    }
}
