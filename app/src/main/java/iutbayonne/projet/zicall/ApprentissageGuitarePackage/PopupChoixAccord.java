package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import iutbayonne.projet.zicall.R;

public class PopupChoixAccord extends Dialog {

    public Button doMajeur,re,mi,fa,sol,la,si,
                    doMineur,reMineur,miMineur,faMineur,solMineur,laMineur,siMineur,
                    doDiese,reDiese,faDiese,solDiese,laDiese,
                    doMineurDiese,reMineurDiese,faMineurDiese,solMineurDiese,laMineurDiese,
                    aide;

    public PopupChoixAccord(Activity activity){
        super(activity, R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.popup_choix_accord);

        this.doMajeur = findViewById(R.id.btnDo);
        this.re = findViewById(R.id.btnRe);
        this.mi = findViewById(R.id.btnMi);
        this.fa = findViewById(R.id.btnFa);
        this.sol = findViewById(R.id.btnSol);
        this.la = findViewById(R.id.btnLa);
        this.si = findViewById(R.id.btnSi);
        this.doMineur = findViewById(R.id.btnDom);
        this.reMineur = findViewById(R.id.btnRem);
        this.miMineur = findViewById(R.id.btnMim);
        this.faMineur = findViewById(R.id.btnFam);
        this.solMineur = findViewById(R.id.btnSolm);
        this.laMineur = findViewById(R.id.btnLam);
        this.siMineur = findViewById(R.id.btnSim);
        this.doDiese = findViewById(R.id.btnDoD);
        this.reDiese = findViewById(R.id.btnReD);
        this.faDiese = findViewById(R.id.btnFaD);
        this.solDiese = findViewById(R.id.btnSolD);
        this.laDiese = findViewById(R.id.btnLaD);
        this.doMineurDiese = findViewById(R.id.btnDomD);
        this.reMineurDiese = findViewById(R.id.btnRemD);
        this.faMineurDiese = findViewById(R.id.btnFamD);
        this.solMineurDiese = findViewById(R.id.btnSolmD);
        this.laMineurDiese = findViewById(R.id.btnLamD);
        this.aide = findViewById(R.id.aide);
    }

    /**
     * Affiche le popup
     */
    public void build()
    {
        show();
    }
}
