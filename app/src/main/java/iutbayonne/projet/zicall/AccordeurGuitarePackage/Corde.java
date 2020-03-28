package iutbayonne.projet.zicall.AccordeurGuitarePackage;

public enum Corde {

    MI(82.4),
    LA(110.0),
    RE(146.8),
    SOL(196.0),
    SI(246.9),
    MI_AIGU(329.6);

    private double frequenceReferenceCorde;

    Corde(double frequenceReferenceCorde) {
        this.frequenceReferenceCorde = frequenceReferenceCorde;
    }

    public boolean estDansIntervaleFrequenceAcceptable(float frequenceMesuree){
        double borneInferieure = getFrequenceReferenceCorde() - 0.5;
        double borneSuperieure = getFrequenceReferenceCorde() + 0.5;
        return (borneInferieure <= frequenceMesuree && frequenceMesuree <= borneSuperieure);
    }

    public double getFrequenceReferenceCorde() {
        return frequenceReferenceCorde;
    }

    public double getFrequenceMinimalAcceptable(){
        return getFrequenceReferenceCorde() - 0.5;
    }

    public double getFrequenceMaximaleAcceptable(){
        return getFrequenceReferenceCorde() + 0.5;
    }
}
