package iutbayonne.projet.zicall.ApprentissageGuitarePackage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Accord;
import iutbayonne.projet.zicall.ApprentissageGuitarePackage.Choix_chanson_entrainement_guitare;
import iutbayonne.projet.zicall.R;

public class Recapitulatif_des_accords extends AppCompatActivity {

    private Accord[] accords;
    private Accord accordCourant;
    private int indiceAccord;
    private MediaPlayer audioAccordCourant;
    private ImageView imageAccordCourant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapitulatif_des_accords);

        initialiserTableauAccords();
        this.imageAccordCourant = findViewById(R.id.imageAccordCourant);
        changerAccord(0);
    }

    public void initialiserTableauAccords(){
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

    public void jouerSonAccord(View view) {
        this.audioAccordCourant.start();
    }

    public void obtenirAccordSuivant(View view) {
        this.audioAccordCourant.stop();
        indiceAccord++;
        if(indiceAccord == accords.length){
            indiceAccord = 0;
        }
        changerAccord(indiceAccord);
    }

    public void obtenirAccordPrecedent(View view) {
        this.audioAccordCourant.stop();
        indiceAccord--;
        if(indiceAccord < 0){
            indiceAccord = accords.length - 1;
        }
        changerAccord(indiceAccord);
    }

    public void changerAccord(int nouvelIndiceAccord){
        this.indiceAccord = nouvelIndiceAccord;
        this.accordCourant = accords[indiceAccord];
        this.audioAccordCourant = MediaPlayer.create(getApplicationContext(), accordCourant.getAudioAccord());
        this.imageAccordCourant.setImageResource(accordCourant.getImageAccord());
    }

    public void accederChoixChansonEntrainement(View view) {
        Intent otherActivity;
        otherActivity = new Intent(getApplicationContext(), Choix_chanson_entrainement_guitare.class);
        startActivity(otherActivity);
    }
}
