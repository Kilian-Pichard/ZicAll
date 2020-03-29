package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import iutbayonne.projet.zicall.R;

public class Recapitulatif_des_accords extends AppCompatActivity {

    private Accord[] accords;
    private Accord accordCourant;
    private int indiceAccord;
    private MediaPlayer audioAccordCourant;
    private ImageView imageAccordCourant;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapitulatif_des_accords);

        initialiserTableauAccords();
        this.imageAccordCourant = findViewById(R.id.imageAccordCourant);
        changerAccord(0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        this.imageAccordCourant = findViewById(R.id.imageAccordCourant);
        changerAccord(0);
    }

    public void initialiserTableauAccords()
    {
        this.accords = new Accord[]{ Accord.DO,
                                Accord.DO_MINEUR,
                                Accord.DO_DIESE,
                                Accord.DO_DIESE_MINEUR,
                                Accord.RE,
                                Accord.RE_MINEUR,
                                Accord.RE_DIESE,
                                Accord.RE_DIESE_MINEUR,
                                Accord.MI,
                                Accord.MI_MINEUR,
                                Accord.FA,
                                Accord.FA_MINEUR,
                                Accord.FA_DIESE,
                                Accord.FA_DIESE_MINEUR,
                                Accord.SOL,
                                Accord.SOL_MINEUR,
                                Accord.SOL_DIESE,
                                Accord.SOL_DIESE_MINEUR,
                                Accord.LA,
                                Accord.LA_MINEUR,
                                Accord.LA_DIESE,
                                Accord.LA_DIESE_MINEUR,
                                Accord.SI,
                                Accord.SI_MINEUR,
                                Accord.AIDE_LECTURE_TABLATURE};
    }

    public void jouerSonAccord(View view)
    {
        this.audioAccordCourant.start();
    }

    public void obtenirAccordSuivant(View view)
    {
        if(this.audioAccordCourant.isPlaying())
        {
            this.audioAccordCourant.stop();
        }
        this.audioAccordCourant.release();
        this.audioAccordCourant = null;
        indiceAccord++;
        if(indiceAccord == accords.length){
            indiceAccord = 0;
        }
        changerAccord(indiceAccord);
    }

    public void obtenirAccordPrecedent(View view)
    {
        if(this.audioAccordCourant.isPlaying())
        {
            this.audioAccordCourant.stop();
        }
        this.audioAccordCourant.release();
        this.audioAccordCourant = null;
        indiceAccord--;
        if(indiceAccord < 0){
            indiceAccord = accords.length - 1;
        }
        changerAccord(indiceAccord);
    }

    public void changerAccord(int nouvelIndiceAccord)
    {
        this.indiceAccord = nouvelIndiceAccord;
        this.accordCourant = accords[indiceAccord];
        this.audioAccordCourant = MediaPlayer.create(getApplicationContext(), accordCourant.getAudioAccord());
        this.imageAccordCourant.setImageResource(accordCourant.getImageAccord());
    }

    public void accederChoixChansonEntrainement(View view)
    {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_chanson_entrainement_guitare.class);

        if(this.audioAccordCourant.isPlaying())
        {
            this.audioAccordCourant.stop();
        }
        this.audioAccordCourant.release();
        this.audioAccordCourant = null;

        startActivity(otherActivity);
    }

    public void proposerChoixAccord(View view)
    {
        PopupChoixAccord popupChoixAccord = new PopupChoixAccord(this);
        initPopupChoixAccord(popupChoixAccord);
        popupChoixAccord.build();
    }

    public void initPopupChoixAccord(final PopupChoixAccord unPopup){
        unPopup.doMajeur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(0);
                unPopup.dismiss();
            }
        });


        unPopup.doMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(1);
                unPopup.dismiss();
            }
        });

        unPopup.doDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(2);
                unPopup.dismiss();
            }
        });

        unPopup.doMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(3);
                unPopup.dismiss();
            }
        });

        unPopup.re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(4);
                unPopup.dismiss();
            }
        });

        unPopup.reMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(5);
                unPopup.dismiss();
            }
        });

        unPopup.reDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(6);
                unPopup.dismiss();
            }
        });

        unPopup.reMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(7);
                unPopup.dismiss();
            }
        });

        unPopup.mi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(8);
                unPopup.dismiss();
            }
        });

        unPopup.miMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(9);
                unPopup.dismiss();
            }
        });

        unPopup.fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(10);
                unPopup.dismiss();
            }
        });

        unPopup.faMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(11);
                unPopup.dismiss();
            }
        });

        unPopup.faDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(12);
                unPopup.dismiss();
            }
        });

        unPopup.faMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(13);
                unPopup.dismiss();
            }
        });

        unPopup.sol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(14);
                unPopup.dismiss();
            }
        });

        unPopup.solMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(15);
                unPopup.dismiss();
            }
        });

        unPopup.solDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(16);
                unPopup.dismiss();
            }
        });

        unPopup.solMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(17);
                unPopup.dismiss();
            }
        });

        unPopup.la.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(18);
                unPopup.dismiss();
            }
        });

        unPopup.laMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(19);
                unPopup.dismiss();
            }
        });

        unPopup.laDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(20);
                unPopup.dismiss();
            }
        });

        unPopup.laMineurDiese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(21);
                unPopup.dismiss();
            }
        });

        unPopup.si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(22);
                unPopup.dismiss();
            }
        });

        unPopup.siMineur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerAccord(23);
                unPopup.dismiss();
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        if(this.audioAccordCourant.isPlaying())
        {
            this.audioAccordCourant.stop();
        }
        this.audioAccordCourant.release();
        this.audioAccordCourant = null;
        super.onBackPressed();
    }
}
